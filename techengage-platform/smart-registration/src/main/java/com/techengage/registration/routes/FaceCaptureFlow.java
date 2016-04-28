package com.techengage.registration.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import com.techengage.components.camel.face.FaceComponent;
import com.techengage.components.camel.face.output.FaceOutputForDetection;
import com.techengage.registration.beans.ImageFaceFilter;
import com.techengage.registration.utility.IConstants;

import io.rhiot.component.webcam.WebcamComponent;

public class FaceCaptureFlow extends RouteBuilder {

    @Override
    public void configure() throws Exception {
	
	from("file://" + IConstants.REGISTRATION_TRIGGER_LOCATION).to("direct://iotFaceTrain");
	
        from("direct://iotFaceTrain").log("Received trigger to start face capture").convertBodyTo(String.class)
                .process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String triggerData = exchange.getIn().getBody(String.class);
                        if (!triggerData.contains(";")) {
                            throw new RuntimeException(
                                    "The data should be of the format personName;totalTrainingImageCount");
                        }
                        int firstIndexOf = triggerData.indexOf(";");

                        exchange.getIn().setHeader("PERSON_NAME", triggerData.substring(0, firstIndexOf));//TRAINING_PERSON_ALIAS_NAME
                        exchange.getIn().setHeader("TOTAL_TRAINING_IMAGE_COUNT", triggerData.substring(firstIndexOf + 1));

                    }
                }).setHeader("CURRENT_IMAGE_COUNT", constant(0))
                .to("direct:takeSnap");

        from("direct:takeSnap").log("Taking Snap!!").to("webcam:faceCapture").to("direct:processCameraImage");

        from("direct:processCameraImage")
                .log("Processing the Camera image")
                .choice()
                .when(method(ImageFaceFilter.class))
                    // set header FACE_PP_IMAGE as the image in bytes
                    .to("face://detection?apiKey="+ IConstants.API_KEY + "&apiSecret=" + IConstants.API_SECRET
                            + "&imageSource=${in.header.FACE_PP_IMAGE}&personAliasName=${in.header.PERSON_NAME}") //TODO: personAliasName is not required for /detection/detect of faceAPI 
                    .to("direct:postFaceDetection")
                .otherwise() // 1st choice -- filter
                    .log(LoggingLevel.ERROR, "Image shot is Not a Face!!!")// check to ensure if facepp denies the snap to be a face despite openImaj recognizing
                    // it as face
                    .process(new Processor() {
    
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            Integer currentImageCount = (Integer) exchange.getIn().getHeader("CURRENT_IMAGE_COUNT");
                            exchange.getIn().setHeader("CURRENT_IMAGE_COUNT", currentImageCount - 1);
                        }
                    })
                    .to("direct:takeSnap");

        from("direct:postFaceDetection")
        	.doTry()
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        FaceOutputForDetection body = exchange.getIn().getBody(FaceOutputForDetection.class);
                        String faceId = body.getFaceId();
                        if (faceId == null) {
                            throw new RuntimeException("No FaceId received for image.");
                        } else {
                            StringBuilder faceIdsBuilder = (StringBuilder) exchange.getIn().getHeader("FACE_ID_LIST");// set of face IDs
                            faceIdsBuilder = faceIdsBuilder == null ? new StringBuilder() : faceIdsBuilder;
                            faceIdsBuilder = faceIdsBuilder.append(faceId + ",");
                            exchange.getIn().setHeader("FACE_ID_LIST", faceIdsBuilder);
                        }
                    }
                })
                .doCatch(RuntimeException.class)
                .process(exchange -> {
                    Integer currentImageCount = (Integer) exchange.getIn().getHeader("CURRENT_IMAGE_COUNT");
                    System.err.println("** image count - "+(currentImageCount-1) );
                    exchange.getIn().setHeader("CURRENT_IMAGE_COUNT", currentImageCount - 1);
                })
                .to("direct:takeSnap")
                .end()
                .choice()
                .when(header("CURRENT_IMAGE_COUNT").isEqualTo(header("TOTAL_TRAINING_IMAGE_COUNT")))
                    .to("direct:createFacePerson")
                .otherwise() // 2nd choice -- continue taking snaps
                    .log("There are more snaps to capture")
                    .to("direct:takeSnap");

    }

    public static void main(String[] args) throws Exception {
        CamelContext camelcontext = new DefaultCamelContext();
        camelcontext.setTracing(true);
        camelcontext.addComponent("face", new FaceComponent());
        camelcontext.addComponent("webcam", new WebcamComponent());
        camelcontext.addRoutes(new FaceCaptureFlow());
        camelcontext.start();
        Thread.sleep(3600000);
        camelcontext.stop();

    }
}

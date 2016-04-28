package com.techengage.verification.routes;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import com.techengage.components.camel.face.FaceComponent;
import com.techengage.components.camel.face.output.FaceOutputForDetection;
import com.techengage.components.camel.face.output.FaceOutputForRecognition;
import com.techengage.verification.beans.ImageFaceFilter;
import com.techengage.verification.utility.IConstants;
import com.techengage.verification.utility.LoginForm;

import io.rhiot.component.webcam.WebcamComponent;

public class FaceRecognizeFlow extends RouteBuilder {

    @Override
    public void configure() throws Exception {

	from("file://" + IConstants.FACE_VERIFICATION_TRIGGER).log("Received trigger to start face capture").convertBodyTo(String.class).to("direct:personIdReceiver");

	from("direct:personIdReceiver").log(LoggingLevel.INFO, "Person ID received").setHeader("PERSON", body()).to("direct:takeSnap");

	from("direct:takeSnap").log("Taking Snap!!").to("webcam:faceCapture").to("direct:processCameraImage");

	from("direct:processCameraImage").log("Processing the Camera image").choice().when(method(ImageFaceFilter.class))
		// set header FACE_PP_IMAGE as the image in bytes
		.setHeader("TRAINING_PERSON_ALIAS_NAME", constant("ProbeImage"))
		// dummy name
		.to("face://detection?apiKey=" + IConstants.API_KEY + "&apiSecret=" + IConstants.API_SECRET
			+ "&imageSource=${in.header.FACE_PP_IMAGE}&personAliasName=${in.header.TRAINING_PERSON_ALIAS_NAME}")
		.to("direct:postFaceDetection").otherwise() // 1st choice --
		.log(LoggingLevel.INFO, "Image Not a Face!!!").to("direct:takeSnap");

	from("direct:postFaceDetection").process(new Processor() {

	    @Override
	    public void process(Exchange exchange) throws Exception {
		FaceOutputForDetection body = exchange.getIn().getBody(FaceOutputForDetection.class);
		String faceId = body.getFaceId();
		if (faceId == null) {
		    throw new RuntimeException("No FaceId received for image.");
		} else {
		    exchange.getIn().setHeader("keyFaceId", faceId);
		    String person = (String) exchange.getIn().getHeader("PERSON");
		    System.out.println("person =" + person);
		    if (!person.contains(";")) {
                throw new RuntimeException(
                        "The data should be of the format personId;userID");
            }
            int firstIndexOf = person.indexOf(";");
            String personId = person.substring(0, firstIndexOf);
            String userId = person.substring(firstIndexOf + 1);
            
            exchange.getIn().setHeader("personFullName", userId);
		    exchange.getIn().setHeader("trainingSetPersonNames", personId);
		}
	    }
	}).to("face://recognition?apiKey=" + IConstants.API_KEY + "&apiSecret=" + IConstants.API_SECRET 
		+ "&keyFaceId=${in.header.keyFaceId}" + "&trainedPersonNames=${in.header.trainingSetPersonNames}").process(new Processor() {

		    @Override
		    public void process(Exchange exchange) throws Exception {
			Double maxConfidence = new Double(0);
			@SuppressWarnings("unchecked")
			List<FaceOutputForRecognition> recognitionOutputList = exchange.getIn().getBody(List.class);
			if (!recognitionOutputList.isEmpty()) {
			    for (FaceOutputForRecognition faceOutputForRecognition : recognitionOutputList) {
				System.out.println(faceOutputForRecognition.getPersonName());
				System.out.println("IsSamePerson : " + faceOutputForRecognition.getIsSamePerson());
				
				exchange.getIn().setHeader("FACE_TRAIN_ELIGIBLE", "false");
				
				if (faceOutputForRecognition.getIsSamePerson()) {
				    if (faceOutputForRecognition.getConfidence() > maxConfidence) {
					exchange.getIn().setHeader("FACE_TRAIN_ELIGIBLE", "true");
					LoginForm.showAlert("Welcome " + exchange.getIn().getHeader("personFullName"));
				    }
				}else{
					LoginForm.showAlert("Sorry, " + exchange.getIn().getHeader("personFullName") + " could not be authenticated.");

				}
				
			    }

			}
		
			
		    }
		})
	.choice()
	.when(header("FACE_TRAIN_ELIGIBLE").isEqualTo("true"))
		.to("face://personManagement?apiKey=" + IConstants.API_KEY + "&apiSecret=" + IConstants.API_SECRET 
		+ "&personIdentifier=${in.header.trainingSetPersonNames}" + "&faceIds=${in.header.keyFaceId}&verb=addFace");
	

    }

    public static void main(String[] args) throws Exception {
	CamelContext camelcontext = new DefaultCamelContext();
	camelcontext.setTracing(true);
	camelcontext.addComponent("face", new FaceComponent());
	camelcontext.addComponent("webcam", new WebcamComponent());
	camelcontext.addRoutes(new FaceRecognizeFlow());
	camelcontext.start();
	Thread.sleep(3600000);
	camelcontext.stop();

    }
}

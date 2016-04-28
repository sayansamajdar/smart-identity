package com.techengage.registration.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import com.techengage.components.camel.face.FaceComponent;
import com.techengage.components.camel.face.output.FaceOutputForPersonManagement;
import com.techengage.idm.dataobject.UserProfile;
import com.techengage.registration.idm.IdmConnectionManager;
import com.techengage.registration.idm.IdmConnectionManagerImpl;
import com.techengage.registration.utility.IConstants;
import com.techengage.registration.utility.ImageUtils;

public class FaceTrainRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

	from("direct:createFacePerson").log("Person recieved").process(new Processor() {

	    @Override
	    public void process(Exchange exchange) throws Exception {

		StringBuilder faceIdsBuilder = (StringBuilder) exchange.getIn().getHeader("FACE_ID_LIST");// set
													  // of
		String faceIds = faceIdsBuilder.length() > 1 ? faceIdsBuilder.substring(0, faceIdsBuilder.length() - 1) : faceIdsBuilder.toString();
		exchange.getIn().setHeader("FACE_IDS", faceIds);
	    }
	}).to("face://personManagement?apiKey=" + IConstants.API_KEY + "&apiSecret=" + IConstants.API_SECRET
		+ "&personIdentifier=${in.header.PERSON_NAME}" + "&faceIds=${in.header.FACE_IDS}").process(new Processor() {

		    @Override
		    public void process(Exchange exchange) throws Exception {
			FaceOutputForPersonManagement body = (FaceOutputForPersonManagement) exchange.getIn()
				.getBody(FaceOutputForPersonManagement.class);
			exchange.getIn().setBody(body.getPersonId());

			IdmConnectionManager idmConnectionManager = IdmConnectionManagerImpl.getInstance();

			UserProfile userProfile = new UserProfile();
			userProfile.setUserName(body.getPersonName());
			userProfile.setFaceCode(body.getPersonId());

			boolean msg = idmConnectionManager.registrationFinalize(userProfile);
			if (msg) {
			    ImageUtils.showAlert("Registration of " + body.getPersonName() + " completed successfully");
			} else {
			    ImageUtils.showAlert("Registration failed.");
			}
		    }
		});

    }

    public static void main(String[] args) throws Exception {
	CamelContext camelcontext = new DefaultCamelContext();
	camelcontext.setTracing(true);
	camelcontext.addComponent("face", new FaceComponent());
	camelcontext.addRoutes(new FaceTrainRoute());
	camelcontext.start();
	Thread.sleep(3600000);
	camelcontext.stop();

    }

}

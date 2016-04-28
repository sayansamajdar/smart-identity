package com.techengage.components.camel.face;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.impl.DefaultProducer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.techengage.components.camel.face.output.FaceOutputForDetection;
import com.techengage.components.camel.face.output.FaceOutputForPersonManagement;
import com.techengage.components.camel.face.output.FaceOutputForRecognition;

public class FaceProducer extends DefaultProducer {
    private FaceEndpoint endpoint;

    public FaceProducer(FaceEndpoint endpoint) {
	super(endpoint);
	this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
	System.out.println("---------- Inside Face producer -----------------");

        HttpRequests httpRequests = new HttpRequests(endpoint.getApiKey(), endpoint.getApiSecret(), true, true);

	FaceEndpointType endpointType = endpoint.getEndpointType();

	if (endpointType.equals(FaceEndpointType.detection)) {
	    System.out.println("------- Face will DETECT ---------");
	    JSONObject result = detection(exchange, httpRequests);
	    System.out.println("Detection JSOn = " + result);

	    JSONArray faceArray = result.getJSONArray("face");
	    String faceId = null;
	    if (faceArray.length() == 0) {
		System.err.println("No Faces Detected!!!");
	    } else {
		faceId = faceArray.getJSONObject(0).getString("face_id");
	    }
	    System.out.println("faceId = " + faceId);
	    String sessionId = result.getString("session_id");
	    System.out.println("sessionId =" + sessionId);

	    FaceOutputForDetection detectionOutput = new FaceOutputForDetection(sessionId, endpointType.name());
	    detectionOutput.setFaceId(faceId);

	    exchange.getIn().setBody(detectionOutput);
	    exchange.getIn().setHeader(FaceConstants.FACE_ACTION_TYPE, endpointType.name());

	} else if (endpointType.equals(FaceEndpointType.recognition)) {
	    System.out.println("------- Face will RECOGNIZE ---------");
	    Expression expr = endpoint.getKeyFaceId();
	    String keyFaceId = expr.evaluate(exchange, String.class);
	    String personNames = endpoint.getTrainedPersonNames().evaluate(exchange, String.class);
	    System.out.println("keyfaceid - " + keyFaceId + " person names - " + personNames);

	    if (keyFaceId == null || personNames == null) {
		exchange.setException(new CamelException(
			"Endpoint Type recognize needs BOTH endpoint URI options - keyFaceId and recognizePersonNames to be not null!!"));
		throw new IllegalArgumentException(
			"Endpoint Type recognize needs BOTH endpoint URI options - keyFaceId and recognizePersonNames to be not null!!");

	    }
	    List<FaceOutputForRecognition> recognitionList = recognize(httpRequests, keyFaceId, personNames);
	    exchange.getIn().setBody(recognitionList);// TODO do this for others
	    exchange.getIn().setHeader(FaceConstants.FACE_ACTION_TYPE, endpointType.name());

	} else if (endpointType.equals(FaceEndpointType.personManagement)) {
	    System.out.println("------- Face will do Person Management ---------");
	    FaceOutputForPersonManagement personManagement = personManagement(exchange, httpRequests);
	    System.out.println("personManagementResult : " + personManagement);
	    exchange.getIn().setBody(personManagement);
	    exchange.getIn().setHeader(FaceConstants.FACE_ACTION_TYPE, endpointType.name());
	}

    }

    private List<FaceOutputForRecognition> recognize(HttpRequests httpRequests, String keyFaceId, String personNames) throws Exception {
	
	List<FaceOutputForRecognition> recognitionOutputList = new ArrayList<FaceOutputForRecognition>();

	String[] persons = personNames.contains(",") ? personNames.split(",") : new String[] { personNames };
	System.out.println("No of persons to be evalutaed against = " + persons.length);

	for (String person : persons) {
	    System.out.println("Recognizing against person - " + person);
	 //   JSONObject result = httpRequests.recognitionVerify(new PostParameters().setPersonName(person).setFaceId(keyFaceId));
	    JSONObject result = httpRequests.recognitionVerify(new PostParameters().setPersonId(person).setFaceId(keyFaceId));

	    System.out.println("Recognition Result " + result);

	    FaceOutputForRecognition recognitionOutput = new FaceOutputForRecognition(result.getString("session_id"),
		    FaceEndpointType.recognition.name());
	    recognitionOutput.setConfidence(result.getDouble("confidence"));
	    recognitionOutput.setIsSamePerson(result.getBoolean("is_same_person"));
	    //recognitionOutput.setPersonName(person);
	    recognitionOutput.setPersonId(person);
	    recognitionOutputList.add(recognitionOutput);
	}
	return recognitionOutputList;

    }

    private FaceOutputForPersonManagement personManagement(Exchange exchange, HttpRequests httpRequests) throws Exception {
	PersonManagementVerb verb = getVerb();
	System.out.println("Verb :: " + verb);
	Expression personIdentifierExpression = endpoint.getPersonIdentifier();
	String personIdentifier = personIdentifierExpression.evaluate(exchange, String.class);
	if (personIdentifier == null) {//TODO check if expr itself will be null
	    throw new RuntimeException("Endpoint URI option personName cannot be null for endpoint Type - " + FaceEndpointType.personManagement);
	}
	switch (verb) {
	case addPerson: {
	    
	    Expression expr  = endpoint.getFaceIds();
	    String faceIds = expr.evaluate(exchange, String.class);
	    System.out.println("Face Ids = " + faceIds);
	    if (faceIds == null) {
		throw new RuntimeException("personMgmtFaceIds cannot be null for endpoint Type - " + FaceEndpointType.personManagement);
	    }
	    
	    JSONObject result = httpRequests.personCreate(new PostParameters().setPersonName(personIdentifier).setFaceId(faceIds));
	    System.out.println("Result :: " + result);
	    
	    JSONObject resultTrain = httpRequests.trainVerify(new PostParameters().setPersonName(personIdentifier));
	    System.out.println("Result Train :: " + resultTrain);
	    
	    FaceOutputForPersonManagement output = new FaceOutputForPersonManagement(null, FaceEndpointType.personManagement.name());
	    output.setVerb(verb.name());
	    output.setPersonId(result.getString("person_id"));
	    output.setPersonName(result.getString("person_name"));
	    return output;
	}
	case removePerson: {
	    JSONObject result = httpRequests.personDelete(new PostParameters().setPersonName(personIdentifier));
	    System.out.println("Result :: " + result);
	    FaceOutputForPersonManagement output = new FaceOutputForPersonManagement(null, FaceEndpointType.personManagement.name());
	    // output.setVerb(verb.name());
	    // output.setPersonId(result.getString("person_id"));
	    // output.setPersonName(result.getString("person_name"));
	    return output;
	}
	case addFace: {
	    Expression expr  = endpoint.getFaceIds();
	    String faceIds = expr.evaluate(exchange, String.class);
	    System.out.println("Face Ids = " + faceIds);
	    if (faceIds == null) {
		throw new RuntimeException("personMgmtFaceIds cannot be null for endpoint Type - " + FaceEndpointType.personManagement);
	    }
	   // JSONObject result = httpRequests.personAddFace(new PostParameters().setPersonName(personIdentifier).setFaceId(faceIds));
	    JSONObject result = httpRequests.personAddFace(new PostParameters().setPersonId(personIdentifier).setFaceId(faceIds));
	    System.out.println("Added faces to person " + personIdentifier + " The response is " + result);
	    System.out.println("Result :: " + result);
	   // JSONObject resultTrain = httpRequests.trainVerify(new PostParameters().setPersonName(personIdentifier));
	    JSONObject resultTrain = httpRequests.trainVerify(new PostParameters().setPersonId(personIdentifier));

	    System.out.println("Result Train :: " + resultTrain);
	    FaceOutputForPersonManagement output = new FaceOutputForPersonManagement(null, FaceEndpointType.personManagement.name());
	    output.setPersonName(personIdentifier);
	    output.setFaceIds(faceIds);
	    output.setVerb(verb.name());
	    output.setSessionId(resultTrain.getString("session_id"));
	    return output;

	}
	case removeFace: {// TODO output object
	    Expression expr  = endpoint.getFaceIds();
	    String faceIds = expr.evaluate(exchange, String.class);
	    System.out.println("Face Ids = " + faceIds);
	    if (faceIds == null) {
		throw new RuntimeException("personMgmtFaceIds cannot be null for endpoint Type - " + FaceEndpointType.personManagement);
	    }
	    JSONObject result = httpRequests.personRemoveFace(new PostParameters().setPersonName(personIdentifier).setFaceId(faceIds));
	    System.out.println("Removed faces from person " + personIdentifier + " The response is " + result);
	    JSONObject resultTrain = httpRequests.trainVerify(new PostParameters().setPersonName(personIdentifier));
	    FaceOutputForPersonManagement output = new FaceOutputForPersonManagement(null, FaceEndpointType.personManagement.name());
	    return output;
	}
	default:
	    throw new UnsupportedOperationException(verb.toString());
	}
    }

    private JSONObject detection(Exchange exchange, HttpRequests httpRequests) throws Exception {
	Expression exprPersonNameAlias = endpoint.getPersonAliasName();
	if(exprPersonNameAlias == null){
	    throw new RuntimeException("Missing argument person alias name");
	}
	String personAliasName = exprPersonNameAlias.evaluate(exchange, String.class);
	exchange.getIn().setHeader("PERSON_ALIAS_NAME", personAliasName);
	
	PostParameters postParameters = new PostParameters();
	Expression expression = endpoint.getImageSource();
	Object img = expression.evaluate(exchange, Object.class);

	
	if (img instanceof byte[]) {
	    System.out.println("------------ Image Source is a byte arr -------------");
	    postParameters.setImg((byte[]) img);
	} else if (img instanceof File) {
	    System.out.println("------------ Image Source is a File -------------");

	    File fileImg = (File) img;
	    postParameters.setImg(fileImg);
	} else if (img instanceof String) {
	    System.out.println("------------ Image Source is a string URL -------------");
	    postParameters.setUrl((String) img);
	} else {
	    System.err.println("Unsupported type for image source.It has to be a byte[] or a java.io.File or java.lang.String URL");
	    throw new RuntimeException(
		    "Unsupported type for endpoint uri option imageSource.It has to be a byte[] or a java.io.File or java.lang.String URL");
	}
	postParameters.setMode("oneface");
	return httpRequests.detectionDetect(postParameters);

    }

    public FaceEndpoint getEndpoint() {
	return endpoint;
    }

    private PersonManagementVerb getVerb() {
	String verb = endpoint.getVerb();
	return verb != null ? PersonManagementVerb.valueOf(verb) : PersonManagementVerb.addPerson;
    }

    private enum PersonManagementVerb {
	addPerson, removePerson, addFace, removeFace;
    }

}

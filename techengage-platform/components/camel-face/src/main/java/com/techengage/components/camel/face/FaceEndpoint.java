package com.techengage.components.camel.face;

import org.apache.camel.Consumer;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Language;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Face endpoint.
 */
@UriEndpoint(scheme = "face", title = "Face", syntax = "face:endpointType", producerOnly = true, label = "face")
public class FaceEndpoint extends DefaultEndpoint {
    // common for all
    @UriParam(description = "Registered API Key")
    private String apiKey;
    @UriParam(description = "Registered API Secret")
    private String apiSecret;

    @UriPath(defaultValue = "detection", enums = "detection,recognition,personManagement")
    // TODO being atype maybe default not req
    @Metadata(required = "true")
    private final FaceEndpointType endpointType;

    // only detect
    @UriParam(description = "The source of the image - it can be a byte[] or a java.io.File or an java.lang.String URL ")
    private Expression imageSource;

    @UriParam(description = "Unique identifier (here emailID) of the person")
    private Expression personIdentifier; // personMgmt

    @UriParam(description = "Alias(nickname)Name of the person")
    private Expression personAliasName; // before detection enter alias

    // only personManagement

    @UriParam(defaultValue = "addPerson", description = "the type of person management - addperson,removePerson,addFace,removeFace", label = "producer", enums = "addperson, removePerson, addFace, removeFace")
    private String verb;

    // @UriParam(description = "Name of the person whose image is being uploaded")
    // private String personMgmtPersonName;

    @UriParam(description = "comma separated list of faceids associated with a person")
    private Expression faceIds;

    // only recognize
    @UriParam(description = "Face Id to be recognized")
    private Expression keyFaceId;

    @UriParam(description = "Comma separated person names from the training set , to recognize the key face id")
    private Expression trainedPersonNames;

    public FaceEndpoint(String uri, FaceComponent component, FaceEndpointType endpointType) {
	super(uri, component);
	this.endpointType = endpointType;
    }

    @Override
    public Producer createProducer() throws Exception {
	return new FaceProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
	throw new UnsupportedOperationException("Face component supports only producer endpoints.");
    }

    @Override
    public boolean isSingleton() {
	return true;
    }

    public String getApiKey() {
	return apiKey;
    }

    public void setApiKey(String apiKey) {
	this.apiKey = apiKey;
    }

    public String getApiSecret() {
	return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
	this.apiSecret = apiSecret;
    }

    public Expression getImageSource() {
	return imageSource;
    }

    public void setImageSource(Expression imageSource) {
	this.imageSource = imageSource;
    }

    public void setImageSource(String imageSourceExpression) {
	this.imageSource = createSimpleLanguageExpression(imageSourceExpression);
    }

    public Expression getPersonIdentifier() {
	return personIdentifier;
    }

    public void setPersonIdentifier(Expression personIdentifier) {
	this.personIdentifier = personIdentifier;
    }

    public void setPersonIdentifier(String personIdentifier) {
	this.personIdentifier = createSimpleLanguageExpression(personIdentifier);
    }

    public FaceEndpointType getEndpointType() {
	return endpointType;
    }

    public String getVerb() {
	return verb;
    }

    public void setVerb(String verb) {
	this.verb = verb;
    }

    public Expression getFaceIds() {
	return faceIds;
    }

    public void setFaceIds(Expression keyFaceId) {
	this.faceIds = keyFaceId;
    }

    public void setFaceIds(String faceIds) {
	this.faceIds = createSimpleLanguageExpression(faceIds);
    }

    public Expression getKeyFaceId() {
	return keyFaceId;
    }

    public void setKeyFaceId(Expression keyFaceId) {
	this.keyFaceId = keyFaceId;
    }

    public void setKeyFaceId(String keyFaceId) {
	this.keyFaceId = createSimpleLanguageExpression(keyFaceId);
    }

    public Expression getTrainedPersonNames() {
	return trainedPersonNames;
    }

    public void setTrainedPersonNames(String trainedPersonNames) {
	this.trainedPersonNames = createSimpleLanguageExpression(trainedPersonNames);
    }

    public void setTrainedPersonNames(Expression trainedPersonNames) {
	this.trainedPersonNames = trainedPersonNames;
    }

    public Expression getPersonAliasName() {
	return personAliasName;
    }

    public void setPersonAliasName(Expression personAliasName) {
	this.personAliasName = personAliasName;
    }

    public void setPersonAliasName(String personAliasName) {
	this.personAliasName = createSimpleLanguageExpression(personAliasName);
    }

    private Expression createSimpleLanguageExpression(String expression) {
	Language language;
	if (expression.contains("$")) {
	    language = getCamelContext().resolveLanguage("simple");
	} else {
	    language = getCamelContext().resolveLanguage("constant");
	}
	return language.createExpression(expression);
    }

}

package com.cts.iot.internal.transfer.objects;

public class Faces {
    private String faceId;
    private String personIdentifier;
    private String jsonObject;
    private String trainingImageURL;

    public String getFaceId() {
	return faceId;
    }

    public void setFaceId(String faceId) {
	this.faceId = faceId;
    }

    public String getPersonIdentifier() {
	return personIdentifier;
    }

    public void setPersonIdentifier(String personIdentifier) {
	this.personIdentifier = personIdentifier;
    }

    public String getJsonObject() {
	return jsonObject;
    }

    public void setJsonObject(String jsonObject) {
	this.jsonObject = jsonObject;
    }

    public String getTrainingImageURL() {
	return trainingImageURL;
    }

    public void setTrainingImageURL(String trainingImageURL) {
	this.trainingImageURL = trainingImageURL;
    }
   
}

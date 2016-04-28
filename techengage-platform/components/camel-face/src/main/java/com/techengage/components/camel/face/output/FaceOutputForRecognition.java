package com.techengage.components.camel.face.output;

public class FaceOutputForRecognition extends FaceOutputType {
    private Boolean isSamePerson;
    private String personName;
    private String personId;
    private Double confidence;

    public FaceOutputForRecognition(String sessionId, String faceAction) {
	super(sessionId, faceAction);
    }

    public Boolean getIsSamePerson() {
	return isSamePerson;
    }

    public void setIsSamePerson(Boolean isSamePerson) {
	this.isSamePerson = isSamePerson;
    }

    public String getPersonName() {
	return personName;
    }

    public void setPersonName(String personName) {
	this.personName = personName;
    }

    public Double getConfidence() {
	return confidence;
    }

    public void setConfidence(Double confidence) {
	this.confidence = confidence;
    }

    public String getPersonId() {
	return personId;
    }

    public void setPersonId(String personId) {
	this.personId = personId;
    }
    
    @Override
    public String toString() {
	return "FaceOutputForRecognition [isSamePerson=" + isSamePerson + ", personName=" + personName + ", confidence=" + confidence
		+ ", sessionId=" + sessionId + ", faceAction=" + faceAction + ", personId=" + personId + "]";
    }

}

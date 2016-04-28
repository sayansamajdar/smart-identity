package com.techengage.components.camel.face.output;

public class FaceOutputForPersonManagement extends FaceOutputType {
    private String personName;
    private String personId;
    private String verb;
    private String faceIds;

    public FaceOutputForPersonManagement(String sessionId, String faceAction) {
	super(sessionId, faceAction);
    }

    public String getPersonId() {
	return personId;
    }

    public void setPersonId(String personId) {
	this.personId = personId;
    }

    public String getPersonName() {
	return personName;
    }

    public void setPersonName(String personName) {
	this.personName = personName;
    }

    public String getVerb() {
	return verb;
    }

    public void setVerb(String verb) {
	this.verb = verb;
    }

    public String getFaceIds() {
	return faceIds;
    }

    public void setFaceIds(String faceIds) {
	this.faceIds = faceIds;
    }

    @Override
    public String toString() {
	return "FaceOutputForPersonManagement [personName=" + personName + ", personId=" + personId + ", verb=" + verb + ", faceIds=" + faceIds
		+ ", sessionId=" + sessionId + ", faceAction=" + faceAction + "]";
    }
    
    

}

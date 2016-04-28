package com.techengage.components.camel.face.output;

public class FaceOutputType {
    String sessionId;
    String faceAction;

    public FaceOutputType(String sessionId, String faceAction) {
	setSessionId(sessionId);
	setFaceAction(faceAction);
    }

    public String getSessionId() {
	return sessionId;
    }

    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }

    public String getFaceAction() {
	return faceAction;
    }

    public void setFaceAction(String faceAction) {
	this.faceAction = faceAction;
    }

}

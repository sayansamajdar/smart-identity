package com.techengage.components.camel.face.output;

import org.json.JSONObject;

public class FaceOutputForDetection extends FaceOutputType {
    private String faceId;
    private JSONObject result;

    public FaceOutputForDetection(String sessionId, String faceAction) {
	super(sessionId, faceAction);
    }

    public String getFaceId() {
	return faceId;
    }

    public void setFaceId(String faceId) {
	this.faceId = faceId;
    }

    public JSONObject getResult() {
	return result;
    }

    public void setResult(JSONObject result) {
	this.result = result;
    }

    @Override
    public String toString() {
	return "FaceOutputForDetection [faceId=" + faceId + ", sessionId=" + sessionId + ", faceAction=" + faceAction + "]";
    }

}

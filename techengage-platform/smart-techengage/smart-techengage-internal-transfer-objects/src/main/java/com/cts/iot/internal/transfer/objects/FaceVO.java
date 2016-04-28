package com.cts.iot.internal.transfer.objects;

public class FaceVO {
    private String faceId;
    private String personIdentifier;
    private String jsonObject;
    private byte[] imageInBytes;

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

    public byte[] getImageInBytes() {
	return imageInBytes;
    }

    public void setImageInBytes(byte[] imageInBytes) {
	this.imageInBytes = imageInBytes;
    }

}

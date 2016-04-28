package com.techengage.components.camel.openimaj;

public class FaceRecognitionResult {
    private String bestMatch;
    private double eucledianDistance;

    public String getBestMatch() {
	return bestMatch;
    }

    public void setBestMatch(String bestMatch) {
	this.bestMatch = bestMatch;
    }

    public double getEucledianDistance() {
	return eucledianDistance;
    }

    public void setEucledianDistance(double eucledianDistance) {
	this.eucledianDistance = eucledianDistance;
    }

}

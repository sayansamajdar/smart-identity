
package com.techengage.components.camel.face;

public enum FaceEndpointType {
    detection("detection"), recognition("recognition"),personManagement("personManagement");

    private final String text;

    private FaceEndpointType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

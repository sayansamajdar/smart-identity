package com.techengage.components.camel.camera;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

public class CameraComponent extends UriEndpointComponent {

    public CameraComponent() {
	super(CameraEndpoint.class);
    }

    public CameraComponent(CamelContext context) {
	super(context, CameraEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
	Endpoint endpoint = new CameraEndpoint(uri, this);
	setProperties(endpoint, parameters);
	return endpoint;
    }

}

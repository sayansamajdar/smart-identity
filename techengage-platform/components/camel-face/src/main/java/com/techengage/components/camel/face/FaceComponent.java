package com.techengage.components.camel.face;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

public class FaceComponent extends UriEndpointComponent {

	public FaceComponent() {
		super(FaceEndpoint.class);
	}

	public FaceComponent(CamelContext context, Class<? extends Endpoint> endpointClass) {
		super(context, endpointClass);
	}

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		String[] uriParts = remaining.split(":");
		System.out.println("FaceComponent :: URI part LENGTH = " + uriParts.length);
		if (uriParts.length != 1) {
			throw new IllegalArgumentException(
					"Invalid Endpoint URI: " + uri + ". It should contains a valid endpointType");
		}
		FaceEndpointType endpointType = FaceEndpointType.valueOf(uriParts[0]);
		Endpoint endpoint = new FaceEndpoint(uri, this, endpointType);
		Object parameter = endpoint.getEndpointConfiguration().getParameter("apiKey");
		System.out.println("FaceComponent :: parameter(apiKey) "+ parameter);
		setProperties(endpoint, parameters);
		return endpoint;
	}

}

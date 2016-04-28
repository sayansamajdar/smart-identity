package com.techengage.verification.verticle;

import java.util.HashSet;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.vertx.VertxComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;

import com.techengage.verification.utility.Utils;

import io.vertx.core.AbstractVerticle;

public class CamelVerticle extends AbstractVerticle {

    SimpleRegistry registry = null;
    CamelContext camelContext = null;

    @Override
    public void start() throws Exception {

        registry = new SimpleRegistry();
       // registry.put("httpClientConfigurer", new ParseHttpClientConfigurer());
        camelContext = new DefaultCamelContext(registry);

        VertxComponent vertxComponent = new VertxComponent();
        vertxComponent.setVertx(vertx);
        camelContext.addComponent("event-bus", vertxComponent);
        try {
            HashSet<RoutesBuilder> routes = Utils.getRoutesBuilders();
            for (RoutesBuilder route : routes) {
                camelContext.addRoutes(route);
            }
            camelContext.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        try {
            camelContext.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.techengage.verification.utility;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrap {

    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
    private final HashSet<BootInitializer> initializers = Utils.getInitializers();

    protected Bootstrap start() {
        for (BootInitializer initializer : initializers) {
            LOG.info("Starting: {}", initializer.getClass().getName());
            initializer.start();
        }
        return this;
    }

    protected Bootstrap stop() {
        for (BootInitializer initializer : Utils
                .getInitializersInReverseOrder(initializers)) {
            LOG.info("Stopping: {}", initializer.getClass().getName());
            initializer.stop();
        }
        return this;
    }

    public static void main(String[] args) {
        final Bootstrap bootstrap = new Bootstrap().start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("bye...");
                bootstrap.stop();
            }
        });
    }
}

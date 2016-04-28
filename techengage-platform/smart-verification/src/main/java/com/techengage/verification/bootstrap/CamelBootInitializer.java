package com.techengage.verification.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techengage.verification.utility.BootInitializer;

public class CamelBootInitializer implements BootInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(CamelBootInitializer.class);

    @Override
    public void start() {
	LOG.info("Blank start CamelBootInitializer");
    }

    @Override
    public void stop() {
	LOG.info("Blank stop CamelBootInitializer");
    }

    @Override
    public int order() {
	return 0;
    }
}

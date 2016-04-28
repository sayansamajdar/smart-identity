package com.techengage.components.camel.camera;

import java.awt.Dimension;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;
import com.techengage.components.camel.camera.utils.CommonUtils;

public class CameraEndpoint extends DefaultEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(CameraEndpoint.class);
    private Webcam webcam;

    @UriParam(defaultValue = "true", description = "Indicates if the endpoint should record still images or video")
    private boolean video = false;
    
    @UriParam(defaultValue = "",description = "Directory where image will be saved. It is a mandatory parameter")
    private String outputDirectory = "";
    
    // STILL Images
    @UriParam(defaultValue = "PNG", description = "Capture format, one of [PNG,GIF,JPG]")
    private String format = "PNG";
    @UriParam(defaultValue = "25", description = "Intensity threshold (0 - 255)")
    private int pixelThreshold = 25;
    @UriParam(defaultValue = "0.2", description = "Percentage threshold of image covered by motion (0 - 100)")
    private double areaThreshold = 0.2;

    @UriParam(description = "Default resolution to use, overriding width and height")
    private String resolution;
    @UriParam(defaultValue = "320", description = "Width in pixels, must be supported by the webcam")
    private int width = 320;
    @UriParam(defaultValue = "240", description = "Height in pixels, must be supported by the webcam")
    private int height = 240;

    // VIDEO
    @UriParam(defaultValue = "100", description = "Frame capture interval for recording video")
    private int frameCaptureInterval = 100;

    @UriParam(defaultValue = "50", description = "Number of frames to capture while recording video")
    private int frameCaptureCount = 50;

    
    public CameraEndpoint(String uri, CameraComponent component) {
        super(uri, component);
    }

    public CameraEndpoint(String endpointUri) {
        super(endpointUri);
        createEndpointConfiguration(endpointUri);
    }
    
    public Producer createProducer() throws Exception {
	Producer producer = isVideo() ? new CameraVideoProducer(this) : new CameraStillImagesProducer(this);
	return producer;
    }

    public Consumer createConsumer(Processor processor) throws Exception {
	throw new UnsupportedOperationException("This component cannot run as a Consumer " + getEndpointUri());
    }

    @Override
    protected void doStart() throws Exception 
    {
        if(CommonUtils.isBlank(getOutputDirectory()))
        {
            throw new IllegalStateException("Please set Output Directory Parameter Value!");
        }
        
	if (webcam == null) {
	    try {
		LOG.info("Loading V4l4jDriver");
		Webcam.setDriver(new V4l4jDriver()); // This is important for Raspberry Pi/Linux
	    } catch (UnsatisfiedLinkError e) {
		LOG.warn("Unable to load V4l4jDriver driver.");
	    }
	    webcam = Webcam.getDefault();
	}

	if (!webcam.isOpen()) {
	    webcam.setViewSize(getDefaultResolution());
	    if (webcam.open()) {
		LOG.info("Webcam device [{}] opened", webcam.getDevice().getName());
	    } else {
		throw new IllegalStateException("Failed to open webcam!");
	    }
	}

	super.doStart();
    }

    /**
     * Returns the default resolution by name if provided, eg HD720, otherwise the width and height.
     */
    private Dimension getDefaultResolution() {
	if (getResolution() != null) {
	    WebcamResolution res = WebcamResolution.valueOf(getResolution());
	    return res.getSize();
	} else {
	    return new Dimension(getWidth(), getHeight());
	}
    }

    @Override
    protected void doStop() throws Exception {
	if (webcam != null) {
	    webcam.close();
	    webcam = null;
	}
	super.doStop();
    }

    public boolean isSingleton() {
	return true;// TODO use? default was false!!
    }

    public Webcam getWebcam() {
	return webcam;
    }

    public void setWebcam(Webcam webcam) {
	this.webcam = webcam;
    }

    public String getFormat() {
	return format;
    }

    public void setFormat(String format) {
	this.format = format;
    }

    public int getPixelThreshold() {
	return pixelThreshold;
    }

    public void setPixelThreshold(int pixelThreshold) {
	this.pixelThreshold = pixelThreshold;
    }

    public double getAreaThreshold() {
	return areaThreshold;
    }

    public void setAreaThreshold(double areaThreshold) {
	this.areaThreshold = areaThreshold;
    }

    public String getResolution() {
	return resolution;
    }

    public void setResolution(String resolution) {
	this.resolution = resolution;
    }

    public int getWidth() {
	return width;
    }

    public void setWidth(int width) {
	this.width = width;
    }

    public int getHeight() {
	return height;
    }

    public void setHeight(int height) {
	this.height = height;
    }

    public int getFrameCaptureInterval() {
	return frameCaptureInterval;
    }

    public void setFrameCaptureInterval(int frameCaptureInterval) {
	this.frameCaptureInterval = frameCaptureInterval;
    }

    public int getFrameCaptureCount() {
	return frameCaptureCount;
    }

    public void setFrameCaptureCount(int frameCaptureCount) {
	this.frameCaptureCount = frameCaptureCount;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }
}

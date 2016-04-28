package com.techengage.components.camel.camera;

import java.awt.image.BufferedImage;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import com.github.sarxos.webcam.Webcam;
import com.techengage.components.camel.camera.utils.CommonUtils;

import java.io.File;
import javax.imageio.ImageIO;

public class CameraStillImagesProducer extends DefaultProducer {

    public CameraStillImagesProducer(CameraEndpoint endpoint) {
	super(endpoint);
    }

    public void process(Exchange exchange) throws Exception {
	Webcam webcam = getEndpoint().getWebcam();
        
	if (webcam == null) {
	    throw new IllegalStateException("No Webcam detected");
	}

	if (!webcam.isOpen()) {
	    throw new IllegalStateException("Webcam is closed");
	}

	BufferedImage image = webcam.getImage();
	if (image != null) {
            File imageFile=new File(CommonUtils.generateStillImageFileNamePath(getEndpoint().getOutputDirectory(), getEndpoint().getFormat()));
            log.error(imageFile.getAbsolutePath());
            ImageIO.write(image, getEndpoint().getFormat() , imageFile);
            log.info("Image saved at location :"+imageFile.getAbsolutePath());
	} else {
	    log.warn("No image returned from the webcam.");
	}
    }

    @Override
    public CameraEndpoint getEndpoint() {
	return (CameraEndpoint) super.getEndpoint();
    }

}

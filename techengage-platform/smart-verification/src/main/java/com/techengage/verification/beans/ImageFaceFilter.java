package com.techengage.verification.beans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.camel.Exchange;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import com.techengage.verification.utility.LoginForm;

public class ImageFaceFilter {
    private static JFrame frame;

    static {
	frame = new JFrame("FaceCam");
	frame.setSize(500, 500);
	frame.setLocationRelativeTo(null);
    }

    public boolean accept(Exchange exchange) throws IOException {
	boolean accept = false;
	byte[] imageInByte = (byte[]) exchange.getIn().getBody();
	InputStream in = new ByteArrayInputStream(imageInByte);
	BufferedImage cameraImage = ImageIO.read(in);

	if (cameraImage != null) {
	    System.out.println("Camera took a snap.Detecting if it is a human face");
	    FaceDetector<DetectedFace, FImage> faceDetector = new HaarCascadeDetector();
	    List<DetectedFace> detectedFaces = faceDetector.detectFaces(ImageUtilities.createFImage(cameraImage));
	    if (detectedFaces.isEmpty() || detectedFaces.size() > 1) {
		System.err.println("Ignoring this snap as either no face was detected in the image or there were multiple faces detected.");
	    } else {
		DetectedFace detectedFace = detectedFaces.get(0);
		MBFImage mBFImage = ImageUtilities.createMBFImage(cameraImage, true);
		mBFImage.drawShape(detectedFace.getBounds(), 5, RGBColour.RED);
		LoginForm.display(ImageUtilities.createBufferedImage(mBFImage));
		accept = true;
	    }
	}
	exchange.getIn().setHeader("FACE_PP_IMAGE", imageInByte);
	return accept;

    }

}

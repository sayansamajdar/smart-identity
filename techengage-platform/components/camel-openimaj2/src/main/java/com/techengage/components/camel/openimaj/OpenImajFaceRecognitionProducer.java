package com.techengage.components.camel.openimaj;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.MapBackedDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.model.EigenImages;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

public class OpenImajFaceRecognitionProducer extends DefaultProducer {

    private FaceDetector<DetectedFace, FImage> faceDetector;

    private String trainedFilePath;

    private Float recognitionThreshold;

    public OpenImajFaceRecognitionProducer(OpenImajEndpoint endpoint) {
	super(endpoint);
	// this.faceDetector = endpoint.getFaceDetector();
	this.trainedFilePath = endpoint.getTrainingFilePath();
	this.recognitionThreshold = endpoint.getRecognitionThreshold();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
	if (trainedFilePath == null) {
	    System.err.println("Please specify a training file path");
	    throw new RuntimeException("Please specify a training file path");
	}
	InputStream inputStream = exchange.getIn().getBody(InputStream.class);
	if (inputStream != null) {
	    try {

		MBFImage image = ImageUtilities.readMBF(inputStream);
		if (image != null) {
		    BufferedImage bufferedImage = ImageUtilities.createBufferedImage(image);
		    // get test image
		    FImage testImage = ImageUtilities.createFImage(bufferedImage);

		    // load training
		    System.out.println("Load training");
		    EigenImages eigen = new EigenImages(10);// TODO param
		    eigen.readBinary(new DataInputStream(new FileInputStream(trainedFilePath)));

		    // extract features
		    Map<String, DoubleFV[]> features = new HashMap<String, DoubleFV[]>();

		    // ----------------START-----------------------------//TODO remove this segment and replace with appropriate code

		    //just extracts the features and populates a map of features
		    VFSGroupDataset<FImage> datasetTraining = new VFSGroupDataset<FImage>("D:/work/project_POCs/iot/face-db/eigenTest/training",
			    ImageUtilities.FIMAGE_READER);

		    GroupedDataset<String, ListDataset<FImage>, FImage> training = new MapBackedDataset<String, ListDataset<FImage>, FImage>();
		    Set<String> trainingKeys = datasetTraining.keySet();
		    for (String key : trainingKeys) {
			VFSListDataset<FImage> vfsListDataset = datasetTraining.get(key);
			training.put(key, vfsListDataset);
			System.out.println(vfsListDataset);
		    }

		    for (final String person : training.getGroups()) {
			final DoubleFV[] fvs = new DoubleFV[10];
			for (int i = 0; i < 10; i++) {
			    final FImage face = training.get(person).get(i);
			    fvs[i] = eigen.extractFeature(face);
			}
			features.put(person, fvs);
		    }

		    // ---------------END--------------------------

		    // test probe
		    System.out.println("Begin test");
		    DoubleFV testFeature = eigen.extractFeature(testImage);

		    String bestPerson = null;
		    double minDistance = Double.MAX_VALUE;
		    for (final String person : features.keySet()) {
			for (final DoubleFV fv : features.get(person)) {
			    double distance = fv.compare(testFeature, DoubleFVComparison.EUCLIDEAN);
			    if (distance < minDistance && distance <= recognitionThreshold) {// TODO review 2nd and condition
				minDistance = distance;
				bestPerson = person;
			    }
			}
		    }
		    System.out.println(" guess: " + bestPerson + "\tdist: " + minDistance);

		    if (minDistance <= recognitionThreshold) {
			System.out.println("The match is acceptable.");
		    }
		    // set the result as output

		    FaceRecognitionResult faceRecognitionResult = new FaceRecognitionResult();
		    faceRecognitionResult.setBestMatch(bestPerson);
		    faceRecognitionResult.setEucledianDistance(minDistance);

		    exchange.getIn().setBody(faceRecognitionResult);

		}

	    } finally {
		inputStream.close();
	    }
	}
    }

    @Override
    public OpenImajEndpoint getEndpoint() {
	return (OpenImajEndpoint) super.getEndpoint();
    }

    @Override
    protected void doStart() throws Exception {
	// if (faceDetector == null) {
	// faceDetector = new HaarCascadeDetector();
	// }
	super.doStart();
    }

}

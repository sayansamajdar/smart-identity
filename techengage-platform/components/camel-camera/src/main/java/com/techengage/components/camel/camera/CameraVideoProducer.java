package com.techengage.components.camel.camera;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.techengage.components.camel.camera.utils.CommonUtils;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

public class CameraVideoProducer extends DefaultProducer {

    public CameraVideoProducer(CameraEndpoint endpoint) 
    {
  	super(endpoint);
    }

    public void process(Exchange exchange) throws Exception 
    {
      Webcam webcam = getEndpoint().getWebcam();
      
      if (webcam == null) {
          throw new IllegalStateException("No Webcam detected!");
      }

      if (!webcam.isOpen()) {
          throw new IllegalStateException("Webcam is closed!");
      }
      
      File videoFile=new File(CommonUtils.generateStillImageFileNamePath(getEndpoint().getOutputDirectory()));
      IMediaWriter writer = ToolFactory.makeWriter(videoFile.getAbsolutePath());
      Dimension size = WebcamResolution.QVGA.getSize();

      writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);

      int frameCaptureCount = getEndpoint().getFrameCaptureCount();
      int frameCaptureInterval = getEndpoint().getFrameCaptureInterval();
      
      log.info("Frame Capture Count="+frameCaptureCount+" frameCaptureInterval="+frameCaptureInterval+" File Path:"+videoFile.getAbsolutePath());

      long start = System.currentTimeMillis();

      for (int i = 0; i < frameCaptureCount; i++) 
      {
          BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
          IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

          IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
          frame.setKeyFrame(i == 0);
          frame.setQuality(0);

          writer.encodeVideo(0, frame);

          // 10 FPS
          Thread.sleep(frameCaptureInterval);
      }
      writer.close();
    }

      @Override
      public CameraEndpoint getEndpoint() {
  	return (CameraEndpoint) super.getEndpoint();
      }
}

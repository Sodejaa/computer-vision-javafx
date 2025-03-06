package org.example.capture;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import org.example.FaceDetection;
import com.github.sarxos.webcam.Webcam;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CaptureWebcam implements CaptureSource {
    private final ImageView imageView;
    private final ScheduledExecutorService executor;
    private Webcam webcam;
    private final FaceDetection faceDetection;

    private volatile boolean isPaused = false;

    public CaptureWebcam(final ImageView imageView) {
        this.imageView = imageView;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.faceDetection = new FaceDetection();
    }

    @Override
    public void start() {
        webcam = Webcam.getDefault();
        if (webcam != null) {
            webcam.setViewSize(new java.awt.Dimension(640, 480));
            webcam.open();
            executor.scheduleAtFixedRate(this::captureAndDetect, 0, 33, TimeUnit.MILLISECONDS);
        } else {
            System.err.println("No webcam found!");
        }
    }

    private void captureAndDetect() {
        if (isPaused) {
            return;
        }
        if (webcam == null || !webcam.isOpen()) {
            return;
        }

        final BufferedImage bufferedImage = webcam.getImage();
        if (bufferedImage == null) {
            return;
        }

        final Mat frame = bufferedImageToMat(bufferedImage);

        faceDetection.detectFaces(frame);

        final BufferedImage outputImage = matToBufferedImage(frame);

        Platform.runLater(() -> imageView.setImage(SwingFXUtils.toFXImage(outputImage, null)));
    }

    private Mat bufferedImageToMat(final BufferedImage image) {
        final Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        final byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    @Override
    public BufferedImage matToBufferedImage(Mat mat) {
        final int width = mat.width();
        final int height = mat.height();
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.get(0, 0, data);
        return image;
    };

    @Override
    public void stop() {
        executor.shutdown();
        if (webcam != null) webcam.close();
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }
}
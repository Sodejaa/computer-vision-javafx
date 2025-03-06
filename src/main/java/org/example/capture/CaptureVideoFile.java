package org.example.capture;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import org.example.FaceDetection;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CaptureVideoFile implements CaptureSource {
    private final ImageView imageView;
    private final String videoPath;
    private VideoCapture videoCapture;
    private final FaceDetection faceDetection;
    private ScheduledExecutorService executor;
    private boolean isPaused = false;
    private double pausedFramePosition = 0;

    public CaptureVideoFile(final ImageView imageView, String videoPath) {
        this.imageView = imageView;
        this.videoPath = videoPath;
        this.faceDetection = new FaceDetection();
    }

    @Override
    public void start() {
        if (videoCapture == null) {
            videoCapture = new VideoCapture(videoPath);
        }

        if (!videoCapture.isOpened()) {
            System.err.println("Error: Cannot open video file " + videoPath);
            return;
        }

        if (isPaused) {
            videoCapture.set(Videoio.CAP_PROP_POS_FRAMES, pausedFramePosition);
        }

        isPaused = false;
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::captureFrame, 0, 33, TimeUnit.MILLISECONDS);
    }

    private void captureFrame() {
        if (isPaused) return;

        final Mat frame = new Mat();
        if (!videoCapture.read(frame)) {
            stop();
            return;
        }

        faceDetection.detectFaces(frame);
        final BufferedImage outputImage = matToBufferedImage(frame);

        Platform.runLater(() -> imageView.setImage(SwingFXUtils.toFXImage(outputImage, null)));
    }

    public void pause() {
        if (videoCapture != null) {
            isPaused = true;
            pausedFramePosition = videoCapture.get(Videoio.CAP_PROP_POS_FRAMES);
        }
        stopExecutor();
    }

    public void resume() {
        isPaused = false;
        start();
    }

    @Override
    public void stop() {
        stopExecutor();
        if (videoCapture != null) {
            videoCapture.release();
            videoCapture = null;
        }
    }

    private void stopExecutor() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    @Override
    public BufferedImage matToBufferedImage(Mat mat) {
        final int width = mat.width();
        final int height = mat.height();
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.get(0, 0, data);
        return image;
    }
}
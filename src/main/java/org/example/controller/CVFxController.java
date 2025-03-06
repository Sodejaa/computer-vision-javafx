package org.example.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.ui.VideoControls;
import org.example.capture.CaptureSource;
import org.example.capture.CaptureVideoFile;
import org.example.capture.CaptureWebcam;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class CVFxController {
    private final Stage primaryStage;
    private CaptureSource captureSource;
    private boolean usingWebcam = true;
    private boolean isRunning;
    private final ImageView imageView = new ImageView();

    public CVFxController(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initializeUI() {
        final VideoControls controls = new VideoControls();
        controls.getToggleButton().setOnAction(e -> switchCapture(controls));
        controls.getPlayPauseButton().setOnAction(e -> togglePlayPause(controls));

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(640);
        imageView.setFitHeight(480);

        // Start with webcam as default
        captureSource = new CaptureWebcam(imageView);
        captureSource.start();

        isRunning = true;

        controls.getPlayPauseButton().setDisable(true);

        final VBox root = new VBox(10, controls.getControls(), new StackPane(imageView));
        final Scene scene = new Scene(root, 640, 480);

        primaryStage.setTitle("Computer Vision FX");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            if (captureSource != null) captureSource.stop();
        });
        primaryStage.show();
    }

    private void togglePlayPause(final VideoControls controls) {
        if (captureSource instanceof CaptureVideoFile) {
            CaptureVideoFile videoFile = (CaptureVideoFile) captureSource;

            if (isRunning) {
                videoFile.pause();
                controls.getPlayPauseButton().setText("Play");
            } else {
                videoFile.resume();
                controls.getPlayPauseButton().setText("Pause");
            }

            controls.getPlayPauseButton().setDisable(false);
        } else {
            controls.getPlayPauseButton().setDisable(true);
        }

        isRunning = !isRunning;
    }

    private void switchCapture(final VideoControls controls) {
        if (captureSource != null) captureSource.stop();

        if (usingWebcam) {
            captureSource = new CaptureVideoFile(imageView, "src/main/resources/steve-jobs-explaining-apps.mp4");
            controls.getToggleButton().setText("Switch to Webcam");
            adjustWindowSize("src/main/resources/steve-jobs-explaining-apps.mp4");
            controls.getPlayPauseButton().setDisable(false);
        } else {
            captureSource = new CaptureWebcam(imageView);
            controls.getToggleButton().setText("Switch to Video");

            // Reset window size for webcam
            Platform.runLater(() -> {
                primaryStage.setWidth(640);
                primaryStage.setHeight(480);
            });

            controls.getPlayPauseButton().setDisable(true);
        }

        usingWebcam = !usingWebcam;
        captureSource.start();
        isRunning = true;
    }

    private void adjustWindowSize(String videoPath) {
        VideoCapture tempCapture = new VideoCapture(videoPath);
        if (tempCapture.isOpened()) {
            int width = (int) tempCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
            int height = (int) tempCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
            tempCapture.release();

            Platform.runLater(() -> {
                primaryStage.setWidth(width);
                primaryStage.setHeight(height);
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
            });
        }
    }
}
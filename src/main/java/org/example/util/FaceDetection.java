package org.example;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.net.URL;

public class FaceDetection {
    private final CascadeClassifier faceDetector;

    public FaceDetection() {
        // Load Haar cascade for frontal face detection
        final URL cascadeURL = getClass().getResource("/haarcascade_frontalface_alt.xml");
        if (cascadeURL == null) {
            throw new RuntimeException("Haar cascade file not found!");
        }
        this.faceDetector = new CascadeClassifier(new File(cascadeURL.getPath()).getAbsolutePath());

        if (this.faceDetector.empty()) {
            throw new RuntimeException("Haar cascade could not be loaded!");
        }
    }

    // Detect faces in a given OpenCV Mat frame
    public void detectFaces(final Mat frame) {
        final Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        final MatOfRect faces = new MatOfRect();
        faceDetector.detectMultiScale(grayFrame, faces);

        final String title = "Frontal Face";

        for (final Rect rect : faces.toArray()) {
            Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 3);

            final Point titlePosition = new Point(rect.x, rect.y - 10);
            Imgproc.putText(frame, title, titlePosition, Imgproc.FONT_HERSHEY_SIMPLEX, 0.6, new Scalar(0, 255, 0), 2);
        }
    }
}

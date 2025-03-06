package org.example.capture;

import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
public interface CaptureSource {

    void start();

    BufferedImage matToBufferedImage(final Mat mat);

    void stop();

    void pause();

    void resume();
}
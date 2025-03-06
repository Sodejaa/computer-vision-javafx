package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import nu.pattern.OpenCV;
import org.example.controller.CVFxController;

public class CVFxApp extends Application {

    static {
        OpenCV.loadLocally(); // Ensure OpenCV is loaded only once
    }

    @Override
    public void start(Stage primaryStage) {
        final CVFxController cvFxController= new CVFxController(primaryStage);
        cvFxController.initializeUI();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
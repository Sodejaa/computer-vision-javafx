package org.example.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class VideoControls {
    private final Button toggleButton = new Button("Switch Input");
    private final Button playPauseButton = new Button("Play/Pause");


    public HBox getControls() {
        return new HBox(10, toggleButton, playPauseButton);
    }

    public Button getToggleButton() {
        return toggleButton;
    }

    public Button getPlayPauseButton() {
        return playPauseButton;
    }
}
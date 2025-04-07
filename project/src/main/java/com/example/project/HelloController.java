package com.example.project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    @FXML
    private Button btnAffiche;
    @FXML
    private HBox optionBar;

    @FXML
    public void initialize() {
        optionBar.setVisible(false);
        optionBar.setManaged(false);

        btnAffiche.setOnAction(event -> {

            System.out.println("Button clicked");


            boolean currentlyVisible = optionBar.isVisible();
            optionBar.setVisible(!currentlyVisible);
            optionBar.setManaged(!currentlyVisible);
        });
    }
}
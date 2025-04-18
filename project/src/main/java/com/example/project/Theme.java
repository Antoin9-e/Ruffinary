package com.example.project;

import javafx.scene.Scene;

public class Theme {
    public void setLightMode(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(String.valueOf(getClass().getResource("style.css")));

        System.out.println("Light mode activated");
    }

    public void setDarkMode(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(String.valueOf(getClass().getResource("darkMode.css")));

        System.out.println("Dark mode activated");
    }
}

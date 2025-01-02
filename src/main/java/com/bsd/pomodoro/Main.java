package com.bsd.pomodoro;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/main.css")).toExternalForm());
        stage.setTitle("Learning Sessions");
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
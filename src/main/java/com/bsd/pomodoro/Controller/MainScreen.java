package com.bsd.pomodoro.Controller;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class MainScreen implements Initializable {

    @FXML
    private Button startButton;
    @FXML
    private Label lblTimer;
    private PauseTransition timer = new PauseTransition(Duration.minutes(1));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTimer.textProperty().bind(timeLeft(timer));
        timer.setOnFinished(e ->{
            System.out.println("Timer finished");
            timer.stop();
            timer.jumpTo(Duration.ZERO);
        });

    }


    @FXML
    void OnActionStart(ActionEvent event) {
        timer.play();
        System.out.println("Timer Started.");
    }

    public void onActionPause(ActionEvent actionEvent) {
        timer.pause();
        System.out.println("Timer Paused.");
    }
    public void onActionStop(ActionEvent actionEvent) {
        timer.stop();
        System.out.println("Timer Stopped.");
    }

    private StringBinding timeLeft(Animation animation){
        return Bindings.createStringBinding(
                ()->{
                    double currentTime = animation.getCurrentTime().toMillis();
                    double totalTime = animation.getCycleDuration().toMillis();
                    long remainingTime = Math.round(totalTime - currentTime);
                    java.time.Duration duration = java.time.Duration.ofMillis(remainingTime);
                    return String.format("%d:%02d:%03d", duration.toMinutes(), duration.toSecondsPart(), duration.toMillisPart());
                },
                animation.currentTimeProperty(),
                animation.cycleDurationProperty());
    }
}

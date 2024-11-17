package com.bsd.pomodoro.Controller;

import com.bsd.pomodoro.Model.ActivityState;
import com.bsd.pomodoro.Pomodoro;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainScreen implements Initializable {

    @FXML
    private Button startButton;
    @FXML
    private Label lblTimer;
    @FXML
    private Label lblActivityState;
    private PauseTransition focusTimer = new PauseTransition(Duration.seconds(SettingsScreenController.getFocusTime()));
    private PauseTransition breakTimer = new PauseTransition(Duration.seconds(5));
    private ActivityState state;
    @FXML
    private Button settingsButton;




//image view is a node used for painting images loaded with images
    //image = photograph
    //imageView = picture frame

    Image img  = new Image("/settingsIcon.png");
    ImageView settingImage = new ImageView(img);



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        settingsButton.setGraphic(settingImage);

        state = ActivityState.valueOf("FOCUS");
        lblTimer.textProperty().bind(timeLeft(focusTimer));


        focusTimer.setOnFinished(e ->{
            System.out.println("Focus Timer finished");
            focusTimer.stop();
            breakTimer.jumpTo(Duration.ZERO);
            state = ActivityState.valueOf("BREAK");
            lblTimer.textProperty().bind(timeLeft(breakTimer));
            lblActivityState.setText(state.toString());
        });
        breakTimer.setOnFinished(e ->{
            System.out.println("Break Timer finished.");
            breakTimer.stop();
            focusTimer.jumpTo(Duration.ZERO);
            state = ActivityState.valueOf("FOCUS");
            lblTimer.textProperty().bind(timeLeft(focusTimer));
            lblActivityState.setText(state.toString());
        });

    }


    @FXML
    void OnActionStart(ActionEvent event) {
        if(state.toString().equalsIgnoreCase("focus")){
            focusTimer.play();
            System.out.println("focus Timer Started.");
            state = ActivityState.valueOf("FOCUS");
            lblActivityState.setText(state.toString());

        }
        if(state.toString().equalsIgnoreCase("break")){
            breakTimer.play();
            System.out.println("break Timer Started.");
            state = ActivityState.valueOf("SHORT_BREAK");
            lblTimer.textProperty().bind(timeLeft(breakTimer));
            lblActivityState.setText(state.toString());

        }


    }

    public void onActionPause(ActionEvent actionEvent) {
        focusTimer.pause();
        System.out.println("Timer Paused.");
    }
    public void onActionStop(ActionEvent actionEvent) {
        focusTimer.stop();
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


    @FXML
    void onActionSettings(ActionEvent event) throws IOException {

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Pomodoro.class.getResource("settings.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Settings");
            stage.setScene(scene);
            stage.show();




    }
}

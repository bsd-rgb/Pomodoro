package com.bsd.pomodoro.Controller;

import com.bsd.pomodoro.Helper.PropertiesUtil;
import com.bsd.pomodoro.Model.ActivityState;
import com.bsd.pomodoro.Main;
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
    @FXML
    private Button settingsButton;
    private PauseTransition focusTimer = new PauseTransition(Duration.seconds(Integer.parseInt(PropertiesUtil.getFocusPreference())));
    private PauseTransition shortBreakTimer = new PauseTransition(Duration.seconds(Integer.parseInt(PropertiesUtil.getShortPreference())));
    private PauseTransition longBreakTimer = new PauseTransition(Duration.seconds(Integer.parseInt(PropertiesUtil.getLongPreference())));
    private ActivityState state;

//image view is a node used for painting images loaded with images
    //image = photograph
    //imageView = picture frame

    Image img  = new Image("/settingsIcon.png");
    ImageView settingImage = new ImageView(img);



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        settingsButton.setGraphic(settingImage);

        state = ActivityState.valueOf("FOCUS");
        lblActivityState.setText(state.toString());
        lblTimer.textProperty().bind(timeLeft(focusTimer));


        focusTimer.setOnFinished(e ->{
            System.out.println("Focus Timer finished");
            focusTimer.stop();

            SettingsController.setInterval(SettingsController.getInterval() - 1);


            if(SettingsController.getInterval() == 0){
                state = ActivityState.valueOf("LONG_BREAK");
                longBreakTimer.jumpTo(Duration.ZERO);
                lblTimer.textProperty().bind(timeLeft(longBreakTimer));
                lblActivityState.setText(state.toString());
                SettingsController.resetInterval();

            }else{
                state = ActivityState.valueOf("SHORT_BREAK");
                shortBreakTimer.jumpTo(Duration.ZERO);
                lblTimer.textProperty().bind(timeLeft(shortBreakTimer));
                lblActivityState.setText(state.toString());
            }



        });
        shortBreakTimer.setOnFinished(e ->{
            System.out.println("Short Break Timer finished.");
            shortBreakTimer.stop();
            focusTimer.jumpTo(Duration.ZERO);
            state = ActivityState.valueOf("FOCUS");
            lblTimer.textProperty().bind(timeLeft(focusTimer));
            lblActivityState.setText(state.toString());
        });

        longBreakTimer.setOnFinished(e ->{
            System.out.println("Long break timer finished.");
            longBreakTimer.stop();
            focusTimer.jumpTo(Duration.ZERO);
            state = ActivityState.valueOf("FOCUS");
            lblTimer.textProperty().bind(timeLeft(focusTimer));
            lblActivityState.setText(state.toString());
        });

    }


    @FXML
    void OnActionStart(ActionEvent event) {
        if(state.toString().equalsIgnoreCase("FOCUS")){
            focusTimer.play();
            System.out.println("focus Timer Started.");
            state = ActivityState.valueOf("FOCUS");
            lblActivityState.setText(state.toString());
            System.out.println("Current Interval: " + SettingsController.getInterval());

        }
        if(state.toString().contains("BREAK")){
            if(state.toString().equalsIgnoreCase("long_break")){
                longBreakTimer.play();
                System.out.println("long break Timer Started.");
                state = ActivityState.valueOf("LONG_BREAK");
                lblTimer.textProperty().bind(timeLeft(longBreakTimer));
            }else{
                shortBreakTimer.play();
                System.out.println("short break Timer Started.");
                state = ActivityState.valueOf("SHORT_BREAK");
                lblTimer.textProperty().bind(timeLeft(shortBreakTimer));
            }
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
                    return String.format("%d:%02d", duration.toMinutes(), duration.toSecondsPart());
                },
                animation.currentTimeProperty(),
                animation.cycleDurationProperty());
    }


    @FXML
    void onActionSettings(ActionEvent event) throws IOException {

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("settings.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Settings");
            stage.setScene(scene);
            stage.show();
    }
}

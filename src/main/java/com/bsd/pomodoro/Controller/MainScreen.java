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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.sound.sampled.*;

public class MainScreen implements Initializable {
    @FXML
    public Button resetButton;
    @FXML
    public Button pauseButton;
    @FXML
    private Button startButton;
    @FXML
    private Label lblTimer;
    @FXML
    private Label lblActivityState;
    @FXML
    private Button settingsButton;
    @FXML
    private Button skipButton;
    @FXML
    GridPane gridPane;
    private PauseTransition focusTimer = new PauseTransition(Duration.seconds(Integer.parseInt(PropertiesUtil.getFocusPreference())));
    private PauseTransition shortBreakTimer = new PauseTransition(Duration.seconds(Integer.parseInt(PropertiesUtil.getShortPreference())));
    private PauseTransition longBreakTimer = new PauseTransition(Duration.seconds(Integer.parseInt(PropertiesUtil.getLongPreference())));
    private ActivityState state;
    private int intervalCounter = Integer.parseInt(PropertiesUtil.getInterval());
    File timerFinishedSound = new File("src/main/resources/audio/timerFinish.wav");
    Image settingImg = new Image(String.valueOf(getClass().getResource("/images/settingsIcon.png")));
    Image skipImg = new Image(String.valueOf(getClass().getResource("/images/skip.png")));
    ImageView settingImage = new ImageView(settingImg);
    ImageView skipImage = new ImageView(skipImg);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        settingsButton.setGraphic(settingImage);
        skipButton.setGraphic(skipImage);
        state = ActivityState.valueOf("FOCUS");
        addAttemptStyle(state);
        lblActivityState.setText(state.toString());
        lblTimer.textProperty().bind(timeLeft(focusTimer));

        lblActivityState.getStyleClass().add("default");
        lblTimer.getStyleClass().add("default");
        startButton.getStyleClass().add("button1");


        focusTimer.setOnFinished(e ->{
            System.out.println("Focus Timer finished");
            playSound();
            focusTimer.stop();
            intervalCounter--;
            checkInterval();
            addAttemptStyle(state);
        });
        shortBreakTimer.setOnFinished(e ->{
            System.out.println("Short Break Timer finished.");
            playSound();

            shortBreakTimer.stop();
            focusTimer.jumpTo(Duration.ZERO);
            state = ActivityState.valueOf("FOCUS") ;
            lblTimer.textProperty().bind(timeLeft(focusTimer));
            addAttemptStyle(state);
            lblActivityState.setText(state.toString());
        });

        longBreakTimer.setOnFinished(e ->{
            System.out.println("Long break timer finished.");
            playSound();
            longBreakTimer.stop();
            focusTimer.jumpTo(Duration.ZERO);
            state = ActivityState.valueOf("FOCUS");
            lblTimer.textProperty().bind(timeLeft(focusTimer));
            addAttemptStyle(state);
            lblActivityState.setText(state.toString());
        });
    }


    @FXML
    void OnActionStart(ActionEvent event) {

        if(state.toString().equalsIgnoreCase("FOCUS")) {
            focusTimer.play();
            System.out.println("Current Interval: " + intervalCounter);
            System.out.println("focus Timer Started.");
            setLabel();
        }

        if(state.toString().contains("Break")) {
            if (state.toString().equalsIgnoreCase("long break")) {
                longBreakTimer.play();
                System.out.println("long break Timer Started.");
            } else {
                shortBreakTimer.play();
                System.out.println(state.toString());
                System.out.println("short break Timer Started.");
            }
            setLabel();
        }

       /* if(state.toString().equalsIgnoreCase("FOCUS")){
            focusTimer.play();
            System.out.println("focus Timer Started.");
            state = ActivityState.valueOf("FOCUS");
            lblActivityState.setText(state.toString());
            System.out.println("Current Interval: " + intervalCounter*//*SettingsController.getInterval()*//*);

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
        }*/
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
        System.out.println("Interval number:" + PropertiesUtil.getInterval());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("settings.fxml"));
        loader.load();
        SettingsController settingsController = loader.getController();

        try{
            settingsController.sendSettings();
        }catch(Exception e){
            System.out.println("Error sending settings.");
            System.out.println(e.getMessage());
        }

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    private void playSound() {
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(timerFinishedSound);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }catch(IOException | UnsupportedAudioFileException | LineUnavailableException ex ){
            System.out.println(ex.getMessage());
        }
    }

    public void onActionSkip(ActionEvent event) {
        if(state.toString().equalsIgnoreCase("FOCUS")){
            if(focusTimer.getCurrentTime().toSeconds() > 0){
                focusTimer.stop();
            }
            intervalCounter--;
            System.out.println("Current Interval(after subtraction): " + intervalCounter);
            checkInterval();
        }else if(state.toString().contains("Break")) {
            if(shortBreakTimer.getCurrentTime().toSeconds() > 0 || longBreakTimer.getCurrentTime().toSeconds() > 0){
                shortBreakTimer.stop();
                longBreakTimer.stop();
            }
            focusTimer.jumpTo(Duration.ZERO);
            lblTimer.textProperty().bind(timeLeft(focusTimer));
            state = ActivityState.valueOf("FOCUS");
            setLabel();
        }
    }

    private void setLabel(){
        if(state.toString().equalsIgnoreCase("FOCUS")){
            state = ActivityState.valueOf("FOCUS");
            lblActivityState.setText(state.toString());
        }
        if(state.toString().contains("Break")){
            if(state.toString().equalsIgnoreCase("long break")){
                state = ActivityState.valueOf("LONG_BREAK");
                lblTimer.textProperty().bind(timeLeft(longBreakTimer));
            }else{
                state = ActivityState.valueOf("SHORT_BREAK");
                lblTimer.textProperty().bind(timeLeft(shortBreakTimer));
            }
            lblActivityState.setText(state.toString());
        }
    }

    private void checkInterval(){
        if(intervalCounter == 0){
            state = ActivityState.valueOf("LONG_BREAK");
            longBreakTimer.jumpTo(Duration.ZERO);
            lblTimer.textProperty().bind(timeLeft(longBreakTimer));
            lblActivityState.setText(state.toString());
            intervalCounter = Integer.parseInt(PropertiesUtil.getInterval());
        }else{
            state = ActivityState.valueOf("SHORT_BREAK");
            shortBreakTimer.jumpTo(Duration.ZERO);
            lblTimer.textProperty().bind(timeLeft(shortBreakTimer));
            lblActivityState.setText(state.toString());
        }

    }

    private void addAttemptStyle(ActivityState state) {
        String lowerCase = state.toString().replaceAll("\\s", "").toLowerCase();
        gridPane.getStyleClass().clear();
        gridPane.getStyleClass().addAll("root", lowerCase);
        //gridPane.getStyleClass().add(lowerCase);
    }
}


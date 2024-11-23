package com.bsd.pomodoro.Controller;

import com.bsd.pomodoro.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    private final ObservableList<Integer> pomodoroLength = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60);
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<Integer> focusCombo;
    @FXML
    private ComboBox<Integer> longBreakCombo;
    @FXML
    private ComboBox<Integer> shortBreakCombo;
    @FXML
    private static TextField longBreakInterval;
    private static  int defaultInterval = 3;
    private static final int defaultFocusTime = 10;
    private static final int defaultShortBreak = 5;
    private static final int defaultLongBreak = 15;
    private static int interval = defaultInterval;
    private static int focusTime = defaultFocusTime;
    private static int shortBreakTime = defaultShortBreak;
    private static int longBreakTime = defaultLongBreak;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        focusCombo.setItems(pomodoroLength);
        shortBreakCombo.setItems(pomodoroLength);
        longBreakCombo.setItems(pomodoroLength);
        focusCombo.setValue(focusTime);
        shortBreakCombo.setValue(shortBreakTime);
        longBreakCombo.setValue(longBreakTime);
        //longBreakInterval.setText("h");

       setComboStringFormat(focusCombo);
        setComboStringFormat(shortBreakCombo);
        setComboStringFormat(longBreakCombo);
    }

    private void setComboStringFormat(ComboBox<Integer> comboBox){
        comboBox.setConverter(new StringConverter<Integer>() {

            @Override
            public String toString(Integer integer) {
                if(integer == 1){
                    return integer + " Minute";
                }
                return integer + " Minutes";
            }

            @Override
            public Integer fromString(String s) {
                return Integer.valueOf(s);

            }
        });

    }

    public static int getFocusTime(){
        return focusTime;
    }

    public static void setFocusTime(int setTime){
        focusTime = setTime;
    }

    public static int getShortBreakTime(){
        return shortBreakTime;
    }

    public static void setShortBreakTime(int setTime){
        shortBreakTime = setTime;
    }

    public static int getLongBreakTime(){
        return longBreakTime;
    }

    public static void setLongBreakTime(int setTime){
        longBreakTime = setTime;
    }

    public static int getInterval(){
        return interval;
    }

    public static void setInterval(int intervalNum){
        interval = intervalNum;
    }

    public  static void resetInterval(){
        System.out.println("Interval Reset!");
        setInterval(defaultInterval);

    }

    public void onActionSave(ActionEvent event) throws IOException {
        setFocusTime(focusCombo.getValue());
        focusCombo.setValue(focusTime);

        setShortBreakTime(shortBreakCombo.getValue());
        shortBreakCombo.setValue(shortBreakTime);

        setLongBreakTime(longBreakCombo.getValue());
        longBreakCombo.setValue(longBreakTime);

        setInterval(Integer.parseInt(longBreakInterval.getText()));
        defaultInterval = Integer.parseInt(longBreakInterval.getText());
        longBreakInterval.setText(String.valueOf(defaultInterval));

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
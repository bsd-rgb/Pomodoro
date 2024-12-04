package com.bsd.pomodoro.Helper;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class PropertiesUtil {

    private static Preferences preferences = Preferences.userRoot();

    public static void setPreferences(int time, String type){

        switch(type.toLowerCase()){
            case "focus" -> {
                preferences.put("focusTime", String.valueOf(time));
            }
            case "short_break" ->{
                preferences.put("shortBreak",String.valueOf(time));
            }
            case "long_break" -> {
                preferences.put("longBreak",String.valueOf(time));
            }
            default -> System.out.println("Time type not found");
        };

    }

    public static void clearPreferences() throws BackingStoreException {
        preferences.clear();
    }

    public static String getFocusPreference(){
        return preferences.get("focusTime", "15");
    }

    public static String getShortPreference(){
        return preferences.get("shortBreak", "5");
    }

    public static String getLongPreference(){
        return preferences.get("longBreak", "20");
    }






}

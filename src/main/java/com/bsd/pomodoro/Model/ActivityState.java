package com.bsd.pomodoro.Model;

public enum ActivityState {

    //TODO: Figure out how to change these strings successfully
    FOCUS("Focus"), SHORT_BREAK("Short Break"), LONG_BREAK("Long Break");

    private final String text;
     ActivityState(String text){
        this.text = text;
    }

    @Override
    public String toString(){
         return text;
    }
}

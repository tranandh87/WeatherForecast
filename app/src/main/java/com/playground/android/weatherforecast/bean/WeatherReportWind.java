package com.playground.android.weatherforecast.bean;

import com.google.gson.annotations.SerializedName;


public class WeatherReportWind {

    private static final String FIELD_DEG = "deg";
    private static final String FIELD_SPEED = "speed";


    @SerializedName(FIELD_DEG)
    private double mDeg;
    @SerializedName(FIELD_SPEED)
    private double mSpeed;


    public WeatherReportWind(){

    }

    public void setDeg(double deg) {
        mDeg = deg;
    }

    public double getDeg() {
        return mDeg;
    }

    public void setSpeed(double speed) {
        mSpeed = speed;
    }

    public double getSpeed() {
        return mSpeed;
    }

    @Override
    public String toString(){
        return "deg = " + mDeg + ", speed = " + mSpeed;
    }


}
package com.playground.android.weatherforecast.bean;

import com.google.gson.annotations.SerializedName;


public class WeatherReportSys {

    private static final String FIELD_SUNSET = "sunset";
    private static final String FIELD_COUNTRY = "country";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_SUNRISE = "sunrise";


    @SerializedName(FIELD_SUNSET)
    private int mSunset;
    @SerializedName(FIELD_COUNTRY)
    private String mCountry;
    @SerializedName(FIELD_MESSAGE)
    private double mMessage;
    @SerializedName(FIELD_SUNRISE)
    private int mSunrise;


    public WeatherReportSys(){

    }

    public void setSunset(int sunset) {
        mSunset = sunset;
    }

    public int getSunset() {
        return mSunset;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setMessage(double message) {
        mMessage = message;
    }

    public double getMessage() {
        return mMessage;
    }

    public void setSunrise(int sunrise) {
        mSunrise = sunrise;
    }

    public int getSunrise() {
        return mSunrise;
    }

    @Override
    public String toString(){
        return "sunset = " + mSunset + ", country = " + mCountry + ", message = " + mMessage + ", sunrise = " + mSunrise;
    }


}
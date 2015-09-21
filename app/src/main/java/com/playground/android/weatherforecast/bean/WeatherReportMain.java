package com.playground.android.weatherforecast.bean;

import com.google.gson.annotations.SerializedName;


public class WeatherReportMain {

    private static final String FIELD_TEMP = "temp";
    private static final String FIELD_TEMP_MAX = "temp_max";
    private static final String FIELD_PRESSURE = "pressure";
    private static final String FIELD_HUMIDITY = "humidity";
    private static final String FIELD_SEA_LEVEL = "sea_level";
    private static final String FIELD_TEMP_MIN = "temp_min";
    private static final String FIELD_GRND_LEVEL = "grnd_level";


    @SerializedName(FIELD_TEMP)
    private double mTemp;
    @SerializedName(FIELD_TEMP_MAX)
    private double mTempMax;
    @SerializedName(FIELD_PRESSURE)
    private double mPressure;
    @SerializedName(FIELD_HUMIDITY)
    private int mHumidity;
    @SerializedName(FIELD_SEA_LEVEL)
    private double mSeaLevel;
    @SerializedName(FIELD_TEMP_MIN)
    private double mTempMin;
    @SerializedName(FIELD_GRND_LEVEL)
    private double mGrndLevel;


    public WeatherReportMain(){

    }

    public void setTemp(double temp) {
        mTemp = temp;
    }

    public double getTemp() {
        return mTemp;
    }

    public void setTempMax(double tempMax) {
        mTempMax = tempMax;
    }

    public double getTempMax() {
        return mTempMax;
    }

    public void setPressure(double pressure) {
        mPressure = pressure;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setSeaLevel(double seaLevel) {
        mSeaLevel = seaLevel;
    }

    public double getSeaLevel() {
        return mSeaLevel;
    }

    public void setTempMin(double tempMin) {
        mTempMin = tempMin;
    }

    public double getTempMin() {
        return mTempMin;
    }

    public void setGrndLevel(double grndLevel) {
        mGrndLevel = grndLevel;
    }

    public double getGrndLevel() {
        return mGrndLevel;
    }

    @Override
    public String toString(){
        return "temp = " + mTemp + ", tempMax = " + mTempMax + ", pressure = " + mPressure + ", humidity = " + mHumidity + ", seaLevel = " + mSeaLevel + ", tempMin = " + mTempMin + ", grndLevel = " + mGrndLevel;
    }


}
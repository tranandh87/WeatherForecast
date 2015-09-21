package com.playground.android.weatherforecast.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WeatherList {

    private static final String FIELD_DT_TXT = "dt_txt";
    private static final String FIELD_WIND = "wind";
    private static final String FIELD_RAIN = "rain";
    private static final String FIELD_SYS = "sys";
    private static final String FIELD_DT = "dt";
    private static final String FIELD_WEATHER = "weather";
    private static final String FIELD_CLOUDS = "clouds";
    private static final String FIELD_MAIN = "main";


    @SerializedName(FIELD_DT_TXT)
    private String mDtTxt;
    @SerializedName(FIELD_WIND)
    private WeatherReportWind mWind;
    @SerializedName(FIELD_RAIN)
    private Rain mRain;
    @SerializedName(FIELD_SYS)
    private WeatherReportSys mSy;
    @SerializedName(FIELD_DT)
    private int mDt;
    @SerializedName(FIELD_WEATHER)
    private List<WeatherReportWeather> mWeathers;
    @SerializedName(FIELD_CLOUDS)
    private Cloud mCloud;
    @SerializedName(FIELD_MAIN)
    private WeatherReportMain mMain;


    public WeatherList(){

    }

    public void setDtTxt(String dtTxt) {
        mDtTxt = dtTxt;
    }

    public String getDtTxt() {
        return mDtTxt;
    }

    public void setWind(WeatherReportWind wind) {
        mWind = wind;
    }

    public WeatherReportWind getWind() {
        return mWind;
    }

    public void setRain(Rain rain) {
        mRain = rain;
    }

    public Rain getRain() {
        return mRain;
    }

    public void setSy(WeatherReportSys sy) {
        mSy = sy;
    }

    public WeatherReportSys getSy() {
        return mSy;
    }

    public void setDt(int dt) {
        mDt = dt;
    }

    public int getDt() {
        return mDt;
    }

    public void setWeathers(List<WeatherReportWeather> weathers) {
        mWeathers = weathers;
    }

    public List<WeatherReportWeather> getWeathers() {
        return mWeathers;
    }

    public void setCloud(Cloud cloud) {
        mCloud = cloud;
    }

    public Cloud getCloud() {
        return mCloud;
    }

    public void setMain(WeatherReportMain main) {
        mMain = main;
    }

    public WeatherReportMain getMain() {
        return mMain;
    }

    @Override
    public String toString(){
        return "dtTxt = " + mDtTxt + ", wind = " + mWind + ", rain = " + mRain + ", sy = " + mSy + ", dt = " + mDt + ", weathers = " + mWeathers + ", cloud = " + mCloud + ", main = " + mMain;
    }


}
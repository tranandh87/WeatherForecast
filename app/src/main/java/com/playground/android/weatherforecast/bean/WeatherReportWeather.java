package com.playground.android.weatherforecast.bean;

import com.google.gson.annotations.SerializedName;


public class WeatherReportWeather {

    private static final String FIELD_ID = "id";
    private static final String FIELD_ICON = "icon";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_MAIN = "main";


    @SerializedName(FIELD_ID)
    private long mId;
    @SerializedName(FIELD_ICON)
    private String mIcon;
    @SerializedName(FIELD_DESCRIPTION)
    private String mDescription;
    @SerializedName(FIELD_MAIN)
    private String mMain;


    public WeatherReportWeather(){

    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setMain(String main) {
        mMain = main;
    }

    public String getMain() {
        return mMain;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof WeatherReportWeather){
            return ((WeatherReportWeather) obj).getId() == mId;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return ((Long)mId).hashCode();
    }

    @Override
    public String toString(){
        return "id = " + mId + ", icon = " + mIcon + ", description = " + mDescription + ", main = " + mMain;
    }


}
package com.playground.android.weatherforecast.bean;

import android.graphics.Bitmap;

/**
 * Created by anarajendran on 9/21/15.
 */
public class WeatherForecastReport {

    private String date;
    private String temperature;
    private Bitmap weaterIcon;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Bitmap getWeaterIcon() {
        return weaterIcon;
    }

    public void setWeaterIcon(Bitmap weaterIcon) {
        this.weaterIcon = weaterIcon;
    }
}

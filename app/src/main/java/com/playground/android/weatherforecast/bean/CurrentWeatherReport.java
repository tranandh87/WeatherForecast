package com.playground.android.weatherforecast.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by anarajendran on 9/20/15.
 */
public class CurrentWeatherReport {

    private String location;
    private String date;
    private String temperature;
    private int humidity;
    private int pressure;
    private double wind;
    private String description;
    private Bitmap weaterIcon;

    public Bitmap getWeaterIcon() {
        return weaterIcon;
    }

    public void setWeaterIcon(Bitmap weaterIcon) {
        this.weaterIcon = weaterIcon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }
}

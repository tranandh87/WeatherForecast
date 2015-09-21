package com.playground.android.weatherforecast.bean;

import com.google.gson.annotations.SerializedName;


public class Rain{

    private static final String FIELD_3H = "3h";


    @SerializedName(FIELD_3H)
    private double m3h;


    public Rain(){

    }

    public void set3h(double h3) {
        m3h = h3;
    }

    public double get3h() {
        return m3h;
    }

    @Override
    public String toString(){
        return "3h = " + m3h;
    }


}
package com.playground.android.weatherforecast.bean;

import com.google.gson.annotations.SerializedName;


public class Cloud{

    private static final String FIELD_ALL = "all";


    @SerializedName(FIELD_ALL)
    private int mAll;


    public Cloud(){

    }

    public void setAll(int all) {
        mAll = all;
    }

    public int getAll() {
        return mAll;
    }

    @Override
    public String toString(){
        return "all = " + mAll;
    }


}
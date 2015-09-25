package com.playground.android.weatherforecast.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anarajendran on 9/20/15.
 */
public class Util {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static String getCurrentDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

        Date date = new Date();
        return dateFormat.format(date);
    }
}

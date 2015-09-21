package com.playground.android.weatherforecast.networking;

import android.location.Location;
import android.net.Uri;
import android.net.Uri.Builder;

/**
 * Created by anarajendran on 9/7/15.
 */
public class WeatherMapQuery {

    private static final String END_POINT = "http://api.openweathermap.org/";

    private static final String WEATHER_DATA = "data/2.5/";
    private static final String WEATHER_ICON = "img/w/";

    private static final String CURRENT_WEATHER = "weather";
    private static final String FORECAST = "forecast";

    //    private static final String API_KEY = "#####";
    private static final String TYPE = "accurate";
    private static final String UNITS = "imperial";


    private static Builder getWeatherMapDataBaseUrl(String queryType){
        return Uri.parse(END_POINT + WEATHER_DATA + queryType)
                .buildUpon()
                .appendQueryParameter("type", TYPE)
                .appendQueryParameter("units", UNITS);
    }

    public static String getCurrentWeatherAPI(Location location){
        return buildLocationQuery(getWeatherMapDataBaseUrl(CURRENT_WEATHER), location);
    }

    public static String getCurrentWeatherAPI(String query){
        return buildSearchQuery(getWeatherMapDataBaseUrl(CURRENT_WEATHER), query);
    }

    public static String getWeatherForecastAPI(Location location){
        return buildLocationQuery(getWeatherMapDataBaseUrl(FORECAST), location);
    }

    public static String getWeatherForecastAPI(String query){
        return buildSearchQuery(getWeatherMapDataBaseUrl(FORECAST), query);
    }

    private static String buildLocationQuery(Builder builder,Location location){
        return builder
                .appendQueryParameter("lat",String.valueOf(location.getLatitude()))
                .appendQueryParameter("lon", String.valueOf(location.getLongitude()))
                .build().toString();
    }

    private static String buildSearchQuery(Builder builder,String query){
        return builder
                .appendQueryParameter("q", query)
                .build().toString();
    }

    public static String buildWeatherMapIconUrl(String iconId){
        return END_POINT + WEATHER_ICON + iconId + ".png";
    }
}

package com.playground.android.weatherforecast.networking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.playground.android.weatherforecast.bean.WeatherForecastReport;
import com.playground.android.weatherforecast.bean.WeatherReportWeather;
import com.playground.android.weatherforecast.bean.WeatherReportMain;
import com.playground.android.weatherforecast.bean.CurrentWeatherReport;
import com.playground.android.weatherforecast.bean.WeatherReportSys;
import com.playground.android.weatherforecast.bean.WeatherReportWind;
import com.playground.android.weatherforecast.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anarajendran on 9/19/15.
 */
public class WeatherMapFetcher {
    private static final String TAG = "WeatherMapFetcher";

    private static final String WEATHER_REPORT_MAIN = "main";
    private static final String WEATHER_REPORT_WIND = "wind";
    private static final String WEATHER_REPORT_LOCATION_NAME = "name";
    private static final String WEATHER_REPORT_SYS = "sys";
    private static final String WEATHER_REPORT_WEATHER = "weather";
    private static final String WEATHER_FORECAST_LIST = "list";
    private static final String WEATHER_FORECAST_DATE = "dt_txt";
    private static final String WEATHER_RESPONSE_CODE = "cod";
    private static final String WEATHER_ERROR_MESSAGE = "message";

    //Current Weather Methods

    public CurrentWeatherReport fetchCurrentWeatherReport(Location location, String query){
        String url;
        if(location != null){
            url = WeatherMapQuery.getCurrentWeatherAPI(location);
        } else if(query != null){
            url = WeatherMapQuery.getCurrentWeatherAPI(query);
        } else{
            return null;
        }

        JSONObject jsonBody = getJsonBody(url);

        try {
            if (jsonBody.getInt(WEATHER_RESPONSE_CODE) == 200) {
                return parseCurrentWeather(jsonBody);
            }
            else{
                logErrorMessage(jsonBody);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CurrentWeatherReport parseCurrentWeather(JSONObject jsonBody)
            throws IOException, JSONException {

        CurrentWeatherReport currentWeatherReport = new CurrentWeatherReport();

        //Getting value to set weather report
        WeatherReportMain main = getWeatherReportMain(jsonBody);
        WeatherReportWind wind = getWeatherReportWind(jsonBody);
        WeatherReportSys sys = getWeatherReportSys(jsonBody);
        WeatherReportWeather weather = getWeatherReportWeather(jsonBody);
        String locationName = jsonBody.getString(WEATHER_REPORT_LOCATION_NAME);

        //feching weather icon
        Bitmap bitMap= getWeatherIcon(weather.getIcon());

        //Setting values for weather report
        currentWeatherReport.setTemperature(String.valueOf(main.getTemp()));
        currentWeatherReport.setHumidity(main.getHumidity());
        currentWeatherReport.setPressure(Double.valueOf(main.getPressure()).intValue());
        currentWeatherReport.setWind(wind.getSpeed());

        currentWeatherReport.setLocation(locationName + ", " + sys.getCountry());
        currentWeatherReport.setDate(Util.getCurrentDateTime());
        currentWeatherReport.setDescription(weather.getDescription());

        currentWeatherReport.setWeaterIcon(bitMap);

        return currentWeatherReport;
    }

    //Weather forecast methods
    public List<WeatherForecastReport> fetchWeatherForecastReport(Location location, String query){

        String url;
        if(location != null){
            url = WeatherMapQuery.getWeatherForecastAPI(location);
        } else if(query != null){
            url = WeatherMapQuery.getWeatherForecastAPI(query);
        } else{
            return null;
        }
        JSONObject jsonBody = getJsonBody(url);

        try {
            if (jsonBody.getInt(WEATHER_RESPONSE_CODE) == 200) {
                return parseWeatherForecast(jsonBody);
            }
            else{
                logErrorMessage(jsonBody);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<WeatherForecastReport> parseWeatherForecast(JSONObject jsonBody)
            throws IOException, JSONException {

        List<WeatherForecastReport> weatherForecastReportList = new ArrayList<>();
        JSONArray weatherForecastObjectArray = jsonBody.getJSONArray(WEATHER_FORECAST_LIST);

        for(int counter=0;counter < weatherForecastObjectArray.length();counter++){

            if (counter % 8 == 0 && counter < 17) {
                WeatherForecastReport weatherForecastReport = new WeatherForecastReport();

                JSONObject weatherForecastObject = weatherForecastObjectArray.getJSONObject(counter);

                Log.i(TAG,"Weather Forecast Object: " + weatherForecastObject.toString());

                //Getting value to set weather forecast report
                WeatherReportMain main = getWeatherReportMain(weatherForecastObject);
                String date = weatherForecastObject.getString(WEATHER_FORECAST_DATE).split(" ")[0];
                WeatherReportWeather weather = getWeatherReportWeather(weatherForecastObject);

                //feching weather icon
                Bitmap bitMap = getWeatherIcon(weather.getIcon());

                //Setting values for weather forecast report
                weatherForecastReport.setWeaterIcon(bitMap);
                weatherForecastReport.setTemperature("Max: " + String.valueOf(main.getTemp()));
                weatherForecastReport.setDate(date);
                weatherForecastReportList.add(weatherForecastReport);
            }
        }

        return weatherForecastReportList;
    }

    private Bitmap getWeatherIcon(String icon) {
        byte[] bitMapBytes = new byte[0];
        try {
            bitMapBytes = getUrlBytes(WeatherMapQuery.buildWeatherMapIconUrl(icon));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  BitmapFactory
                .decodeByteArray(bitMapBytes, 0, bitMapBytes.length);
    }

    private WeatherReportMain getWeatherReportMain(JSONObject jsonBody) throws IOException, JSONException{
        JSONObject main = jsonBody.getJSONObject(WEATHER_REPORT_MAIN);

        Gson gson = new Gson();
        return gson.fromJson(main.toString(),WeatherReportMain.class);
    }

    private WeatherReportWind getWeatherReportWind(JSONObject jsonBody) throws IOException, JSONException{
        JSONObject wind = jsonBody.getJSONObject(WEATHER_REPORT_WIND);

        Gson gson = new Gson();
        return gson.fromJson(wind.toString(),WeatherReportWind.class);
    }

    private WeatherReportSys getWeatherReportSys(JSONObject jsonBody) throws IOException, JSONException{
        JSONObject sys = jsonBody.getJSONObject(WEATHER_REPORT_SYS);

        Gson gson = new Gson();
        return gson.fromJson(sys.toString(),WeatherReportSys.class);
    }

    private WeatherReportWeather getWeatherReportWeather(JSONObject jsonBody) throws IOException, JSONException{
        JSONObject weather = jsonBody.getJSONArray(WEATHER_REPORT_WEATHER).getJSONObject(0);

        Gson gson = new Gson();
        return gson.fromJson(weather.toString(),WeatherReportWeather.class);
    }

    //Common Methods

    public JSONObject getJsonBody(String url) {
        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);

            return new JSONObject(jsonString);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return null;
    }


    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }
        finally {
            connection.disconnect();
        }

    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private void logErrorMessage(JSONObject jsonBody) {
        try {
            Log.e(TAG, "Error Code: " + jsonBody.getInt(WEATHER_RESPONSE_CODE) + ". Message : " +  jsonBody.getString(WEATHER_ERROR_MESSAGE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

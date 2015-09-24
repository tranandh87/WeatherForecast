package com.playground.android.weatherforecast.networking;

import android.location.Location;

import com.playground.android.weatherforecast.bean.CurrentWeatherReport;
import com.playground.android.weatherforecast.bean.WeatherForecastReport;
import com.playground.android.weatherforecast.util.Util;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherMapFetcherTest extends TestCase {

    WeatherMapFetcher mWeatherMapFetcher;
    Location mockLocation;

    public void setUp() throws Exception {
        super.setUp();
        mWeatherMapFetcher = new WeatherMapFetcher();
        mockLocation = new Location("Mock");
        mockLocation.setLatitude(45.513904);
        mockLocation.setLongitude(-122.6799639);
    }

    public void tearDown() throws Exception {

    }

    public void testFetchCurrentWeatherReport() throws Exception {
        CurrentWeatherReport currentWeatherReport = mWeatherMapFetcher.fetchCurrentWeatherReport(null,"portland");
        Assert.assertNotNull(currentWeatherReport);
        Assert.assertNotNull(currentWeatherReport.getWeaterIcon());
        Assert.assertNotNull(currentWeatherReport.getTemperature());
        Assert.assertNotNull(currentWeatherReport.getDate());
        Assert.assertNotNull(currentWeatherReport.getDescription());
        Assert.assertNotNull(currentWeatherReport.getHumidity());
        Assert.assertTrue(currentWeatherReport.getLocation().contains("Portland"));
        Assert.assertNotNull(currentWeatherReport.getPressure());
        Assert.assertNotNull(currentWeatherReport.getWind());
    }

    public void testFetchCurrentWeatherReport1() throws Exception {

        CurrentWeatherReport currentWeatherReport = mWeatherMapFetcher.fetchCurrentWeatherReport(mockLocation,null);
        Assert.assertNotNull(currentWeatherReport);
        Assert.assertNotNull(currentWeatherReport.getWeaterIcon());
        Assert.assertNotNull(currentWeatherReport.getTemperature());
        Assert.assertNotNull(currentWeatherReport.getDate());
        Assert.assertNotNull(currentWeatherReport.getDescription());
        Assert.assertNotNull(currentWeatherReport.getHumidity());
        Assert.assertTrue(currentWeatherReport.getLocation().contains("Portland"));
        Assert.assertNotNull(currentWeatherReport.getPressure());
        Assert.assertNotNull(currentWeatherReport.getWind());
    }

    public void testFetchWeatherForecastReport() throws Exception {
        List<WeatherForecastReport> weatherForecastReport= mWeatherMapFetcher.fetchWeatherForecastReport(null,"chennai");

        Assert.assertNotNull(weatherForecastReport);
        for (WeatherForecastReport report : weatherForecastReport){
            Assert.assertNotNull(report.getDate());
            Assert.assertNotNull(report.getTemperature());
            Assert.assertNotNull(report.getWeaterIcon());
        }
    }

    public void testFetchWeatherForecastReport1() throws Exception {
        List<WeatherForecastReport> weatherForecastReport= mWeatherMapFetcher.fetchWeatherForecastReport(mockLocation,null);

        Assert.assertNotNull(weatherForecastReport);
        for (WeatherForecastReport report : weatherForecastReport){
            Assert.assertNotNull(report.getDate());
            Assert.assertNotNull(report.getTemperature());
            Assert.assertNotNull(report.getWeaterIcon());
        }
    }

    public void testParseWeatherForecast() throws Exception {

    }

    public void testGetJsonBody() throws Exception {

    }

    public void testGetUrlBytes() throws Exception {

    }

    public void testGetUrlString() throws Exception {

    }

    public void testUtilCurrentDateAndTime() throws Exception{
        Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()),Util.getCurrentDateTime());
    }
}
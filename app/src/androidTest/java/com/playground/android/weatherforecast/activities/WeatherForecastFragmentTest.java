package com.playground.android.weatherforecast.activities;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.playground.android.weatherforecast.R;

public class WeatherForecastFragmentTest extends ActivityInstrumentationTestCase2<WeatherActivity> {

    private WeatherActivity mWeatherActivity;
    private TextView mlocationTextView;

    public WeatherForecastFragmentTest() {
        super(WeatherActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mWeatherActivity = getActivity();
    }

    private Fragment startFragment(Fragment fragment) {
        FragmentTransaction transaction = mWeatherActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment, "tag");
        transaction.commit();
        getInstrumentation().waitForIdleSync();
        Fragment frag = mWeatherActivity.getSupportFragmentManager().findFragmentByTag("tag");
        mlocationTextView = (TextView) frag.getView().findViewById(R.id.location);
        return frag;
    }


    public void testFragment() {
        WeatherForecastFragment fragment = new WeatherForecastFragment();

        Fragment frag = startFragment(fragment);

        assertNotNull(frag);
        assertTrue(fragment.created);
        assertTrue(fragment.started);
        assertTrue(fragment.viewCreated);

        Log.i("Instrumentation Test", mlocationTextView.getText().toString());
    }
}
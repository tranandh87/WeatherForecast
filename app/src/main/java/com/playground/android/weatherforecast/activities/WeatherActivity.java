package com.playground.android.weatherforecast.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.playground.android.weatherforecast.R;


public class WeatherActivity extends SingleFragmentActivity {

    private static final String TAG = "WeatherActivity";
    private static final int REQUEST_ERROR = 0;

    @Override
    public Fragment createFragment(){
        return WeatherForecastFragment.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfGooglePlayServicesAvailable();
        checkIfGPSEnabled();
    }

    private void checkIfGooglePlayServicesAvailable() {
        final int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.goole_play_service_error_message)
                    .setTitle("Google Play Service Error")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i(TAG, "Google Play service is unavailable. Quitting app. Error Code: " + errorCode);
                            System.exit(0);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            Log.i(TAG, "Google Play service is available");
        }
    }

    private void checkIfGPSEnabled(){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.gps_enable_option_message)
                .setTitle("GPS is Disabled")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        System.exit(0);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

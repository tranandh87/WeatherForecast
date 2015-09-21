package com.playground.android.weatherforecast.activities;

import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.playground.android.weatherforecast.networking.WeatherMapFetcher;
import com.playground.android.weatherforecast.R;
import com.playground.android.weatherforecast.bean.WeatherForecastReport;
import com.playground.android.weatherforecast.bean.CurrentWeatherReport;

import java.util.List;

/**
 * Created by anarajendran on 9/19/15.
 */
public class WeatherForecastFragment extends Fragment {

    private static final String TAG = "WeatherForecastFragment";

    private TextView mLocationTextView;
    private TextView mDateTimeTextView;
    private TextView mTempView;
    private TextView mDescriptionTextView;
    private ImageView mWeatherIcon;
    private TextView mPressureView;
    private TextView mHumidityView;
    private TextView mWindView;

    private GoogleApiClient mClient;
    private Location mLocation = null;

    private CurrentWeatherReport mCurrentWeatherReport;
    private List<WeatherForecastReport> mWeatherForecastReports;
    private RecyclerView mWeatherForecastRecyclerView;

    private ProgressBar weatherForecastProgressBar;
    private ProgressBar currentWeatherProgressBar;

    private SearchView mSearchView;

    public static WeatherForecastFragment newInstance() {
        return new WeatherForecastFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                        Log.i(TAG, "find location from on start");
                        if (mSearchView != null && !mSearchView.getQuery().toString().isEmpty()) {
                            Log.i(TAG, "Query : " + mSearchView.getQuery().toString());
                            findWeatherForSearchQuery(mSearchView.getQuery().toString());
                        } else {
                            findWeatherForCurrentLocation();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        String reason = null;
                        if (i == 1)
                            reason = " CAUSE_SERVICE_DISCONNECTED";
                        else if (i == 2)
                            reason = "CAUSE_NETWORK_LOST";

                        Log.e(TAG, "Connection Suspendend with reason -> " + reason);
                    }
                })
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragement_weather_forecast, container, false);

        mWeatherForecastRecyclerView = (RecyclerView) v
                .findViewById(R.id.fragment_forecast_recycler_view);

        mWeatherForecastRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        mLocationTextView = (TextView) v.findViewById(R.id.location);
        mDateTimeTextView = (TextView) v.findViewById(R.id.date);
        mTempView = (TextView) v.findViewById(R.id.temp);
        mDescriptionTextView = (TextView) v.findViewById(R.id.description);
        mWeatherIcon = (ImageView) v.findViewById(R.id.weather_icon);
        mPressureView = (TextView) v.findViewById(R.id.pressure);
        mHumidityView = (TextView) v.findViewById(R.id.humidity);
        mWindView = (TextView) v.findViewById(R.id.wind);

        weatherForecastProgressBar = (ProgressBar) v.findViewById(R.id.weather_forecast_progressBar);
        currentWeatherProgressBar =(ProgressBar) v.findViewById(R.id.current_weather_progressBar);

        return v;

    }

    private void setupAdapter() {
        if (isAdded()) {
            mWeatherForecastRecyclerView.setAdapter(new WeatherForecastAdapter(mWeatherForecastReports));
        }
    }

    private void updateUIWithWeatherReport(){
        if (isAdded()) {
            mLocationTextView.setText(mCurrentWeatherReport.getLocation());
            mDateTimeTextView.setText(mCurrentWeatherReport.getDate());
            mTempView.setText(mCurrentWeatherReport.getTemperature() + " \u2109");
            mDescriptionTextView.setText(mCurrentWeatherReport.getDescription());
            mWeatherIcon.setImageDrawable(new BitmapDrawable(getResources(), mCurrentWeatherReport.getWeaterIcon()));
            mPressureView.setText(String.valueOf(mCurrentWeatherReport.getPressure()).trim());
            mHumidityView.setText(String.valueOf(mCurrentWeatherReport.getHumidity()).trim());
            mWindView.setText(String.valueOf(mCurrentWeatherReport.getWind()).trim());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_weather_forecast, menu);

        MenuItem currentLocationItem = menu.findItem(R.id.action_locate);
        currentLocationItem.setEnabled(mClient.isConnected());

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                currentWeatherProgressBar.setVisibility(View.VISIBLE);
                weatherForecastProgressBar.setVisibility(View.VISIBLE);
                mSearchView.clearFocus();
                Log.d(TAG, "QueryTextSubmit: " + s);
                findWeatherForSearchQuery(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "QueryTextChange: " + s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                currentWeatherProgressBar.setVisibility(View.VISIBLE);
                weatherForecastProgressBar.setVisibility(View.VISIBLE);
                mSearchView.clearFocus();
                Log.i(TAG,"find location from Options Item Selected");
                findWeatherForCurrentLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findWeatherForCurrentLocation() {
        Log.i(TAG," Google find Current Location is called");
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);

        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i(TAG, "Got a fix: " + location);
                        mLocation = location;
                        new currentWeatherTask().execute("");
                        new WeatherForecastTask().execute("");
                    }
                });
    }

    private void findWeatherForSearchQuery(String query){
        new currentWeatherTask().execute(query);
        new WeatherForecastTask().execute(query);
    }

    private class currentWeatherTask extends AsyncTask<String,Void,CurrentWeatherReport> {
        @Override
        protected CurrentWeatherReport doInBackground(String... params) {

            if (!params[0].isEmpty()) {
                Log.i(TAG,"Weather for search query is fired");
                return new WeatherMapFetcher().fetchCurrentWeatherReport(params[0]);
            }
            else {
                Log.i(TAG,"Weather for current location is fired");
                return new WeatherMapFetcher().fetchCurrentWeatherReport(mLocation);
            }
        }

        @Override
        protected void onPostExecute(CurrentWeatherReport currentWeatherReport) {
            if (currentWeatherReport != null) {
                currentWeatherProgressBar.setVisibility(View.GONE);
                mCurrentWeatherReport = currentWeatherReport;
                updateUIWithWeatherReport();
            }
            else{
                showErrrorMessageDialog();
            }
        }
    }

    private class WeatherForecastTask extends AsyncTask<String,Integer,List<WeatherForecastReport>> {
        @Override
        protected List<WeatherForecastReport> doInBackground(String... params) {

            if (!params[0].isEmpty()) {
                Log.i(TAG,"Weather for search query is fired");
                return new WeatherMapFetcher().fetchWeatherForecastReport(params[0]);
            }
            else {
                Log.i(TAG,"Weather for current location is fired");
                return new WeatherMapFetcher().fetchWeatherForecastReport(mLocation);
            }
        }

        @Override
        protected void onPostExecute(List<WeatherForecastReport> items) {
            if (items != null) {
                weatherForecastProgressBar.setVisibility(View.GONE);
                mWeatherForecastReports = items;
                setupAdapter();
            } else {
                showErrrorMessageDialog();
            }
        }

    }

    //Weather forecast view holder and adapter

    private class WeatherForecastHolder extends RecyclerView.ViewHolder{
        private TextView mDateTextView;
        private TextView mTempView;
        private ImageView mWeatherForecastIcon;

        private WeatherForecastReport mWeatherForecastReport;

        public WeatherForecastHolder(View itemView) {
            super(itemView);

            mDateTextView = (TextView) itemView.findViewById(R.id.date);
            mTempView = (TextView) itemView.findViewById(R.id.temp);
            mWeatherForecastIcon = (ImageView) itemView.findViewById(R.id.weather_forecast_icon);
        }

        public void bindWeatherForecastReport(WeatherForecastReport weatherForecastReport) {
            mDateTextView.setText(weatherForecastReport.getDate());
            mTempView.setText(weatherForecastReport.getTemperature() + " \u2109");
            mWeatherForecastIcon.setImageDrawable(new BitmapDrawable(getResources(), mCurrentWeatherReport.getWeaterIcon()));
        }
    }

    private class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastHolder> {
        private List<WeatherForecastReport> mWeatherForecastReports;

        public WeatherForecastAdapter(List<WeatherForecastReport> weatherForecastReports) {
            mWeatherForecastReports = weatherForecastReports;
        }
        @Override
        public WeatherForecastHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.weather_forecast_card, viewGroup, false);
            return new WeatherForecastHolder(view);
        }
        @Override
        public void onBindViewHolder(WeatherForecastHolder weatherForecastHolder, int position) {
            WeatherForecastReport weatherForecastReport = null;
            if (mWeatherForecastReports != null) {
                weatherForecastReport = mWeatherForecastReports.get(position);
            }

            weatherForecastHolder.bindWeatherForecastReport(weatherForecastReport);
        }
        @Override
        public int getItemCount() {
            return mWeatherForecastReports.size();
        }
    }

    private void showErrrorMessageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.api_error_message)
                .setTitle("Error in api query!!!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Error message dialog is captured");
                        findWeatherForCurrentLocation();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

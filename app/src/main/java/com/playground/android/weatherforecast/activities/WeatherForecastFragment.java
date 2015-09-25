package com.playground.android.weatherforecast.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import com.playground.android.weatherforecast.R;
import com.playground.android.weatherforecast.bean.CurrentWeatherReport;
import com.playground.android.weatherforecast.bean.WeatherForecastReport;
import com.playground.android.weatherforecast.networking.WeatherMapFetcher;

import java.util.List;

/**
 * Created by anarajendran on 9/19/15.
 */
public class WeatherForecastFragment extends Fragment {

    private static final String TAG = "WeatherForecastLoader";

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

    private static final String KEY_SEARCH_QUERY = "SearchQuery";

    private Bundle mSavedInstanceState;

    public boolean created;
    public boolean started;
    public boolean viewCreated;

    boolean mIsLandScape = false;

    private static final int CURRENT_WEATHER_LOADER = 1;
    private static final int WEATHER_FORECAST_LOADER = 2;

    public static WeatherForecastFragment newInstance() {
        return new WeatherForecastFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        created = true;

        mIsLandScape = getActivity().getResources().getConfiguration().orientation == 2;

        if (mIsLandScape) {
            setHasOptionsMenu(false);
        } else {
            setHasOptionsMenu(true);
        }

        if (savedInstanceState != null) {
            mSavedInstanceState = savedInstanceState;
        }

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        getActivity().invalidateOptionsMenu();

                        if (getLoaderManager().getLoader(CURRENT_WEATHER_LOADER) != null && getLoaderManager().getLoader(WEATHER_FORECAST_LOADER) != null) {
                            Log.i(TAG, "Loader is not null");
                            if (getLoaderManager().getLoader(CURRENT_WEATHER_LOADER).isStarted()) {
                                Log.i(TAG, "Loader is not null and already running so just initializing it");
                                initLoaders();
                            }
                        } else if ((mSavedInstanceState != null && mSavedInstanceState.getString(KEY_SEARCH_QUERY) != null
                                && !mSavedInstanceState.getString(KEY_SEARCH_QUERY).isEmpty())) {
                            Log.i(TAG, "Search for Search query in OnCreate");
                            findWeatherForSearchQuery(mSavedInstanceState.getString(KEY_SEARCH_QUERY));
                        } else {
                            Log.i(TAG, "Search for current location in OnCreate");
                            findWeatherForCurrentLocation();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        String reason = null;
                        if (i == 1) {
                            reason = " CAUSE_SERVICE_DISCONNECTED";
                        } else if (i == 2) {
                            reason = "CAUSE_NETWORK_LOST";
                        }

                        Log.e(TAG, "Connection Suspendend with reason -> " + reason);
                    }
                })
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewCreated = true;
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
        currentWeatherProgressBar = (ProgressBar) v.findViewById(R.id.current_weather_progressBar);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        if (mSavedInstanceState != null && mSavedInstanceState.getString(KEY_SEARCH_QUERY) != null
                && !mSavedInstanceState.getString(KEY_SEARCH_QUERY).isEmpty()) {
            savedInstanceState.putString(KEY_SEARCH_QUERY, mSavedInstanceState.getString(KEY_SEARCH_QUERY));
        } else {
            if (mSearchView != null) {
                savedInstanceState.putString(KEY_SEARCH_QUERY, mSearchView.getQuery().toString());
            }
        }
    }

    private void setupAdapter() {
        if (isAdded()) {
            mWeatherForecastRecyclerView.setAdapter(new WeatherForecastAdapter(mWeatherForecastReports));
        }
    }

    private void updateUIWithWeatherReport() {
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
        started = true;
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
        mSearchView.setQueryHint(getResources().getString(R.string.search));

        if (mSavedInstanceState != null && mSavedInstanceState.getString(KEY_SEARCH_QUERY) != null
                && !mSavedInstanceState.getString(KEY_SEARCH_QUERY).isEmpty()) {
            mSearchView.setQuery(mSavedInstanceState.getString(KEY_SEARCH_QUERY), false);
        }
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
                mSavedInstanceState = null;
                currentWeatherProgressBar.setVisibility(View.VISIBLE);
                weatherForecastProgressBar.setVisibility(View.VISIBLE);
                mSearchView.clearFocus();
                mSearchView.setQuery("", false);
                Log.i(TAG, "find current location weather from Options Item Selected");
                findWeatherForCurrentLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findWeatherForCurrentLocation() {
        Log.i(TAG, "Checking for null location value in on Create() is NULL");
        Log.i(TAG, " Google find Current Location is called");
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);

        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i(TAG, "LOCATION CHANGED: New location : " + location.toString());
                        Log.i(TAG, "Got a fix: " + location);
                        mLocation = location;
                        restartLoaders(null);
                    }
                });

    }

    private void restartLoaders(String query) {
        getLoaderManager().restartLoader(CURRENT_WEATHER_LOADER, null, new CurrentWeatherReportLoaderListener(query));
        if (!mIsLandScape) {
            getLoaderManager().restartLoader(WEATHER_FORECAST_LOADER, null, new WeatherForecastReportLoaderListener(query));
        }
    }

    private void initLoaders() {
        getLoaderManager().initLoader(CURRENT_WEATHER_LOADER, null, new CurrentWeatherReportLoaderListener(null));
        if (!mIsLandScape) {
            getLoaderManager().initLoader(WEATHER_FORECAST_LOADER, null, new WeatherForecastReportLoaderListener(null));
        }
    }

    private void findWeatherForSearchQuery(String query) {
        restartLoaders(query);
    }

    //Class for Implementing Current Weather report loader (currentWeatherTaskLoader) call back methods
    private class CurrentWeatherReportLoaderListener implements LoaderManager.LoaderCallbacks<CurrentWeatherReport> {
        String mQuery = null;

        public CurrentWeatherReportLoaderListener(String query) {
            mQuery = query;
        }

        @Override
        public Loader<CurrentWeatherReport> onCreateLoader(int id, Bundle args) {
            // This is called when a new Loader needs to be created.
            Log.i(TAG, "CurrentWeatherReportLoaderListener: On Create Loader");
            return new currentWeatherTaskLoader(getActivity(), mLocation, mQuery);
        }

        @Override
        public void onLoadFinished(Loader<CurrentWeatherReport> loader, CurrentWeatherReport currentWeatherReport) {
            // Set the new data in the adapter.
            Log.i(TAG, "CurrentWeatherReportLoaderListener: On Loader Finished");
            if (currentWeatherReport != null) {
                currentWeatherProgressBar.setVisibility(View.GONE);
                mCurrentWeatherReport = currentWeatherReport;
                updateUIWithWeatherReport();
            } else {
                showErrrorMessageDialog();
            }
        }

        @Override
        public void onLoaderReset(Loader<CurrentWeatherReport> loader) {
            // Clear the data in the adapter.
            Log.i(TAG, "CurrentWeatherReportLoaderListener: On Loader Reset");

        }
    }

    //Class for Implementing Weather forecast report loader (WeatherForecastTaskLoader) call back methods
    private class WeatherForecastReportLoaderListener implements LoaderManager.LoaderCallbacks<List<WeatherForecastReport>> {
        String mQuery = null;

        public WeatherForecastReportLoaderListener(String query) {
            mQuery = query;
        }

        @Override
        public Loader<List<WeatherForecastReport>> onCreateLoader(int id, Bundle args) {
            // This is called when a new Loader needs to be created.
            Log.i(TAG, "WeatherForecastReportLoaderListener: On Create Loader");
            return new WeatherForecastTaskLoader(getActivity(), mLocation, mQuery);

        }

        @Override
        public void onLoadFinished(Loader<List<WeatherForecastReport>> loader, List<WeatherForecastReport> weatherForecastReports) {
            // Set the new data in the adapter.
            Log.i(TAG, "WeatherForecastReportLoaderListener: On Loader Finished");
            if (weatherForecastReports != null) {
                weatherForecastProgressBar.setVisibility(View.GONE);
                mWeatherForecastReports = weatherForecastReports;
                setupAdapter();
            } else {
                showErrrorMessageDialog();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<WeatherForecastReport>> loader) {
            // Clear the data in the adapter.
            Log.i(TAG, "WeatherForecastReportLoaderListener: On Loader Reset");

        }

    }

    //Weather forecast view holder

    private class WeatherForecastHolder extends RecyclerView.ViewHolder {
        private TextView mDateTextView;
        private TextView mTempView;
        private ImageView mWeatherForecastIcon;

        public WeatherForecastHolder(View itemView) {
            super(itemView);

            mDateTextView = (TextView) itemView.findViewById(R.id.date);
            mTempView = (TextView) itemView.findViewById(R.id.temp);
            mWeatherForecastIcon = (ImageView) itemView.findViewById(R.id.weather_forecast_icon);
        }

        public void bindWeatherForecastReport(WeatherForecastReport weatherForecastReport) {
            mDateTextView.setText(weatherForecastReport.getDate());
            mTempView.setText(weatherForecastReport.getTemperature() + " \u2109");
            mWeatherForecastIcon.setImageDrawable(new BitmapDrawable(getResources(), weatherForecastReport.getWeaterIcon()));
        }
    }


    //Weather forecast Adapater
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

    private void showErrrorMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.api_error_message)
                .setTitle("Error in api query!!!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Error message dialog is captured");

                        if (mLocation == null) {
                            Log.i(TAG, "Checking for null location value in on showErrrorMessageDialog() is NULL");
                        } else
                            Log.i(TAG, "Checking for null location value in on showErrrorMessageDialog() is NOT NULL");
                        findWeatherForCurrentLocation();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Async Loader for getting current weather report task loader
    private static class currentWeatherTaskLoader extends AsyncTaskLoader<CurrentWeatherReport> {

        CurrentWeatherReport mCurrentWeatherReport;
        Location mLocation;
        String mQuery;

        public currentWeatherTaskLoader(Context context, Location location, String query) {
            super(context);
            mLocation = location;
            mQuery = query;

            Log.i(TAG, "currentWeatherTaskLoader: On Loader Constructor");
        }

        @Override
        public CurrentWeatherReport loadInBackground() {
            Log.i(TAG, "currentWeatherTaskLoader: starting loader background thread");
            if (mQuery != null && !mQuery.isEmpty()) {
                return new WeatherMapFetcher().fetchCurrentWeatherReport(null, mQuery);
            } else {
                return new WeatherMapFetcher().fetchCurrentWeatherReport(mLocation, null);
            }
        }

        @Override
        protected void onStartLoading() {
            Log.i(TAG, "currentWeatherTaskLoader: On start Loading");
            if (mCurrentWeatherReport != null) {
                deliverResult(mCurrentWeatherReport);
            }

            if (takeContentChanged() || mCurrentWeatherReport == null) {
                forceLoad();
            }
        }

        @Override
        public void deliverResult(CurrentWeatherReport currentWeatherReport) {
            Log.i(TAG, "currentWeatherTaskLoader: On Loader delivering result");
            if (isReset()) {
                if (currentWeatherReport != null) {
                    onReleaseResources(currentWeatherReport);
                }
            }
            CurrentWeatherReport report = mCurrentWeatherReport;
            mCurrentWeatherReport = currentWeatherReport;

            if (isStarted()) {
                super.deliverResult(currentWeatherReport);
            }

            if (report != null) {
                onReleaseResources(report);
            }
        }

        @Override
        protected void onStopLoading() {
            Log.i(TAG, "currentWeatherTaskLoader: On stop Loading");
            cancelLoad();
        }

        @Override
        public void onCanceled(CurrentWeatherReport currentWeatherReport) {
            Log.i(TAG, "currentWeatherTaskLoader: On Loader Cancelled");
            super.onCanceled(currentWeatherReport);

            onReleaseResources(currentWeatherReport);
        }

        @Override
        protected void onReset() {
            Log.i(TAG, "currentWeatherTaskLoader: On Loader Reset");
            super.onReset();

            onStopLoading();

            if (mCurrentWeatherReport != null) {
                onReleaseResources(mCurrentWeatherReport);
                mCurrentWeatherReport = null;
            }
        }

        protected void onReleaseResources(CurrentWeatherReport currentWeatherReport) {
            Log.i(TAG, "currentWeatherTaskLoader: On Loader Released");
        }
    }

    //Async Loader for getting weather forecast report task loader
    private static class WeatherForecastTaskLoader extends AsyncTaskLoader<List<WeatherForecastReport>> {

        List<WeatherForecastReport> mWeatherForecastReportList;
        Location mLocation;
        String mQuery;

        public WeatherForecastTaskLoader(Context context, Location location, String query) {
            super(context);
            mLocation = location;
            mQuery = query;

            Log.i(TAG, "WeatherForecastTaskLoader: On Loader Constructor");
        }

        @Override
        public List<WeatherForecastReport> loadInBackground() {
            Log.i(TAG, "WeatherForecastTaskLoader: starting loader background thread");
            if (mQuery != null && !mQuery.isEmpty()) {
                return new WeatherMapFetcher().fetchWeatherForecastReport(null, mQuery);
            } else {
                return new WeatherMapFetcher().fetchWeatherForecastReport(mLocation, null);
            }
        }

        @Override
        protected void onStartLoading() {
            Log.i(TAG, "WeatherForecastTaskLoader: On start Loading");
            if (mWeatherForecastReportList != null) {
                deliverResult(mWeatherForecastReportList);
            }

            if (takeContentChanged() || mWeatherForecastReportList == null) {
                forceLoad();
            }
        }

        @Override
        public void deliverResult(List<WeatherForecastReport> weatherForecastReportList) {
            Log.i(TAG, "WeatherForecastTaskLoader: On Loader delivering result");
            if (isReset()) {
                if (weatherForecastReportList != null) {
                    onReleaseResources(weatherForecastReportList);
                }
            }
            List<WeatherForecastReport> weatherForecastReports = mWeatherForecastReportList;
            mWeatherForecastReportList = weatherForecastReportList;

            if (isStarted()) {
                super.deliverResult(weatherForecastReportList);
            }

            if (weatherForecastReports != null) {
                onReleaseResources(weatherForecastReports);
            }
        }

        @Override
        protected void onStopLoading() {
            Log.i(TAG, "WeatherForecastTaskLoader: On stop Loading");
            cancelLoad();
        }

        @Override
        public void onCanceled(List<WeatherForecastReport> weatherForecastReports) {
            Log.i(TAG, "WeatherForecastTaskLoader: On Loader Cancelled");
            super.onCanceled(weatherForecastReports);

            onReleaseResources(weatherForecastReports);
        }

        @Override
        protected void onReset() {
            Log.i(TAG, "WeatherForecastTaskLoader: On Loader Reset");
            super.onReset();

            onStopLoading();

            if (mWeatherForecastReportList != null) {
                onReleaseResources(mWeatherForecastReportList);
                mWeatherForecastReportList = null;
            }
        }

        protected void onReleaseResources(List<WeatherForecastReport> weatherForecastReports) {
            Log.i(TAG, "WeatherForecastTaskLoader: On Loader Released");
        }
    }

}

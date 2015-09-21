package com.playground.android.weatherforecast.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.playground.android.weatherforecast.R;

/**
 * Created by anarajendran on 9/19`/15.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    public static String PACKAGE_NAME;

    protected abstract Fragment createFragment();


    /**
     * You annotate getLayoutResId() with @LayoutRes to tell Android Studio that any implementation of this method should return a valid layout resource ID.
     * @return
     */
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment);
        setContentView(getLayoutResId());
        PACKAGE_NAME = getApplicationContext().getPackageName();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit(); }
    }
}

package com.example.arrietty.demoapp;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by asus on 2017/11/14.
 */

public class MyApplication extends Application {
    private static MyApplication mApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }
    /**
     * 获取上下文 *
     */
    public static MyApplication getBaseApplication() {
        return mApplication;
    }

    private Tracker mTracker;
    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
           //global_tracker, // Tracker used by all the apps from a company. eg: roll-up tracking.
            //ecommerce_tracker, // Tracker used by all ecommerce transactions from a company.
            mTracker = analytics.newTracker(R.xml.global_tracker);
            //自动上报crash
           // mTracker.enableExceptionReporting(true);
        }
        return mTracker;
    }
}

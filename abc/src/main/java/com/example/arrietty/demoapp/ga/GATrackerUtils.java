package com.example.arrietty.demoapp.ga;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.arrietty.demoapp.MyApplication;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by arrietty on 2017/11/27.
 */

public class GATrackerUtils {
    private static  GATrackerUtils instanse;
    private Tracker mTracker;
    private static final String TAG ="GATrackerUtils";
    private GATrackerUtils(Context context){
        MyApplication app = (MyApplication) context.getApplicationContext();
        mTracker= app.getDefaultTracker();
    }
    public static synchronized  GATrackerUtils getInstance(Context context){
        if(instanse==null){
            instanse= new GATrackerUtils(context);
        }
        return instanse;
    }
    /*
    记录屏幕切换情况
     */
    public void tranceScreenSwitch(Activity activity){
        String name = activity.getClass().getName().toString();
        Log.i(TAG, "Setting screen name: " + name);
        if(mTracker==null){
            return;
        }
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    /*
    发送事件
     */
    public void tranceEvent(String action, String lable){
        if(mTracker==null){
            return;
        }
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction(action)
                .setLabel(lable)
                .build());
    }
    /*
    用户ID
     */
    public void tranceUserID(String userId){
        if(mTracker==null){
            return;
        }
        // You only need to set User ID on a tracker once. By setting it on the
        // tracker, the ID will be sent with all subsequent hits.
        mTracker.set("&uid", userId);

        // This hit will be sent with the User ID value and be visible in
        // User-ID-enabled views (profiles).
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("User Sign In")
                .build());
    }
    /*
    发送用户计时，请创建 TimingBuilder 并使用 Tracker 发送
     */
    public void tranceTimer(Activity activity, long time, String timeName, String timeLable) {
            String categoryName = activity.getClass().getName().toString();
        if (mTracker == null) {
            return;
        }
        // Build and send timing.
        mTracker.send(new HitBuilders.TimingBuilder()
                .setCategory(categoryName)
                .setValue(time)
                .setVariable(timeName)
                .setLabel(timeLable)
                .build());
    }

    public void activityStart(Activity activity){
        GoogleAnalytics.getInstance(activity).reportActivityStart(activity);
    }
    public void activityStop(Activity activity){
        GoogleAnalytics.getInstance(activity).reportActivityStop(activity);
    }
}

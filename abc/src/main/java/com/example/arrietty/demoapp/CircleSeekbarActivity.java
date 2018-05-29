package com.example.arrietty.demoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.example.arrietty.demoapp.views.CircleSeekBar;

/**
 * Created by asus on 2018/3/2.
 */

public class CircleSeekbarActivity extends AppCompatActivity implements CircleSeekBar.OnProgressChangeListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "MainActivity";
    private CircleSeekBar mCircleSeekBar;
    private TextView mProgressTv;
    private SeekBar mProgressSb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mCircleSeekBar = (CircleSeekBar) findViewById(R.id.circle_seek_bar);
        mCircleSeekBar.setOnProgressChangeListener(this);
        mProgressTv = (TextView) findViewById(R.id.tv_progress);
        mProgressSb = (SeekBar) findViewById(R.id.sb_progress);
        mProgressSb.setOnSeekBarChangeListener(this);
        scheduleTimer();

    }

    private void updateUI(int progress) {
        mProgressTv.setText("progress:" + progress);
        mProgressSb.setProgress(progress);
        mCircleSeekBar.setCurrentProgress(progress);
    }

    private void scheduleTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI(5);
                        }
                    });

                }
            };
        }
        timer.schedule(task, 1000, 5 * 1000);
    }

    Timer timer;
    TimerTask task;


    @Override
    public void onProgress(int progress) {
        mProgressTv.setText("progress:" + progress);
        mProgressSb.setProgress(progress);
    }

    @Override
    public void onStartTrackingTouch(CircleSeekBar circleSeekBar) {
        Log.v(TAG, "onStartTrackingTouch " + circleSeekBar.getProgress());
    }

    @Override
    public void onStopTrackingTouch(CircleSeekBar circleSeekBar) {
        Log.v(TAG, "onStopTrackingTouch " + circleSeekBar.getProgress());
    }

    @Override
    public void onCancleTrackingTouch(CircleSeekBar circleSeekBar) {
        Log.v(TAG, "onCancleTrackingTouch " + circleSeekBar.getProgress());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mCircleSeekBar.setCurrentProgress(i);
        mProgressTv.setText("progress:" + i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
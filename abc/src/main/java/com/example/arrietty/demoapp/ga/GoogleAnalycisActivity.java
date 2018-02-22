package com.example.arrietty.demoapp.ga;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.arrietty.demoapp.R;

/**
 * Created by asus on 2017/11/27.
 */

public class GoogleAnalycisActivity extends AppCompatActivity {
    private static final String TAG ="SpanActivity";
    private Button mButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ga);
        mButton = (Button)findViewById(R.id.send);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GATrackerUtils.getInstance(GoogleAnalycisActivity.this).tranceEvent("click event", "send");
            }
        });
        //添加此方法，用于GA统计
        GATrackerUtils.getInstance(this).activityStart(this);

    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //添加此方法，用于GA统计
        GATrackerUtils.getInstance(this).activityStop(this);
    }
}


package com.example.arrietty.demoapp.SubToMainThread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.Executor;

public class ThreadActivity extends AppCompatActivity {
    private Platform mPlatform;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlatform = Platform.get();
        mPlatform.execute(new Runnable()
        {
            @Override
            public void run()
            {
                //onError onAfter 运行在主线程
//                callback.onError(call, e, id);
//                callback.onAfter(id);


            }
        });
    }
    public Executor getDelivery()
    {
        return mPlatform.defaultCallbackExecutor();
    }



}

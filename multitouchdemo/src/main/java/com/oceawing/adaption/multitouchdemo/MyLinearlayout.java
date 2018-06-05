package com.oceawing.adaption.multitouchdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearlayout extends LinearLayout {
    private String TAG ="MainActivity.Lin";
    public MyLinearlayout(Context context) {
        super(context);
    }

    public MyLinearlayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearlayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG, "onTouchEvent ACTION_DOWN111");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.v(TAG, "onTouchEvent ACTION_POINTER_DOWN1111");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.v(TAG, "AonTouchEvent CTION_POINTER_UP1111");
                break;
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "onTouchEvent ACTION_UP1111111");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG, "onTouchEvent ACTION_MOVE111111111");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.v(TAG, "onTouchEvent ACTION_CANCEL1111111");
                break;
        }
        return true;
    }
}

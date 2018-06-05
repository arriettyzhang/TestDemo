package com.oceawing.adaption.multitouchdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewDebug;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity implements View.OnTouchListener{
    private static final String TAG ="MainActivity";
    private TextView btn1, btn2, btn1_velocity, btn2_velocity;
    private VelocityTracker mVelocityTracker;
    private int mMaxVelocity;
    private int TouchSlop;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVelocityTracker = VelocityTracker.obtain();
        mMaxVelocity = ViewConfiguration.get(this).getScaledMaximumFlingVelocity();
        TouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        Log.e(TAG, "TouchSlop = "+TouchSlop);
        setContentView(R.layout.activity_main);
        btn1_velocity = (TextView)findViewById(R.id.tv1_info);
        btn2_velocity = (TextView)findViewById(R.id.tv2_info);
        btn1 = (TextView)findViewById(R.id.btn1);
        btn2 = (TextView)findViewById(R.id.btn2);
        btn1.setOnTouchListener(this);
        btn2.setOnTouchListener(this);
        Log.v(TAG, "test d= "+getMoveDistance(2, 2, 5,6));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG, "onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.v(TAG, "onTouchEvent ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.v(TAG, "AonTouchEvent CTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "onTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG, "onTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.v(TAG, "onTouchEvent ACTION_CANCEL");
                break;
        }

        return super.onTouchEvent(event);

    }

    private float lastView1X, lastView1Y, lastView2X, lastView2Y;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        acquireVelocityTracker(event);
        final VelocityTracker verTracker = mVelocityTracker;
        int which=0;
        if(v.getId() ==R.id.btn1){
            which =1;
        }else if(v.getId() ==R.id.btn2){
            which =2;
        }
        final View mVIew =v;

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                if(which ==1){
                    Log.v(TAG, "onTouch btn1 ACTION_DOWN");
                    lastView1X = event.getX();
                    lastView1Y = event.getY();
                }else if(which ==2){
                    Log.v(TAG, "onTouch btn2 ACTION_DOWN");
                    lastView2X = event.getX();
                    lastView2Y = event.getY();
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(which==1){
                    Log.v(TAG, "onTouch btn1 ACTION_POINTER_DOWN");
                }else if(which==2){
                    Log.v(TAG, "onTouch btn2 ACTION_POINTER_DOWN");
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(which==1){
                    Log.v(TAG, "onTouch btn1 ACTION_POINTER_UP");
                }else if(which==2){
                    Log.v(TAG, "onTouch btn2 ACTION_POINTER_UP");
                }
                break;
            case MotionEvent.ACTION_UP:
                if(which==1){
                    Log.v(TAG, "onTouch btn1 ACTION_UP");
                    lastView1X = event.getX();
                    lastView1Y = event.getY();
                }else if(which==2){
                    Log.v(TAG, "onTouch btn2 ACTION_UP");
                    lastView2X = event.getX();
                    lastView2Y = event.getY();
                }
                releaseVelocityTracker();
                break;
            case MotionEvent.ACTION_MOVE:
                float startX=0;
                float startY=0;
                float endX = event.getX();
                float endY = event.getY();
                if(which==1){
                    startX = lastView1X;
                    startY = lastView1Y;
                }else if(which==2){
                    startX = lastView2X;
                    startY = lastView2Y;
                }
                if(getMoveDistance(startX ,startY, endX, endY)>TouchSlop){
                    if(which==1){
                        lastView1X = endX;
                        lastView1Y = endY;
                        Log.v(TAG, "onTouch btn1 ACTION_MOVE");
                    }else if(which==2){
                        lastView2X = endX;
                        lastView2Y = endY;
                        Log.v(TAG, "onTouch btn2 ACTION_MOVE");
                    }
                    verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                    final float velocityX = verTracker.getXVelocity();
                    final float velocityY = verTracker.getYVelocity();
                    recodeInfo((TextView) mVIew, velocityX, velocityY);
                    Log.v(TAG, "onTouch ACTION_MOVE mView " +((TextView) mVIew).getText().toString() +"  velocity ="+getSqrt(velocityX, velocityY));
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if(which ==1){
                    Log.v(TAG, "onTouch btn1 ACTION_CANCEL");
                }else if(which ==2){
                    Log.v(TAG, "onTouch btn2 ACTION_CANCEL");
                }
                releaseVelocityTracker();
                break;
        }
        return true;
    }
    //释放VelocityTracker

    private double getMoveDistance(float startX, float startY, float endX, float endY){
        double d = getSqrt((startX - endX),(startY-endY));
        return d;
    }
    private double getSqrt(double x, double y){
        return Math.sqrt(x*x + y*y);
    }
    private void releaseVelocityTracker() {
        if(null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
    private static final String sFormatStr = "velocityX=%f\nvelocityY=%f";

    /**
     * 记录当前速度
     *
     * @param velocityX x轴速度
     * @param velocityY y轴速度
     */
    private void recodeInfo(TextView infoTv, final float velocityX, final float velocityY) {
        final String info = String.format(sFormatStr, velocityX, velocityY);
        infoTv.setText(info);
    }
    private void acquireVelocityTracker(final MotionEvent event) {
        if(null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
}

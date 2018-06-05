package com.oceawing.adaption.multitouchdemo;


import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyImageView extends SurfaceView implements SurfaceHolder.Callback{
    public MyImageView(Context context) {
        super(context);
    }
    float beforeLenght, afterLenght,scale,beforeX,beforeY,afterX,afterY;
    public void  scaleWithFinger(MotionEvent event) {

        float moveX = event.getX(1) - event.getX(0);

        float moveY = event.getY(1) - event.getY(0);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beforeLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));
                break;
            case MotionEvent.ACTION_MOVE:
                // 得到两个点之间的长度
                afterLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));
                float gapLenght = afterLenght - beforeLenght;
                if (gapLenght == 0) {
                    break;
                }
     // 如果当前时间两点距离大于前一时间两点距离，则传0，否则传1
                if (gapLenght > 0) {
                    this.setScale(scale, 0);
                } else {
                    this.setScale(scale, 1);
                }
                beforeLenght = afterLenght;
                break;
        }

    }


    public void  moveWithFinger(MotionEvent event) {
         switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beforeX = event.getX();
                beforeY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                afterX = event.getX();
                afterY = event.getY();
//                this.setLocation((int) (afterX - beforeX),
//                        (int) (afterY - beforeY));
                beforeX = afterX;
                beforeY = afterY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

    }

    private
    void  setScale(float temp, int flag) {

        if (flag == 0) {
//            this.setFrame(this.getLeft() - (int) (temp * this.getWidth()),
//
//                    this.getTop() - (int) (temp * this.getHeight()),
//
//                    this.getRight() + (int) (temp * this.getWidth()),
//
//                    this.getBottom() + (int) (temp * this.getHeight()));

        } else {
//            this.setFrame(this.getLeft() + (int) (temp * this.getWidth()),
//                    this.getTop() + (int) (temp * this.getHeight()),
//                    this.getRight() - (int) (temp * this.getWidth()),
//                    this.getBottom() - (int) (temp * this.getHeight()));
        }

    }
    public boolean  onTouchEvent(MotionEvent event) {
//        if (inView(imageView, event)) {
//
//            if (event.getPointerCount() == 2) {
//                imageView.scaleWithFinger(event);
//            } else if  (event.getPointerCount() == 1) {
//                imageView.moveWithFinger(event);
//            }
//
//        }

        return true;

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

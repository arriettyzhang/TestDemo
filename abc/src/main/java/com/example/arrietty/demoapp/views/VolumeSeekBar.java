package com.example.arrietty.demoapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.arrietty.demoapp.R;

/**
 * Created by MD01 on 2018/2/23.
 */

public class VolumeSeekBar extends View {

    private Paint mPaint;
    private RectF mRectF;
    private float mRadius, mRadiusMin, mRadiusMax;
    private float progressWidth;
    private float pointX, startPointX, endPointX, endPointY, pointY, centerX, centerY;
    private float sweepAngle;

    private Bitmap mBitmap;

    public VolumeSeekBar(Context context) {
        super(context);
    }

    public VolumeSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VolumeSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize
                (widthMeasureSpec));
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mRectF = new RectF();
        progressWidth = getContext().getResources().getDisplayMetrics().density * 6 + 0.5f;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.home_icon_rename_delet);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = w / 2 - dip2px(10);
        mRectF.left = getWidth() / 2 - mRadius;
        mRectF.top = getHeight() / 2 - mRadius;
        mRectF.right = getWidth() / 2 + mRadius;
        mRectF.bottom = getHeight() / 2 + mRadius;
        mRadiusMin = mRadius - dip2px(15);
        mRadiusMax = mRadius + dip2px(15);
        startPointX = pointX = (float) (w / 2 - Math.sin(30 * Math.PI / 180) * mRadius - mBitmap
                .getWidth() / 2);
        endPointX = (float) (w / 2 + Math.sin(30 * Math.PI / 180) * mRadius - mBitmap.getWidth()
                / 2);
        endPointY = pointY = (float) (h / 2 + Math.cos(30 * Math.PI / 180) * mRadius - mBitmap
                .getHeight() / 2);
        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(progressWidth / 2);
        mPaint.setColor(Color.parseColor("#cccccc"));
        canvas.drawArc(mRectF, 120, 300, false, mPaint);
        mPaint.setStrokeWidth(progressWidth);
        mPaint.setColor(Color.GREEN);
        canvas.drawArc(mRectF, 120, sweepAngle, false, mPaint);

        canvas.drawBitmap(mBitmap, pointX, pointY, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isValid(x, y)) {
                return false;
            }
        }
        if (y < getHeight() / 2 - mRadius) {
            y = getHeight() / 2 - mRadius;
        }
        pointY = y - mBitmap.getHeight() / 2;
        if (pointY > endPointY) {
            pointY = endPointY;
            if (x <= getWidth() / 2) {
                pointX = startPointX;
                sweepAngle = 0;
            } else {
                pointX = endPointX;
                sweepAngle = 300;
            }
//            Log.e("tag", "sweepAngle -> " + sweepAngle);
            Log.e("tag", "progress -> " + (int) (16.0 / 300 * sweepAngle));
            invalidate();
            return true;
        }

        if (x <= getWidth() / 2) {
            // left
            x = (float) (centerX - Math.sqrt(Math.pow(mRadius, 2) - Math.pow((y - centerY), 2)));
            pointX = x - mBitmap.getWidth() / 2;


            double c = Math.sqrt(Math.pow((x - startPointX), 2) + Math.pow((y - endPointY), 2));
            double a = Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2));
            double b = Math.sqrt(Math.pow((startPointX - centerX), 2) + Math.pow((endPointY -
                    centerY), 2));

            double cos = (a * a + b * b - c * c) / (2 * a * b);
            sweepAngle = (float) (Math.acos(cos) * 180 / Math.PI + 2);

        } else {
            // right
            x = (float) (centerX + Math.sqrt(Math.pow(mRadius, 2) - Math.pow((y - centerY), 2)));
            pointX = x - mBitmap.getWidth() / 2;


            double c = Math.sqrt(Math.pow((x - getWidth() / 2), 2) + Math.pow((y - (getHeight() /
                    2 - mRadius)), 2));
            double a = Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2));
            double b = Math.sqrt(Math.pow((getWidth() / 2 - centerX), 2) + Math.pow(((getHeight()
                    / 2 - mRadius) - centerY), 2));

            double cos = (a * a + b * b - c * c) / (2 * a * b);
            sweepAngle = (float) (Math.acos(cos) * 180 / Math.PI) + 150;
        }
//        Log.e("tag", "sweepAngle -> " + sweepAngle);
        Log.e("tag", "progress -> " + (int) (16.0 / 300 * sweepAngle));
//        Log.e("tag", "pointx -> " + pointX);
//        Log.e("tag", "pointy -> " + pointY);
        invalidate();
        return true;
    }

    private boolean isValid(float x, float y) {

        return ((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) >= mRadiusMin *
                mRadiusMin) && ((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) <=
                mRadiusMax * mRadiusMax);
    }

    private int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setProgress(int progress) {

        sweepAngle = 300.0f / 16 * progress;
        Log.e("tag", "sweepAngle -> " + sweepAngle);
        invalidate();
    }
}

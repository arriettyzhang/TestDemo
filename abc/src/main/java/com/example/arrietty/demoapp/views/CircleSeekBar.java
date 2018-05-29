package com.example.arrietty.demoapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.arrietty.demoapp.R;

/**
 * Created by Administrator on 2016/12/13.
 */

public class CircleSeekBar extends View {
    //弧度转角度
    private static final double RADIAN = 180 / Math.PI;
    //半径
    private int mRadius;
    private int circleSeekbarRadius;
    //画弧的笔
    private Paint mCircleSeekPaint;
    private LinearGradient mLinearGradient;//zfuyan
    private LinearGradient mSeekbagBgLinearGrident;//zfuyan
    private int mViewWidth = 0;//zfuyan
    //弧尽头的小圆点
    private Paint mPointPaint;
    //小圆点的X坐标
    private float mPointX;
    //小圆点的Y坐标
    private float mPointY;
    //小圆点的半径
    private int mThumbRadius;
    //小圆点的颜色
    private int mThumbColor;
    //弧线宽度
    private float mCircleLineWidth;
    //弧线颜色
    private int mSeekbarBgColor;
    //起始角度,绘制的时候会用到
    private int mStartAngle;

    //初始角度
    private int mCalculateStartAngle;
    //最大角度
    private int mMaxAngle;
    //当前的角度
    private float mCurrentAngle = 0;
    //当前进度值
    private int mCurrentProgerss;
    //最大进度值
    private int mMaxProgress;
    private int[] seekColors = new int[]{0xff99cc00, 0xFFFF4081, 0xFF3F51B5};
    private float[] postions = new float[]{0, 0.5f, 1};

    private OnProgressChangeListener mListener;

    public CircleSeekBar(Context context) {
        this(context, null);
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    private void initView(AttributeSet attrs, int defStyleAttr) {


        initAttrs(attrs, defStyleAttr);
        initPaint();

    }

    private void initAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleSeekBar,
                defStyleAttr, 0);
        mCircleLineWidth = a.getDimension(R.styleable.CircleSeekBar_circle_line_width, 20);
        mSeekbarBgColor = a.getColor(R.styleable.CircleSeekBar_seekbar_bg_color, Color.BLUE);
        mThumbRadius = (int) a.getDimension(R.styleable.CircleSeekBar_thumb_radius, mCircleLineWidth / 2);
        mThumbColor = a.getColor(R.styleable.CircleSeekBar_thumb_color, Color.RED);
        mStartAngle = a.getInt(R.styleable.CircleSeekBar_start_angle, 135);
        mMaxAngle = a.getInt(R.styleable.CircleSeekBar_max_angle, 270);
        mCurrentProgerss = a.getInt(R.styleable.CircleSeekBar_current_progress, 0);
        mMaxProgress = a.getInt(R.styleable.CircleSeekBar_max_progress, 100);
        a.recycle();
        //处理角度偏差
        mCalculateStartAngle = mStartAngle % 90;
    }
    public void setSeekDrawableColor(int colors[], float positions[]) {
        this.seekColors = colors;
        this.postions = positions;
        invalidate();

    }
    private void initPaint() {
        mReactF = new RectF();
        //初始化弧形的笔
        mCircleSeekPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleSeekPaint.setStrokeWidth(mCircleLineWidth);
        mCircleSeekPaint.setStyle(Paint.Style.STROKE);
        mCircleSeekPaint.setStrokeCap(Paint.Cap.ROUND);
        mSeekbagBgLinearGrident = new LinearGradient(0, 0, mViewWidth, 0,
                new int[]{mSeekbarBgColor,mSeekbarBgColor, mSeekbarBgColor }, postions, Shader.TileMode.CLAMP);
        //初始化小圆点的笔
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(mThumbColor);
        mPointPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
//        //取宽度和高度的最小值
//        int diameter=Math.min(width,height);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int widthMeasureSpec) {
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int result;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 30;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        return measureWidth(heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() -
                getPaddingBottom()) / 2;
        circleSeekbarRadius = mRadius-mThumbRadius + (int)mCircleLineWidth/4;
        updateProgress(mCurrentProgerss);

    }

    private RectF mReactF;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mViewWidth = getMeasuredWidth();
        mReactF.left = getWidth() /2 - circleSeekbarRadius;
        mReactF.top =getHeight() / 2 -circleSeekbarRadius;
        mReactF.right = getWidth() /2 + circleSeekbarRadius;
        mReactF.bottom = getHeight() / 2 +circleSeekbarRadius;
        mCircleSeekPaint.setStrokeWidth(/*dip2px(4)*/mCircleLineWidth/2);
        mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                seekColors, postions, Shader.TileMode.CLAMP);
        //mCircleSeekPaint.setColor(Color.parseColor("#cccccc"));
        mCircleSeekPaint.setShader(mSeekbagBgLinearGrident);
        canvas.drawArc(mReactF, mStartAngle, mMaxAngle, false, mCircleSeekPaint);
        //mCircleSeekPaint.setColor(mSeekbarBgColor);
        mCircleSeekPaint.setShader(mLinearGradient);
        mCircleSeekPaint.setStrokeWidth(/*dip2px(8)*/mCircleLineWidth);
        if(mCurrentAngle < 0) mCurrentAngle = 0;
        canvas.drawArc(mReactF, mStartAngle, mCurrentAngle, false, mCircleSeekPaint);

        canvas.drawCircle(mPointX, mPointY, mThumbRadius, mPointPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isValid(x, y)) {
                    return false;
                }
                calculateAngle(x, y);
                calculateProgress();
                if(mListener!=null){
                    mListener.onStartTrackingTouch(CircleSeekBar.this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                calculateAngle(x, y);
                calculateProgress();
                break;
            case MotionEvent.ACTION_UP:
                calculateAngle(x, y);
                //添加粘性效果
                if (mCurrentAngle <= 5) {
                    mCurrentAngle = 0;
                }
                calculateProgress();
                if(mListener!=null){
                    mListener.onStopTrackingTouch(CircleSeekBar.this);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if(mListener!=null){
                    mListener.onCancleTrackingTouch(CircleSeekBar.this);
                }
                break;
        }
        invalidate();
        return true;
    }

    private void calculateAngle(float x, float y) {
        float angle;
        //斜边
        double hypotenuse;
        hypotenuse = Math.sqrt(Math.pow(x - mRadius, 2) + Math.pow(y - mRadius, 2));
        double sin = (double) (y - mRadius) / hypotenuse;
        float pointX;
        float pointY;
        boolean isLeft = x - mRadius < 0;
        if (isLeft) {
            angle = (float) (90 - Math.asin(sin) * RADIAN);
            //计算小圆点坐标
            pointX = calculatePointX(isLeft, sin);
            pointY = calculatePointY(sin);
            Log.d("c_angle", "left:" + angle);
        } else {
            angle = (float) (180 + 90 + Math.asin(sin) * RADIAN);
            //计算小圆点坐标
            pointX = calculatePointX(isLeft, sin);
            pointY = calculatePointY(sin);
            Log.d("c_angle", "right:" + angle);
        }
        if (angle >= mCalculateStartAngle && angle <= mMaxAngle + mCalculateStartAngle) {
            mCurrentAngle = Math.round(angle - mCalculateStartAngle);
            Log.d("cur_angle", "mCurrentAngle:" + mCurrentAngle);
            mPointX = pointX;
            mPointY = pointY;
        }
    }

    /**
     * 计算小圆点的X坐标
     *
     * @param isLeft 判断点是否位于弧形的左半部分
     * @param sin    #calculateAngle计算出来的sin值
     * @return 小圆点的X坐标
     */
    private float calculatePointX(boolean isLeft, double sin) {

        float x;
        if(isLeft){
            x = (float) ((mRadius - (mRadius - mCircleLineWidth) * Math.sqrt(1 - sin * sin)) + getPaddingLeft());
        }else{
            x= (float) ((mRadius + (mRadius - mCircleLineWidth) * Math.sqrt(1 - sin * sin)) +getPaddingLeft());
        }

        return x;
    }

    private float calculatePointY(double sin) {
        return (float) ((mRadius + (mRadius - mCircleLineWidth) * sin) + getPaddingTop());
    }

    private void calculateProgress() {
        mCurrentProgerss = Math.round(mCurrentAngle / mMaxAngle * mMaxProgress);
        if (mListener != null) {
            mListener.onProgress(mCurrentProgerss);
        }
    }

    /**
     * 判断点是否在弧形的半径内
     *
     * @param x
     * @param y
     * @return true在   false不在
     */
    private boolean isValid(float x, float y) {
        float radiusMin = mRadius - dip2px(10);
        float radiusMax = mRadius + dip2px(10);
        return Math.pow(x - mRadius - getPaddingLeft(), 2) + Math.pow(y - mRadius - getPaddingTop
                (), 2) >= radiusMin * radiusMin || Math.pow(x - mRadius - getPaddingLeft(), 2) + Math.pow(y - mRadius - getPaddingTop
                (), 2) <= radiusMax * radiusMax;
    }

    private int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void updateProgress(int progress) {
        if (progress > mMaxProgress) {
            throw new IllegalArgumentException("progress must < mMaxProgress");
        }
        mCurrentProgerss = progress;
        mCurrentAngle = (float) mCurrentProgerss / mMaxProgress * mMaxAngle;
        float angle = mCurrentAngle + mCalculateStartAngle;
        //与calculateAngle方法求sin值相反
        boolean isLeft = angle <= 180;
        if (isLeft) {
            mPointX = calculatePointX(isLeft, Math.sin((90 - angle) / RADIAN));
            mPointY = calculatePointY(Math.sin((90 - angle) / RADIAN));
        } else {
            mPointX = calculatePointX(isLeft, Math.sin((angle - 180 - 90) / RADIAN));
            mPointY = calculatePointY(Math.sin((angle - 180 - 90) / RADIAN));
        }
    }
    public void setCurrentProgress(int progress) {
        if (progress > mMaxProgress) {
            throw new IllegalArgumentException("progress must < mMaxProgress");
        }
        updateProgress(progress);
        invalidate();
    }
    public int getProgress(){
        return mCurrentProgerss;
    }
    public void setOnProgressChangeListener(OnProgressChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnProgressChangeListener {
        void onProgress(int progress);
        void onStartTrackingTouch(CircleSeekBar circleSeekBar);
        void onStopTrackingTouch(CircleSeekBar circleSeekBar);
        void onCancleTrackingTouch(CircleSeekBar circleSeekBar);
    }
}

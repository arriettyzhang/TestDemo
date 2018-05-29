package com.example.arrietty.demoapp.myturantable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.arrietty.demoapp.R;

import java.util.List;

public class TurntableView2 extends View {
    private Object obj = new Object();
    private int mBgColor;
    private int circleStrokeDimen;
    private int devideLineColor;
    private int deviceLineWidth;
    private int regionFColor;
    private int regionSColor;
    private int regionTColor;


    private int textColor;


    /**
     * 被选中的position,默认第0个选中
     **/
    private int checkPosition = -1;
    /**
     * 绘画区域的图片。这边会将各个小图片拼接成一张图片
     **/
    private Bitmap fontBitmap;


    private List<WheelInfo> mWheelInfos;
    /**
     * 选项的个数
     **/
    private int mItemCount;

    /**
     * 保存的画布
     **/
    private Canvas mCanvas;

    /**
     * 触摸点的坐标位置
     **/
    private float touchX;
    private float touchY;

    /**
     * 绘制盘块的范围
     */
    private RectF mRange = new RectF();

    /**
     * 绘制盘快的画笔
     */
    private Paint mArcPaint;

    /**
     * 绘制文字的画笔
     */
    private TextPaint mTextPaint;

    /**
     * 绘制分割线的画笔
     */
    private Paint mLinePaint;

    /**
     * 圆形背景的画笔
     **/
    private Paint mBackColorPaint;

    /**
     * 圆盘角度
     **/
    private volatile float mStartAngle = 0;

    /**
     * 控件的中心位置,处于中心位置。x和y是相等的
     */
    private int mCenterX;
    private int mCenterY;
    /**
     * 圆形背景的宽度，这边是在1080p下的780
     **/
    private float mBackColorWidth = 780;

    /**
     * 内圈画盘小圆的宽度，这边是在1080p下的730，绘画区域
     **/
    private float mRangeWidth = 730;

    /**
     * 里面的小图大小，这边是1080p下的115
     **/
    private float mLitterBitWidth = 115;
    private Bitmap mCheckBitmap;
    private RectF mCheckBitmapRect = new RectF();//图片绘制区域

    /**
     * 文字的大小
     */
    private float mTextSize ;
    private boolean clickable =true;

    public TurntableView2(Context context) {
        this(context, null, 0);
    }

    public TurntableView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TurntableView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView( attrs, defStyleAttr);
    }
    private void initView(AttributeSet attrs, int defStyleAttr) {
        initAttrs(attrs, defStyleAttr);
        initPaint();

    }
    private void initAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TurntableView,
                defStyleAttr, 0);
        circleStrokeDimen = (int) a.getDimension(R.styleable.TurntableView_circle_stroke_dimen, dip2px(20));
        mBgColor = a.getColor(R.styleable.TurntableView_bg_color, 0xff5e5d);
        devideLineColor = a.getColor(R.styleable.TurntableView_divide_line_color, 0xff8811);
        deviceLineWidth = (int) a.getDimension(R.styleable.TurntableView_divide_line_width, dip2px(1));
        regionFColor = a.getColor(R.styleable.TurntableView_region_color1, 0xff8586);
        regionSColor = a.getColor(R.styleable.TurntableView_region_color2, 0xfe6869);
        regionTColor = a.getColor(R.styleable.TurntableView_region_color3,0xfd8569);
        int resId = a.getResourceId(R.styleable.TurntableView_btn_drawable, R.mipmap.node);
        mCheckBitmap = BitmapFactory.decodeResource(getResources(), resId);
        textColor = a.getColor(R.styleable.TurntableView_text_color, 0xffffff);
        mTextSize = (int) a.getDimension(R.styleable.TurntableView_text_size, dip2px(16));

        a.recycle();
    }
    private void initPaint() {
        // 初始化绘制圆弧的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);//设置抗锯齿
        mArcPaint.setDither(true);//设置防抖动
        // 初始化绘制文字的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);//设置对齐方式
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);//设置样式
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        //圆形背景的画笔
        mBackColorPaint = new Paint();
        mBackColorPaint.setColor(mBgColor);
        mBackColorPaint.setAntiAlias(true);
        mBackColorPaint.setDither(true);
        //未选中的分割线的画笔
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setStrokeWidth(deviceLineWidth);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(devideLineColor);
    }
    /**
     * 设置控件为正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        int minValue = Math.min(width, height);
        //屏幕的宽度
        setMeasuredDimension(minValue, minValue);
    }
    private float verPanRadius =0;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBackColorWidth = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() -
                getPaddingBottom()) ;
        mCenterX =w/2;
        mCenterY = h/2;

        mRangeWidth = mBackColorWidth - circleStrokeDimen *2;
        // 内圈画盘
        mRange = new RectF(mCenterX - mRangeWidth / 2, mCenterY - mRangeWidth / 2, mCenterX + mRangeWidth / 2, mCenterY + mRangeWidth / 2);
        //中心图片绘制区域
        mCheckBitmapRect = new RectF(mCenterX - mCheckBitmap.getWidth() / 2, mCenterY - mCheckBitmap.getHeight() / 2, mCenterX + mCheckBitmap.getWidth() / 2, mCenterY + mCheckBitmap.getHeight()  / 2);

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
    /**
     * dp转像素
     *
     * @param dpValue
     * @return
     */
    public final int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getContext().getResources().getDisplayMetrics());
    }
    public void setWheelInfos(List<WheelInfo> bitInfos) {
        this.mWheelInfos = bitInfos;
        mItemCount = this.mWheelInfos.size();
        verPanRadius = 360f/mItemCount;
        onDrawInvalidate();
    }
    public void setPostion(int postion){
        checkPosition = postion;
        int tmp = (int) (postion*verPanRadius);
        mStartAngle =   (tmp % 360 + 360) % 360;
        Log.v(TAG,"setPostion mStartAngle " +mStartAngle);
        onDrawInvalidate();
        if(mOnCallBackPosition!=null){
            mOnCallBackPosition.getStopPosition(checkPosition);
        }

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        drawCanvas();
    }
    private void drawCanvas() {
        if (mWheelInfos == null || mWheelInfos.size() == 0) {
            return;
        }
        //绘制背景图
        mCanvas.drawCircle(mCenterX, mCenterY, mBackColorWidth / 2, mBackColorPaint);
        //画前景图片
        mCanvas.drawBitmap(getFontBitmap(), 0, 0, null);
        //绘制中心图片
        mCanvas.rotate(mStartAngle, mCenterX, mCenterY);
        mCanvas.drawBitmap(mCheckBitmap, null, mCheckBitmapRect, null);
    }

    //绘制前景图片,这里包含的是图片信息和文字信息,还有背景圆弧背景展示
    private Bitmap getFontBitmap() {
        fontBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(fontBitmap);
        //根据角度进行同步旋转
        canvas.rotate(0, mCenterX, mCenterY);
        float tmpAngle = 0;
        float sweepAngle = verPanRadius;
        for (int i = 0; i < mItemCount; i++) {
            //这边可以得到新的bitmap
            canvas.drawBitmap(getDrawItemBitmap(tmpAngle, sweepAngle, i), 0, 0, null);
            tmpAngle += sweepAngle;
        }
        return fontBitmap;
    }
    private static final String TAG="TurntableView2";
    /**
     * 两种种状态
     * 0 表示未被选中
     * 1 表示选中
     **/
    private static final int TYPE_UNCHECKED = 0;


    private static final int TYPE_CHECKED = 1;
    /**
     * 对每个选项进行绘制
     **/
    private Bitmap getDrawItemBitmap(float tmpAngle, float sweepAngle, int position) {
        //是否需要重新绘制
        boolean needToNew = false;
        //根据状态判断是否需要重新绘制
        Log.v(TAG, "-----mStartAngle " +mStartAngle);
        int tmpP = calInExactArea(mStartAngle);
        Log.v(TAG, "----tmpP " +tmpP +" position " +position);
        if (tmpP == position && mWheelInfos.get(position).info.bitmapType == TYPE_UNCHECKED) {//这次选中，上次没选中的要更新
            needToNew = true;
            mWheelInfos.get(position).info.bitmapType = TYPE_CHECKED;
        } else if (tmpP != position && mWheelInfos.get(position).info.bitmapType == TYPE_CHECKED) {//这次没选中，上次选中的要更新
            needToNew = true;
            mWheelInfos.get(position).info.bitmapType = TYPE_UNCHECKED;
        }
//        if(position == checkPosition){
//            mWheelInfos.get(checkPosition).info.bitmapType = TYPE_CHECKED;
//        }else{
//            mWheelInfos.get(position).info.bitmapType = TYPE_UNCHECKED;
//        }
        if (mWheelInfos.get(position).info.itemBitmap == null  || needToNew) {
            Log.v(TAG, "getDrawItemBitmap 1111111111");
            //选择背景颜色
//            if(mItemCount%2==0){
//                if (position%2==0) {
//                    mArcPaint.setColor(regionFColor);
//                } else {
//                    mArcPaint.setColor(regionSColor);
//                }
//            }else{
//                if (position%3==0) {
//                    mArcPaint.setColor(regionFColor);
//                }else if (position%3==1) {
//                    mArcPaint.setColor(regionSColor);
//                } else {
//                    mArcPaint.setColor(regionTColor);
//                }
//            }


            if (position==tmpP) {
                    mArcPaint.setColor(regionFColor);
                } else {
                    mArcPaint.setColor(regionSColor);
                }
            //绘制每一个小块
            mWheelInfos.get(position).info.itemBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Log.v(TAG, "tmpAngle " +tmpAngle +  " sweepAngle " +sweepAngle +"  getWidth()" +  getWidth() +"  getHeight() " +getHeight()) ;
            Canvas itemCanvas = new Canvas(mWheelInfos.get(position).info.itemBitmap);
            //根据角度进行同步旋转
            itemCanvas.rotate(tmpAngle, mCenterX, mCenterY);
            //绘制背景颜色,从最上边开始画 原本右侧中间是0度，此处需要上方中间为0度，所以需要-90
            itemCanvas.drawArc(mRange, -sweepAngle / 2 - 90, sweepAngle, true, mArcPaint);
            //绘制小图片和文本，因为一起画好画点
            drawIconAndText(position, itemCanvas);
            //绘制分割线，这里保证没一个小块都有一条分割线，分割线的位置是在最右侧
            drawCanvasLine(itemCanvas);
        } else {
            Log.v(TAG, "getDrawItemBitmap 22222222");
            Canvas itemCanvas = new Canvas(mWheelInfos.get(position).info.itemBitmap);
            //根据角度进行同步旋转
            itemCanvas.rotate(tmpAngle, mCenterX, mCenterY);
        }
        return mWheelInfos.get(position).info.itemBitmap;
    }
    /**
     * 绘制分割线
     **/
    private void drawCanvasLine(Canvas canvas) {
        if (mItemCount == 1) {
            return;
        }
        //计算终点角度，这边从最上边为0度开始计算,则角度在0-180之间，给0.5f的偏移
        float range = verPanRadius / 2 - 0.5f;
        //计算半径的长度
        float radio = (mRange.right - mRange.left) / 2;
        //计算终点坐标
        float x = 0;
        float y = 0;
        if (range < 90) {
            x = (float) (radio * Math.sin(Math.toRadians(range)));
            y = (float) (radio * Math.cos(Math.toRadians(range)));

            x += mCenterX;
            y = mCenterY - y;
        } else {
            x = (float) (radio * Math.sin(Math.toRadians(180 - range)));
            y = (float) (radio * Math.cos(Math.toRadians(180 - range)));

            x += mCenterX;
            y += mCenterY;
        }
        canvas.drawLine(mCenterX, mCenterY, x, y, mLinePaint);
    }

    /**
     * 根据当前旋转的mStartAngle计算当前滚动到的区域
     *
     * @param startAngle
     */
    public int calInExactArea(float startAngle) {
        float size = verPanRadius;
        // 确保rotate是正的，且在0-360度之间
        float rotate = (startAngle % 360.0f + 360.0f) % 360.0f;
        Log.v(TAG, "calInExactArea rotate " +rotate);

        for (int i = 0; i < mItemCount; i++) {
            // 每个的中奖范围
            if (i == 0) {
                if ((rotate > 360 - size / 2) || (rotate <= size / 2)) {
                    return i;
                }
            } else {
                float from = size * (i - 1) + size / 2;
                float to = from + size;
                if ((rotate > from) && (rotate <= to)) {
                    return i;
                }
            }
        }
        return -1;
    }
    public int calInExactArea2(float startAngle) {
        float size = verPanRadius;
        // 确保rotate是正的，且在0-360度之间
        float rotate = (startAngle % 360.0f + 360.0f) % 360.0f;
        Log.v(TAG, "calInExactArea rotate " +rotate);

        for (int i = 0; i < mItemCount; i++) {
            // 每个的中奖范围
            float from = size * (i) ;
            float to = from + size;
            if ((rotate >= from) && (rotate < to)) {
                return i;
            }
        }
        return -1;
    }
    /**
     * 绘制小图片和文字
     *
     * @param i
     */
    private void drawIconAndText(int i, Canvas canvas) {
        //根据的标注，比例为115/730
        float rt = mLitterBitWidth / mRangeWidth;
        //计算绘画区域的直径
        int mRadius = (int) (mRange.right - mRange.left);
        int imgWidth = (int) (mRadius * rt);
        //获取中心点坐标
        int x = mCenterX;
        //这边让图片从四分之一出开始画
        int y = (int) (mCenterY - mRadius / 2 + (float) mRadius / 2 * 1 / 4f);
        //确定小图片的区域
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth
                / 2, y + imgWidth / 2);
        //将图片画上去
        canvas.drawBitmap(mWheelInfos.get(i).bitmap, null, rect, null);
        //绘制文本
        if (!TextUtils.isEmpty(mWheelInfos.get(i).text)) {
            //最大字数限制为8个字
            if (mWheelInfos.get(i).text.length() > 8) {
                mWheelInfos.get(i).text = mWheelInfos.get(i).text.substring(0, 8);
            }
            StaticLayout textLayout = new StaticLayout(mWheelInfos.get(i).text, mTextPaint, imgWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 0, false);
            canvas.translate(mCenterX, rect.bottom + dip2px(2));
            textLayout.draw(canvas);
            //画完之后移动回来
            canvas.translate(-mCenterX, -(rect.bottom + dip2px(2)));
        }
    }
    /**
     * 点击事件，点击中心就开始动画
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!clickable){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                //点击了中间位置
                if(x>mCheckBitmapRect.left&&x<mCheckBitmapRect.right&&y>mCheckBitmapRect.top&&y<mCheckBitmapRect.bottom){
                    startAnimationByPostion(checkPosition);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }
    private static final long ONE_WHEEL_TIME = 500;

    //旋转的动画
    public void startAnimationByPostion(int pos){
        float size = verPanRadius;
        //Rotate lap. 随机的圈数，>4圈，修改可以控制旋转的圈数和时长
        int lap = (int) (Math.random()*2) + 4;

        //Rotate angle.
        int angle = 0;
        Log.v(TAG, "startAnimationByPostion pos " +pos);
        angle = (int) (Math.random() * 360);

        //All of the rotate angle.
        int increaseDegree = lap * 360 + angle;
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;
        float DesRotate =  (increaseDegree + mStartAngle);

        //TODO 为了每次都能旋转到转盘的中间位置//zfuyan
//        float offRotate =  (DesRotate % 360 % size);
//        DesRotate -= offRotate;
//        DesRotate += size/2;
        Log.v(TAG, "DesRotate11== " +DesRotate);
        if((DesRotate % 360 % size) ==0f){
            DesRotate+=10;
        }
        Log.v(TAG, "DesRotate222== " +DesRotate);
        //属性动画
        ValueAnimator animtor = ValueAnimator.ofFloat((int) mStartAngle,DesRotate);
        animtor.setInterpolator(new AccelerateDecelerateInterpolator());
        animtor.setDuration(time);
        animtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float updateValue = (float) animation.getAnimatedValue();
                //每次绘制的初始值改变，最终会停止到之前设置的角度数值
                mStartAngle = (updateValue % 360 + 360) % 360;
                //重绘制
                ViewCompat.postInvalidateOnAnimation(TurntableView2.this);
            }
        });
        //动画监听，获取到具体的停留位置，这里处理随机位置，其实提前是知道的
        animtor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                float tmp =mStartAngle;

                Log.v(TAG, "mStartAngle " +mStartAngle +" tmp " +tmp);
                checkPosition = calInExactArea(tmp);
                Log.v(TAG, "onAnimationEnd checkPosition " +checkPosition );
                if(mOnCallBackPosition!=null){
                    mOnCallBackPosition.getStopPosition(checkPosition);
                }
                clickable =true;

            }
        });
        animtor.start();
        clickable =false;
    }


    /**
     * 刷新画布
     **/
    private void onDrawInvalidate() {
        synchronized (obj) {
            invalidate();
        }
    }

    public int getCheckPosition() {
        return checkPosition;
    }

    //回调传值
    public void setOnCallBackPosition( onCallBackPosition mOnCallBackPosition){
        this.mOnCallBackPosition=mOnCallBackPosition;
    }
    private onCallBackPosition mOnCallBackPosition;
    interface onCallBackPosition{
        void getStopPosition(int pos);
    }
}


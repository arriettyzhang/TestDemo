package com.oceawing.adaption.multitouchdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyView extends SurfaceView implements SurfaceHolder.Callback {
    private static final int MAX_TOUCHPOINTS = 10;
    private static final String TAG ="MyView";

    private static final String START_TEXT = "请随便触摸屏幕进行测试";

    private Paint textPaint = new Paint();

    private Paint touchPaints[] = new Paint[MAX_TOUCHPOINTS];
    private int colors[] = new int[MAX_TOUCHPOINTS];
    private int width, height;
    private float scale = 1.0f;
    public MyView(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true); // 确保我们的View能获得输入焦点
        setFocusableInTouchMode(true); // 确保能接收到触屏事件
        init();
    }

    private void init() {
        // 初始化10个不同颜色的画笔
        textPaint.setColor(Color.WHITE);
        colors[0] = Color.BLUE;
        colors[1] = Color.RED;
        colors[2] = Color.GREEN;
        colors[3] = Color.YELLOW;
        colors[4] = Color.CYAN;
        colors[5] = Color.MAGENTA;
        colors[6] = Color.DKGRAY;
        colors[7] = Color.WHITE;
        colors[8] = Color.LTGRAY;
        colors[9] = Color.GRAY;
        for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
            touchPaints[i] = new Paint();
            touchPaints[i].setColor(colors[i]);
        }
    }

    /**
     * 处理触屏事件
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {

       int index =  event.getActionIndex();
        int actionId = event.getPointerId(index);
        int j = event.findPointerIndex(actionId);
//        Log.e(TAG, "action indext "+ index +" actionId " +actionId +"  find index " +j);
        // 获得屏幕触点数量
        int pointerCount = event.getPointerCount();
        Log.v(TAG, "onTouchEvent getActionMasked " +event.getActionMasked() );
        Log.v(TAG, "onTouchEvent event " +event.getAction() +"  pointerCount " +pointerCount);
        if (pointerCount > MAX_TOUCHPOINTS) {
            pointerCount = MAX_TOUCHPOINTS;
        }
        // 锁定Canvas,开始进行相应的界面处理
        Canvas c = getHolder().lockCanvas();
        if (c != null) {
            c.drawColor(Color.BLACK);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // 当手离开屏幕时，清屏
            } else {
                // 在每一个触点上绘制一个十字和坐标信息
                for (int i = 0; i < pointerCount; i++) {
                    int id = event.getPointerId(i);
                    int x = (int) event.getX(i);
                    int y = (int) event.getY(i);
                    drawCrosshairsAndText(x, y, touchPaints[id], i, id, c);
                }
                // 在每一个触点上绘制一个圆
                for (int i = 0; i < pointerCount; i++) {
                    int id = event.getPointerId(i);
                    int x = (int) event.getX(i);
                    int y = (int) event.getY(i);
                    drawCircle(x, y, touchPaints[id], c);
                }
            }
            // 画完后，unlock
            getHolder().unlockCanvasAndPost(c);

        }
        return true;
    }

    /**
     * 画十字及坐标信息
     *
     * @param x
     * @param y
     * @param paint
     * @param ptr
     * @param id
     * @param c
     */

    private void drawCrosshairsAndText(int x, int y, Paint paint, int ptr, int id, Canvas c) {
        Log.v(TAG, "x " +x+" y " +y +" id "+id);
        c.drawLine(0, y, width, y, paint);
        c.drawLine(x, 0, x, height, paint);
        int textY = (int) ((15 + 20 * ptr) * scale);
        c.drawText("x"
                + ptr + "="
                + x, 10
                * scale, textY, textPaint);

        c.drawText("y"
                + ptr + "="
                + y, 70
                * scale, textY, textPaint);

        c.drawText("id"
                + ptr + "="
                + id, width - 55
                * scale, textY, textPaint);
    }

    /**
     * 画圆
     *
     * @param x
     * @param y
     * @param paint
     * @param c
     */

    private void drawCircle(int x, int y, Paint paint, Canvas c) {
        c.drawCircle(x, y, 40 * scale, paint);
    }


    /**
     * 进入程序时背景画成黑色，然后把START_TEXT写到屏幕
     */

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, "surfaceChanged width "+width +" height " +height);
        this.width = width;
        this.height = height;
        if (width > height) {
            this.scale = width / 480f;
        } else {
            this.scale = height / 480f;

        }
        textPaint.setTextSize(14 * scale);
        Canvas c = getHolder().lockCanvas();
        if (c != null) {
            c.drawColor(Color.BLACK);
            float tWidth = textPaint.measureText(START_TEXT);
            c.drawText(START_TEXT, width / 2 - tWidth / 2, height / 2, textPaint);
            getHolder().unlockCanvasAndPost(c);
        }

    }
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(TAG, "surfaceCreated ");
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "surfaceDestroyed ");
    }
    void printSamples(MotionEvent ev) {
        final int historySize = ev.getHistorySize();
        final int pointerCount = ev.getPointerCount();
        for (int h = 0; h < historySize; h++) {
            System.out.printf("At time %d:", ev.getHistoricalEventTime(h));
            for (int p = 0; p < pointerCount; p++) {
                System.out.printf("  pointer %d: (%f,%f)",
                        ev.getPointerId(p), ev.getHistoricalX(p, h), ev.getHistoricalY(p, h));
            }
        }
        System.out.printf("At time %d:", ev.getEventTime());
        for (int p = 0; p < pointerCount; p++) {
            System.out.printf("  pointer %d: (%f,%f)",
                    ev.getPointerId(p), ev.getX(p), ev.getY(p));
        }
    }
}
package com.example.arrietty.demoapp.spandemo;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;


/**
 * Created by ocean on 2017/5/20.
 */

public class ColorUnderlineClickableSpan extends ClickableSpan {

    private int color;

    public ColorUnderlineClickableSpan(Context context, int colorId) {
        super();
        color = context.getResources().getColor(colorId);
    }

    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color);
        ds.setUnderlineText(true);
    }

}

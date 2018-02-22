package com.example.arrietty.demoapp.recyclelist;

/**
 * Created by asus on 2017/12/6.
 */

public class BeanMode {
    private int drawableId;
    private String text;

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "BeanMode{" +
                "drawableId=" + drawableId +
                ", text=" + text +
                '}';
    }
}

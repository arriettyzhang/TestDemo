package com.example.arrietty.demoapp;

/**
 * Created by asus on 2017/11/13.
 */

public class TestBackPress extends MainActivity{
    private int index=10;
    @Override
    public void onBackPressed() {
        if(index>0){
            index--;
        }else{
            super.onBackPressed();
        }


    }

}

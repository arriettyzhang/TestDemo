package com.example.arrietty.demoapp.utils;

/**
 * Created by asus on 2018/3/21.
 */

public class APP {
    public static APP instance;
    private boolean enablaWifi;
    private boolean wifiConnected;
    private boolean mobileConnected;
    private boolean networkConnected;
    public synchronized static APP getInstance(){
        if(instance == null){
            instance = new APP();
        }
        return instance;
    }
    public  void setEnablaWifi(boolean enablaWifi){
        this.enablaWifi = enablaWifi;
    }
    public void setWifi(boolean connected){
        wifiConnected = connected;
    }
    public void setMobile(boolean connected){
        mobileConnected = connected;
    }
    public void setConnected(boolean connected){
        networkConnected = connected;
    }

}

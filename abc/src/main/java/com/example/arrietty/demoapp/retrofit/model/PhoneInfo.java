package com.example.arrietty.demoapp.retrofit.model;

/**
 * Created by asus on 2017/12/14.
 */

public class PhoneInfo {
    private String ower;
    private String number;
    private String psw;


    public String getOwer() {
        return ower;
    }

    public void setOwer(String ower) {
        this.ower = ower;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    @Override
    public String toString() {
        return "PhoneInfo{" +
                "ower='" + ower + '\'' +
                ", number='" + number + '\'' +
                ", psw='" + psw + '\'' +
                '}';
    }
}

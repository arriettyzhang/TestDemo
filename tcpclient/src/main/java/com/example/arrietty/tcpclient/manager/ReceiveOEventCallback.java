package com.example.arrietty.tcpclient.manager;


import com.example.arrietty.tcpclient.model.AudioInfoBean;
import com.example.arrietty.tcpclient.model.DeviceInfoBean;

/**
 * Created by asus on 2018/2/5.
 */

public interface ReceiveOEventCallback {
    //adaption
    void setAdaptionPreEQRsp(boolean success, int errcode);

    //heart beater
    void getHeartBeaterIsNormalRsp(boolean success, int errcode);
}

package com.example.arrietty.tcpclient.manager;

import com.example.arrietty.tcpclient.model.DeviceInfoBean;

/**
 * Created by asus on 2018/2/6.
 */

public interface ReceiveDIEventCallback {
    //device info
    void getDeviceInfoRsp(DeviceInfoBean info);
    void getDeviceNameRsp(DeviceInfoBean info);
    void getDeviceBatteryRsp(DeviceInfoBean info);
    void getDeviceVolumeRsp(DeviceInfoBean info);
    void setDevicevolumeRsp(boolean success, int errcode);
    void getDevicePlayStateRsp(DeviceInfoBean info);
    void setDevicePlayStateRsp(boolean success, int errcode);
    void getDeviceFirmwareVersionRsp(DeviceInfoBean info);
    void getDeviceSNRsp(DeviceInfoBean info);
    void getDeviceMACRsp(DeviceInfoBean info);

    //device info report
    void reportDeviceBattety(DeviceInfoBean info);
    void reportDeviceVolume(DeviceInfoBean info);
    void reportDevicePlayState(DeviceInfoBean info);
}

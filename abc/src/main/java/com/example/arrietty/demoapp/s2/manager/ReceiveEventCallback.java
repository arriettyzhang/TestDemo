package com.example.arrietty.demoapp.s2.manager;

import com.example.arrietty.demoapp.s2.model.AudioInfoBean;
import com.example.arrietty.demoapp.s2.model.DeviceInfoBean;

/**
 * Created by asus on 2018/2/5.
 */

public interface ReceiveEventCallback {
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


    //audio
    void getAudioInfoRsp(AudioInfoBean info);
    void getAudioEqIndexRsp(AudioInfoBean info);
    void setAudioEqIndexRsp(boolean success, int errcode);
    void getAudioDolbyAudioRsp(AudioInfoBean info);
    void setAudioDolbyAudioRsp(boolean success, int errcode);
    void getAudioSurroundRsp(AudioInfoBean info);
    void setAudioSurroundRsp(boolean success, int errcode);
    //audio report
    void reportAudioDolbyAudioRsp(AudioInfoBean info);

    //adaption
    void setAdaptionPreEQRsp(boolean success, int errcode);

    //heart beater
    void getHeartBeaterIsNormalRsp(boolean success, int errcode);





}

package com.example.arrietty.tcpclient.manager;

import com.example.arrietty.tcpclient.model.AudioInfoBean;

/**
 * Created by asus on 2018/2/6.
 */

public interface ReceiveAIEventCallback {
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
}

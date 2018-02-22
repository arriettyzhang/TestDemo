package com.example.arrietty.tcpclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.arrietty.tcpclient.manager.DeviceManager;
import com.example.arrietty.tcpclient.manager.ReceiveAIEventCallback;
import com.example.arrietty.tcpclient.manager.ReceiveDIEventCallback;
import com.example.arrietty.tcpclient.manager.ReceiveOEventCallback;
import com.example.arrietty.tcpclient.model.AudioInfoBean;
import com.example.arrietty.tcpclient.model.DeviceInfoBean;
import com.example.arrietty.tcpclient.tcp.TcpClient;
import com.example.arrietty.tcpclient.tcp.TcpLinkCallback;


/**
 * Created by asus on 2018/2/2.
 */

public class S2MainActivity extends AppCompatActivity implements TcpLinkCallback, ReceiveDIEventCallback
        ,ReceiveAIEventCallback, ReceiveOEventCallback{
    private static final String TAG ="S2.S2MainActivity";
    String IP ="10.1.116.176";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);

        DeviceManager.getInstance().addTcpLinkCallbackListener(this);
        DeviceManager.getInstance().addReceiveDIEventCallbackListener(this);
        DeviceManager.getInstance().addReceiveAIEventCallbackListener(this);
        DeviceManager.getInstance().addReceiveOEventCallbackListener(this);
        DeviceManager.getInstance().initLink(IP,DeviceManager.PORT);
    }

    public void ButtonClick(View v) {
        switch (v.getId()){
            case R.id.connect:
                //startActivity(new Intent(S2MainActivity.this, SubActivity.class));
                DeviceManager.getInstance().initLink(IP,DeviceManager.PORT);
                break;
            case R.id.disconnect:
                DeviceManager.getInstance().destroy();
                break;
            case R.id.getDeviceInfo:
                Log.v(TAG, "on clock getDeviceInfo btn");
                DeviceManager.getInstance().getDeviceInfo();
                break;

            case R.id.getDeviceName:
                DeviceManager.getInstance().getDeviceName();
                break;
            case R.id.getDeviceBattery:
                DeviceManager.getInstance().getDeviceBattery();
                break;
            case R.id.reportDeviceBatteryAck:
                DeviceManager.getInstance().reportDeviceBatteryAck(true);
                break;
            case R.id.getDeviceVolume:
                DeviceManager.getInstance().getDeviceVolume();
                break;
            case R.id.setDeviceVolume:
                DeviceManager.getInstance().setDeviceVolume(5);
                break;
            case R.id.reportDeviceVolumeAck:
                DeviceManager.getInstance().reportDeviceVolumeAck(true);
                break;
            case R.id.getDevicePlayState:
                DeviceManager.getInstance().getDevicePlayState();
                break;
            case R.id.setDevicePlayState:
                DeviceManager.getInstance().setDevicePlayState(1);
                break;
            case R.id.reportDevicePlayStateAck:
                DeviceManager.getInstance().reportDevicePlayStateAck(true);
                break;
            case R.id.getDeviceFirmwareVersion:
                DeviceManager.getInstance().getDeviceFirmwareVersion();
                break;
            case R.id.getDeviceSN:
                DeviceManager.getInstance().getDeviceSN();
                break;
            case R.id.getDeviceMAC:
                DeviceManager.getInstance().getDeviceMAC();
                break;
            case R.id.getAudioInfo:
                DeviceManager.getInstance().getAudioInfo();
                break;
            case R.id.getAudioEQIndex:
                DeviceManager.getInstance().getAudioEQIndex();
                break;
            case R.id.setAudioEQIndex:
                DeviceManager.getInstance().setAudioEQIndex(1);
                break;
            case R.id.getAudioDolby:
                DeviceManager.getInstance().getAudioDolby();
                break;
            case R.id.setAudioDolby:
                DeviceManager.getInstance().setAudioDolby(true);
                break;
            case R.id.reportAudioDolbyAck:
                DeviceManager.getInstance().reportAudioDolbyAck(true);
                break;
            case R.id.getAudioSurrond:
                DeviceManager.getInstance().getAudioSurrond();
                break;
            case R.id.setAudioSurrond:
                DeviceManager.getInstance().setAudioSurrond(true);
                break;
            case R.id.setAdaptionPreEQ:
                DeviceManager.getInstance().setAdaptionPreEQ(2);
                break;
            case R.id.getHeartBeaterIsNormal:
                DeviceManager.getInstance().getHeartBeaterIsNormal();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DeviceManager.getInstance().removeReceiveDIEventCallbackListener(this);
        DeviceManager.getInstance().removeReceiveAIEventCallbackListener(this);
        DeviceManager.getInstance().removeReceiveOEventCallbackListener(this);
        DeviceManager.getInstance().removeTcpLinkCallbackListener(this);
        DeviceManager.getInstance().destroy();
    }

    @Override
    public void onTcpConnectSuccess() {
        Log.v(TAG, "onTcpConnectSuccess");

    }

    @Override
    public void onTcpClose(int error) {
        Log.v(TAG, "onTcpClose error" +error);

    }

    @Override
    public void getDeviceInfoRsp(DeviceInfoBean info) {
        Log.v(TAG, "getDeviceInfoRsp");

    }

    @Override
    public void getDeviceNameRsp(DeviceInfoBean info) {
        Log.v(TAG, "getDeviceNameRsp");
    }

    @Override
    public void getDeviceBatteryRsp(DeviceInfoBean info) {
        Log.v(TAG, "getDeviceBatteryRsp");

    }

    @Override
    public void getDeviceVolumeRsp(DeviceInfoBean info) {
        Log.v(TAG, "getDeviceVolumeRsp");
    }

    @Override
    public void setDevicevolumeRsp(boolean success, int errcode) {
        Log.v(TAG, "setDevicevolumeRsp");
    }

    @Override
    public void getDevicePlayStateRsp(DeviceInfoBean info) {
        Log.v(TAG, "getDevicePlayStateRsp");
    }

    @Override
    public void setDevicePlayStateRsp(boolean success, int errcode) {
        Log.v(TAG, "setDevicePlayStateRsp");
    }

    @Override
    public void getDeviceFirmwareVersionRsp(DeviceInfoBean info) {
        Log.v(TAG, "getDeviceFirmwareVersionRsp");
    }

    @Override
    public void getDeviceSNRsp(DeviceInfoBean info) {
        Log.v(TAG, "getDeviceSNRsp");
    }

    @Override
    public void getDeviceMACRsp(DeviceInfoBean info) {
        Log.v(TAG, "getDeviceMACRsp");
    }

    @Override
    public void reportDeviceBattety(DeviceInfoBean info) {
        Log.v(TAG, "reportDeviceBattety");
    }

    @Override
    public void reportDeviceVolume(DeviceInfoBean info) {
        Log.v(TAG, "reportDeviceVolume");
    }

    @Override
    public void reportDevicePlayState(DeviceInfoBean info) {
        Log.v(TAG, "reportDevicePlayState");
    }

    @Override
    public void getAudioInfoRsp(AudioInfoBean info) {
        Log.v(TAG, "getAudioInfoRsp");
    }

    @Override
    public void getAudioEqIndexRsp(AudioInfoBean info) {
        Log.v(TAG, "getAudioEqIndexRsp");
    }

    @Override
    public void setAudioEqIndexRsp(boolean success, int errcode) {
        Log.v(TAG, "setAudioEqIndexRsp");
    }

    @Override
    public void getAudioDolbyAudioRsp(AudioInfoBean info) {
        Log.v(TAG, "getAudioDolbyAudioRsp");
    }

    @Override
    public void setAudioDolbyAudioRsp(boolean success, int errcode) {
        Log.v(TAG, "setAudioDolbyAudioRsp");
    }

    @Override
    public void getAudioSurroundRsp(AudioInfoBean info) {
        Log.v(TAG, "getAudioSurroundRsp");
    }

    @Override
    public void setAudioSurroundRsp(boolean success, int errcode) {
        Log.v(TAG, "setAudioSurroundRsp");
    }

    @Override
    public void reportAudioDolbyAudioRsp(AudioInfoBean info) {
        Log.v(TAG, "reportAudioDolbyAudioRsp");
    }

    @Override
    public void setAdaptionPreEQRsp(boolean success, int errcode) {
        Log.v(TAG, "setAdaptionPreEQRsp");
    }

    @Override
    public void getHeartBeaterIsNormalRsp(boolean success, int errcode) {
        Log.v(TAG, "getHeartBeaterIsNormalRsp");
    }
}

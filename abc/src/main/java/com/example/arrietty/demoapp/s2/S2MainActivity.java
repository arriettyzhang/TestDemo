package com.example.arrietty.demoapp.s2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.arrietty.demoapp.MainActivity;
import com.example.arrietty.demoapp.R;
import com.example.arrietty.demoapp.s2.manager.DeviceManager;
import com.example.arrietty.demoapp.s2.manager.ReceiveEventCallback;
import com.example.arrietty.demoapp.s2.model.AudioInfoBean;
import com.example.arrietty.demoapp.s2.model.DeviceInfoBean;
import com.example.arrietty.demoapp.s2.tcp.TcpClient;
import com.example.arrietty.demoapp.s2.tcp.TcpLinkCallback;

/**
 * Created by asus on 2018/2/2.
 */

public class S2MainActivity extends AppCompatActivity implements TcpLinkCallback, ReceiveEventCallback {
    private static final String TAG ="S2.S2MainActivity";
    private TcpClient client;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);
        String IP ="10.1.116.176";
        DeviceManager.getInstance().addTcpLinkCallbackListener(this);
        DeviceManager.getInstance().addReceiveEventCallbackListener(this);
        DeviceManager.getInstance().initLink(IP,5858);
    }

    public void ButtonClick(View v) {
        switch (v.getId()){
            case R.id.connect:
                startActivity(new Intent(S2MainActivity.this, SubActivity.class));
                break;
            case R.id.disconnect:
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
        DeviceManager.getInstance().removeReceiveEventCallbackListener(this);
        DeviceManager.getInstance().removeTcpLinkCallbackListener(this);
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

    }

    @Override
    public void getDeviceNameRsp(DeviceInfoBean info) {

    }

    @Override
    public void getDeviceBatteryRsp(DeviceInfoBean info) {


    }

    @Override
    public void getDeviceVolumeRsp(DeviceInfoBean info) {

    }

    @Override
    public void setDevicevolumeRsp(boolean success, int errcode) {

    }

    @Override
    public void getDevicePlayStateRsp(DeviceInfoBean info) {

    }

    @Override
    public void setDevicePlayStateRsp(boolean success, int errcode) {

    }

    @Override
    public void getDeviceFirmwareVersionRsp(DeviceInfoBean info) {

    }

    @Override
    public void getDeviceSNRsp(DeviceInfoBean info) {

    }

    @Override
    public void getDeviceMACRsp(DeviceInfoBean info) {

    }

    @Override
    public void reportDeviceBattety(DeviceInfoBean info) {

    }

    @Override
    public void reportDeviceVolume(DeviceInfoBean info) {

    }

    @Override
    public void reportDevicePlayState(DeviceInfoBean info) {

    }

    @Override
    public void getAudioInfoRsp(AudioInfoBean info) {

    }

    @Override
    public void getAudioEqIndexRsp(AudioInfoBean info) {

    }

    @Override
    public void setAudioEqIndexRsp(boolean success, int errcode) {

    }

    @Override
    public void getAudioDolbyAudioRsp(AudioInfoBean info) {

    }

    @Override
    public void setAudioDolbyAudioRsp(boolean success, int errcode) {

    }

    @Override
    public void getAudioSurroundRsp(AudioInfoBean info) {

    }

    @Override
    public void setAudioSurroundRsp(boolean success, int errcode) {

    }

    @Override
    public void reportAudioDolbyAudioRsp(AudioInfoBean info) {

    }

    @Override
    public void setAdaptionPreEQRsp(boolean success, int errcode) {

    }

    @Override
    public void getHeartBeaterIsNormalRsp(boolean success, int errcode) {

    }
}

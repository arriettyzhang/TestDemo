package com.example.arrietty.tcpclient.manager;

import android.util.Log;

import com.example.arrietty.tcpclient.CmdConstants;
import com.example.arrietty.tcpclient.model.AudioInfoBean;
import com.example.arrietty.tcpclient.model.DeviceInfoBean;
import com.example.arrietty.tcpclient.model.ReceiveBeanParent;
import com.example.arrietty.tcpclient.tcp.SocketReceiver;
import com.example.arrietty.tcpclient.tcp.TcpClient;
import com.example.arrietty.tcpclient.tcp.TcpLinkCallback;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by asus on 2018/2/2.
 */

public class DeviceManager implements SocketReceiver {
    private static final String TAG ="S2.DeviceManager";
    private static DeviceManager instance = null;
    public final List<ReceiveDIEventCallback> mReceiveDIEventCallback;
    public final List<ReceiveAIEventCallback> mReceiveAIEventCallback;
    public final List<ReceiveOEventCallback> mReceiveOEventCallback;
    public final List<TcpLinkCallback> mTcpLinkCallback;
    public static final int PORT = 5858;
    private TcpClient tcpClient;
    private DeviceManager(){
        mReceiveDIEventCallback = Collections.synchronizedList(new ArrayList<ReceiveDIEventCallback>());
        mReceiveAIEventCallback = Collections.synchronizedList(new ArrayList<ReceiveAIEventCallback>());
        mReceiveOEventCallback = Collections.synchronizedList(new ArrayList<ReceiveOEventCallback>());
        mTcpLinkCallback = Collections.synchronizedList(new ArrayList<TcpLinkCallback>());
    }
    public static DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }
    public void initLink(String ip, int port){
        if(tcpClient!=null && tcpClient.isConnected()){
            return;
        }
        tcpClient= new TcpClient(new TcpLinkCallback() {
            @Override
            public void onTcpConnectSuccess() {
                Log.e(TAG, "onTcpConnectSuccess " );
                notifyTcpLinkCallback(true, 0);
            }

            @Override
            public void onTcpClose(int error) {
                Log.e(TAG, "onTcpClose error" + error);
                notifyTcpLinkCallback(false, error);

            }
        }, ip, port);
        tcpClient.connect();
        DeviceCommand.getInstance().setReceverInterface(this);
    }
    public void destroy(){
        DeviceCommand.getInstance().closeTcpClient(tcpClient);
        tcpClient = null;
    }
    /**
     * 注册监听tcp 连接情况
     *
     * @param listener
     */
    public void addTcpLinkCallbackListener(TcpLinkCallback listener) {
        mTcpLinkCallback.add(listener);
    }

    /**
     * 取消监听tcp 连接情况
     *
     * @param listener
     */
    public void removeTcpLinkCallbackListener(TcpLinkCallback listener) {
        mTcpLinkCallback.remove(listener);
    }
    public void notifyTcpLinkCallback(boolean success, int errCode){
        for (TcpLinkCallback mc : mTcpLinkCallback) {
            if(success){
                mc.onTcpConnectSuccess();
            }else{
                mc.onTcpClose(errCode);
            }
        }
    }
    /**
     * 注册监听tcp receive event
     *
     * @param listener
     */
    public void addReceiveDIEventCallbackListener(ReceiveDIEventCallback listener) {
        mReceiveDIEventCallback.add(listener);
    }
    public void addReceiveAIEventCallbackListener(ReceiveAIEventCallback listener) {
        mReceiveAIEventCallback.add(listener);
    }
    public void addReceiveOEventCallbackListener(ReceiveOEventCallback listener) {
        mReceiveOEventCallback.add(listener);
    }

    /**
     * 取消注册监听tcp receive event
     *
     * @param listener
     */

    public void removeReceiveDIEventCallbackListener(ReceiveDIEventCallback listener) {
        mReceiveDIEventCallback.remove(listener);
    }
    public void removeReceiveAIEventCallbackListener(ReceiveAIEventCallback listener) {
        mReceiveAIEventCallback.remove(listener);
    }
    public void removeReceiveOEventCallbackListener(ReceiveOEventCallback listener) {
        mReceiveOEventCallback.remove(listener);
    }
    public void getDeviceInfoCallback(DeviceInfoBean infoBean, int cmdId) {
        for (ReceiveDIEventCallback mc : mReceiveDIEventCallback) {
            if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_INFO){
                mc.getDeviceInfoRsp(infoBean);
                Log.v(TAG, "getDeviceInfoRsp");
            }else if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_NAME){
                mc.getDeviceNameRsp(infoBean);
                Log.v(TAG, "getDeviceNameRsp");
            }else if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_BATTERY){
                mc.getDeviceBatteryRsp(infoBean);
                Log.v(TAG, "getDeviceBatteryRsp");
            }else if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_VOLUME){
                mc.getDeviceVolumeRsp(infoBean);
                Log.v(TAG, "getDeviceVolumeRsp");
            }else if(cmdId == CmdConstants.CMD_ID_SET_DEVICE_VOLUME){
                mc.setDevicevolumeRsp(infoBean.getSuccessFlag()==CmdConstants.SUCCESS_CODE?true:false,
                        infoBean.getSuccessFlag());
                Log.v(TAG, "setDevicevolumeRsp");
            }else if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_PLAY_STATE){
                mc.getDevicePlayStateRsp(infoBean);
                Log.v(TAG, "getDevicePlayStateRsp");
            }else if(cmdId == CmdConstants.CMD_ID_SET_DEVICE_PLAY_STATE){
                mc.setDevicePlayStateRsp(infoBean.getSuccessFlag()==CmdConstants.SUCCESS_CODE?true:false,
                        infoBean.getSuccessFlag());
                Log.v(TAG, "setDevicePlayStateRsp");
            }else if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_FIRMWARE_VERSION){
                mc.getDeviceFirmwareVersionRsp(infoBean);
                Log.v(TAG, "getDeviceFirmwareVersionRsp");
            }else if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_SN){
                mc.getDeviceSNRsp(infoBean);
                Log.v(TAG, "getDeviceSNRsp");
            }else if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_MAC){
                mc.getDeviceMACRsp(infoBean);
                Log.v(TAG, "getDeviceMACRsp");
            }
        }
    }
    public void getAudioInfoCallback(AudioInfoBean infoBean, int cmdId) {
        for (ReceiveAIEventCallback mc : mReceiveAIEventCallback) {
            if (cmdId == CmdConstants.CMD_ID_GET_AUDIO_INFO) {
                mc.getAudioInfoRsp(infoBean);
                Log.v(TAG, "getAudioInfoRsp");
            } else if (cmdId == CmdConstants.CMD_ID_GET_AUDIO_EQ_INDEX) {
                mc.getAudioEqIndexRsp(infoBean);
                Log.v(TAG, "getAudioEqIndexRsp");
            }else if (cmdId == CmdConstants.CMD_ID_SET_AUDIO_EQ_INDEX) {
                mc.setAudioEqIndexRsp(infoBean.getSuccessFlag()==CmdConstants.SUCCESS_CODE?true:false,
                        infoBean.getSuccessFlag());
                Log.v(TAG, "setAudioEqIndexRsp");
            }else if (cmdId == CmdConstants.CMD_ID_GET_AUDIO_DOLBY_AUDIO) {
                mc.getAudioDolbyAudioRsp(infoBean);
                Log.v(TAG, "getAudioDolbyAudioRsp");
            }else if (cmdId == CmdConstants.CMD_ID_SET_AUDIO_DOLBY_AUDIO) {
                mc.setAudioDolbyAudioRsp(infoBean.getSuccessFlag()==CmdConstants.SUCCESS_CODE?true:false,
                        infoBean.getSuccessFlag());
                Log.v(TAG, "setAudioDolbyAudioRsp");
            }else if (cmdId == CmdConstants.CMD_ID_GET_AUDIO_SURROUND) {
                mc.getAudioSurroundRsp(infoBean);
                Log.v(TAG, "getAudioSurroundRsp");
            }else if (cmdId == CmdConstants.CMD_ID_SET_AUDIO_SURROUND) {
                mc.setAudioSurroundRsp(infoBean.getSuccessFlag()==CmdConstants.SUCCESS_CODE?true:false,
                        infoBean.getSuccessFlag());
                Log.v(TAG, "setAudioSurroundRsp");
            }
        }
    }
    public void setAdaptionInfoCallback(ReceiveBeanParent infoBean, int cmdId) {
        for (ReceiveOEventCallback mc : mReceiveOEventCallback) {
            if (cmdId == CmdConstants.CMD_ID_SET_ADAPTION) {
                mc.setAdaptionPreEQRsp(infoBean.getSuccessFlag() == CmdConstants.SUCCESS_CODE ? true : false,
                        infoBean.getSuccessFlag());
                Log.v(TAG, "setAdaptionPreEQRsp");
            }
        }
    }
    public void getHeartBeaterIsNormalCallback(ReceiveBeanParent infoBean, int cmdId){
        for (ReceiveOEventCallback mc : mReceiveOEventCallback) {
            if (cmdId == CmdConstants.CMD_ID_GET_HEART_BEATER_IS_NORMAL) {
                mc.getHeartBeaterIsNormalRsp(infoBean.getSuccessFlag() == CmdConstants.SUCCESS_CODE ? true : false,
                        infoBean.getSuccessFlag());
                Log.v(TAG, "getHeartBeaterIsNormalRsp");
            }
        }
    }
    public void reportDeviceInfoCallback(DeviceInfoBean infoBean, int cmdId) {
        for (ReceiveDIEventCallback mc : mReceiveDIEventCallback) {
            if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_BATTERY){
                mc.reportDeviceBattety(infoBean);
                Log.v(TAG, "reportDeviceBattety");
            }else if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_VOLUME){
                mc.reportDeviceVolume(infoBean);
                Log.v(TAG, "reportDeviceVolume");
            }else if(cmdId == CmdConstants.CMD_ID_GET_DEVICE_PLAY_STATE){
                mc.reportDevicePlayState(infoBean);
                Log.v(TAG, "reportDevicePlayState");
            }
        }
    }
    public void reportAudioInfoCallback(AudioInfoBean infoBean, int cmdId) {
        for (ReceiveAIEventCallback mc : mReceiveAIEventCallback) {
            if(cmdId == CmdConstants.CMD_ID_GET_AUDIO_DOLBY_AUDIO){
                mc.reportAudioDolbyAudioRsp(infoBean);
                Log.v(TAG, "reportAudioDolbyAudioRsp");
            }
        }
    }
    @Override
    public void OnReciver(String action, Object obj) {

        Gson g = new Gson();
        if(CmdConstants.ACTION_DEVICE_ACK_APP.equals(action)){
            String receive =(String)obj;
            ReceiveBeanParent responseObj = g.fromJson(receive, ReceiveBeanParent.class);
            int cmdGroup = responseObj.getCmdGroup();
            int cmdId = responseObj.getCmdId();
            if(cmdGroup== CmdConstants.CMD_GROUP_DEVICE_INFO){
                getDeviceInfoCallback((DeviceInfoBean)responseObj,cmdId);

            }else if(cmdGroup== CmdConstants.CMD_GROUP_AUDIO_INFO){
                getAudioInfoCallback((AudioInfoBean)responseObj,cmdId);
            }else if(cmdGroup== CmdConstants.CMD_GROUP_SELF_ADAPTION){
                setAdaptionInfoCallback(responseObj, cmdId);
            }else if(cmdGroup== CmdConstants.CMD_GROUP_HEART_BEATER){
                getHeartBeaterIsNormalCallback(responseObj, cmdId);
            }

        }else if(CmdConstants.ACTION_DEVICE_REPORT_APP.equals(action)){
            String receive =(String)obj;
            ReceiveBeanParent responseObj = g.fromJson(receive, ReceiveBeanParent.class);
            int cmdGroup = responseObj.getCmdGroup();
            int cmdId = responseObj.getCmdId();
            if(cmdGroup== CmdConstants.CMD_GROUP_DEVICE_INFO){
                reportDeviceInfoCallback((DeviceInfoBean) responseObj, cmdId);
            }else if(cmdGroup== CmdConstants.CMD_GROUP_AUDIO_INFO){
                reportAudioInfoCallback((AudioInfoBean) responseObj, cmdId);
            }

        }
    }
    public void getDeviceInfo(){
        DeviceCommand.getInstance().getDeviceInfo(tcpClient);
    }
    public void getDeviceName(){
        DeviceCommand.getInstance().getDeviceName(tcpClient);
    }
    public void getDeviceBattery(){
        DeviceCommand.getInstance().getDeviceBattery(tcpClient);
    }
    public void reportDeviceBatteryAck(boolean isSuccess){
        DeviceCommand.getInstance().reportDeviceBatteryAck(tcpClient, isSuccess);
    }
    public void getDeviceVolume(){
        DeviceCommand.getInstance().getDeviceVolume(tcpClient);
    }
    public void setDeviceVolume(int level){
        DeviceCommand.getInstance().setDeviceVolume(tcpClient, level);
    }
    public void reportDeviceVolumeAck(boolean isSuccess){
        DeviceCommand.getInstance().reportDeviceVolumeAck(tcpClient, isSuccess);
    }
    public void getDevicePlayState(){
        DeviceCommand.getInstance().getDevicePlayState(tcpClient);
    }
    public void reportDevicePlayStateAck(boolean isSuccess){
        DeviceCommand.getInstance().reportDevicePlayStateAck(tcpClient, isSuccess);
    }
    public void setDevicePlayState(int playState){
        DeviceCommand.getInstance().setDevicePlayState(tcpClient, playState);
    }
    public void getDeviceFirmwareVersion(){
        DeviceCommand.getInstance().getDeviceFirmwareVersion(tcpClient);
    }
    public void getDeviceSN(){
        DeviceCommand.getInstance().getDeviceSN(tcpClient);
    }
    public void getDeviceMAC(){
        DeviceCommand.getInstance().getDeviceMAC(tcpClient);
    }
    public void getAudioInfo(){
        DeviceCommand.getInstance().getAudioInfo(tcpClient);
    }
    public void getAudioEQIndex(){
        DeviceCommand.getInstance().getAudioEQIndex(tcpClient);
    }
    public void setAudioEQIndex(int index){
        DeviceCommand.getInstance().setAudioEQIndex(tcpClient, index);
    }
    public void getAudioDolby(){
        DeviceCommand.getInstance().getAudioDolby(tcpClient);
    }
    public void setAudioDolby(boolean on){
        DeviceCommand.getInstance().setAudioDolby(tcpClient, on?1:0);
    }
    public void reportAudioDolbyAck(boolean isSuccess){
        DeviceCommand.getInstance().reportAudioDolbyAck(tcpClient, isSuccess);
    }
    public void getAudioSurrond(){
        DeviceCommand.getInstance().getAudioSurrond(tcpClient);
    }

    public void setAudioSurrond(boolean on){
        DeviceCommand.getInstance().setAudioSurrond(tcpClient,on?1:0);
    }
    public void setAdaptionPreEQ(int preEQ){
        DeviceCommand.getInstance().setAdaptionPreEQ(tcpClient, preEQ);
    }
    public void getHeartBeaterIsNormal(){
        DeviceCommand.getInstance().getHeartBeaterIsNormal(tcpClient);
    }
    public void updateSocket(String ip, int port){
        DeviceCommand.getInstance().updateSocket(tcpClient, ip, port);
    }
    public void closeTcpClient(){
        DeviceCommand.getInstance().closeTcpClient(tcpClient);
    }

}

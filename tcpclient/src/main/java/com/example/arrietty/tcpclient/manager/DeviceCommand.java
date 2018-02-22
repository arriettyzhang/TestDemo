package com.example.arrietty.tcpclient.manager;

import android.text.TextUtils;
import android.util.Log;


import com.example.arrietty.tcpclient.CmdConstants;
import com.example.arrietty.tcpclient.model.GetBean;
import com.example.arrietty.tcpclient.tcp.SocketReceiver;
import com.example.arrietty.tcpclient.tcp.TcpClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asus on 2018/2/2.
 */

public class DeviceCommand {
    private static final String TAG ="S2.DeviceCommand";
    private static DeviceCommand _instance = null;
    public static DeviceCommand getInstance() {
        if (_instance == null) {
            _instance = new DeviceCommand();
        }

        return _instance;
    }
    public void getDeviceInfo(TcpClient client){
        Log.v(TAG, "getDeviceInfo " );
        GetBean data =combineData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_INFO);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void getDeviceName(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_NAME);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void getDeviceBattery(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_BATTERY);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void reportDeviceBatteryAck(TcpClient client, boolean success){
        String data = combineAckData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_BATTERY, "success_flag",success?1:0);
        Log.v(TAG, "data " +data);
        excuteCmd(client, data, CmdConstants.ACTION_APP_ACK_DEVICE);
    }
    public void getDeviceVolume(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_VOLUME);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void setDeviceVolume(TcpClient client, int level){
        String data = combineSetData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_SET_DEVICE_VOLUME ,"volume",level);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void reportDeviceVolumeAck(TcpClient client, boolean success){
        String data = combineAckData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_VOLUME, "success_flag",success?1:0);
        excuteCmd(client, data, CmdConstants.ACTION_APP_ACK_DEVICE);
    }
    public void getDevicePlayState(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_PLAY_STATE);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void setDevicePlayState(TcpClient client, int playState){
        String data = combineSetData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_SET_DEVICE_PLAY_STATE ,"play_state",playState);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void reportDevicePlayStateAck(TcpClient client, boolean success){
        String data = combineAckData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_PLAY_STATE, "success_flag",success?1:0);
        excuteCmd(client, data, CmdConstants.ACTION_APP_ACK_DEVICE);
    }
    public void getDeviceFirmwareVersion(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_FIRMWARE_VERSION);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }

    public void getDeviceSN(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_SN);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }

    public void getDeviceMAC(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_DEVICE_INFO, CmdConstants.CMD_ID_GET_DEVICE_MAC);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }

    public void getAudioInfo(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_AUDIO_INFO, CmdConstants.CMD_ID_GET_AUDIO_INFO);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void getAudioEQIndex(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_AUDIO_INFO, CmdConstants.CMD_ID_GET_AUDIO_EQ_INDEX);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void setAudioEQIndex(TcpClient client, int index){
        String data = combineSetData(CmdConstants.CMD_GROUP_AUDIO_INFO, CmdConstants.CMD_ID_SET_AUDIO_EQ_INDEX ,"eq_index",index);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void getAudioDolby(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_AUDIO_INFO, CmdConstants.CMD_ID_GET_AUDIO_DOLBY_AUDIO);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void setAudioDolby(TcpClient client, int offOn){
        String data = combineSetData(CmdConstants.CMD_GROUP_AUDIO_INFO, CmdConstants.CMD_ID_SET_AUDIO_DOLBY_AUDIO ,"dolby_audio",offOn);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void reportAudioDolbyAck(TcpClient client, boolean success){
        String data = combineAckData(CmdConstants.CMD_GROUP_AUDIO_INFO, CmdConstants.CMD_ID_GET_AUDIO_DOLBY_AUDIO, "success_flag",success?1:0);
        excuteCmd(client, data, CmdConstants.ACTION_APP_ACK_DEVICE);
    }
    public void getAudioSurrond(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_AUDIO_INFO, CmdConstants.CMD_ID_GET_AUDIO_SURROUND);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void setAudioSurrond(TcpClient client, int offOn) {
        String data = combineSetData(CmdConstants.CMD_GROUP_AUDIO_INFO, CmdConstants.CMD_ID_SET_AUDIO_SURROUND ,"surround",offOn);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }

    public void setAdaptionPreEQ(TcpClient client, int preEQ){
        String data = combineSetData(CmdConstants.CMD_GROUP_SELF_ADAPTION, CmdConstants.CMD_ID_SET_ADAPTION ,"pre_eq",preEQ);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public void getHeartBeaterIsNormal(TcpClient client){
        GetBean data =combineData(CmdConstants.CMD_GROUP_HEART_BEATER, CmdConstants.CMD_ID_GET_HEART_BEATER_IS_NORMAL);
        excuteCmd(client, data, CmdConstants.ACTION_APP_GET_SET_DEVICE);
    }
    public GetBean combineData(int cmdGroup, int cmdId){
        GetBean data = new GetBean();
        data.setCmdGroup(cmdGroup);
        data.setCmdId(cmdId);
        return data;
    }
    public String combineSetData(int group, int cmdId, String dataKey, int value){
        JSONObject o = new JSONObject();
        try {
            o.put("cmd_group", group);
            o.put("cmd_id", cmdId);
            JSONObject sub = new JSONObject();
            sub.put(dataKey,value );
            o.put("data", sub);
            return o.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
    public String combineAckData(int group, int cmdId, String dataKey, int value){
        JSONObject o = new JSONObject();
        try {
            o.put(dataKey, value);
            o.put("cmd_group", group);
            o.put("cmd_id", cmdId);
            return o.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
    private void excuteCmd(TcpClient client,GetBean data , String action){
        Log.v(TAG, " excuteCmd client "+client );
        if(client!=null && client.isConnected()){
            Log.v(TAG, " excuteCmd client 1111111" );
            Gson g = new Gson();
            client.sendData(g.toJson(data), action);
        }
    }
    private void excuteCmd(TcpClient client,String data , String action){
        if(client!=null && client.isConnected() && !TextUtils.isEmpty(data)){
            client.sendData(data, action);
        }
    }

    public void setReceverInterface(SocketReceiver interface1) {
        TcpClient.receiver = interface1;
    }
    public void updateSocket(TcpClient client, String ip, int port){
        if(client!=null){
            client.updateSocketInfo(ip,port);
        }else{
            Log.e(TAG, "updateSocket failed because tcpclient =null");
        }
    }
    public void closeTcpClient(TcpClient client){
        if(client!=null){
            client.destroy();
        }
    }


}

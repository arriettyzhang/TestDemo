package com.example.arrietty.tcpclient.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asus on 2018/2/2.
 */

public class DeviceInfoBean extends ReceiveBeanParent {

    /**
     * success_flag : 0
     * cmd_group : 1
     * cmd_id : 1
     * data : {"device_name":"xxxx...","battery":1,"volume":10,"play_state":0,"firmware_version":"xxxxxx","sn":"xxxxxxxx","mac":"AABBCCDDEEFF"}
     */

    @SerializedName("data")
    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * device_name : xxxx...
         * battery : 1
         * volume : 10
         * play_state : 0
         * firmware_version : xxxxxx
         * sn : xxxxxxxx
         * mac : AABBCCDDEEFF
         */

        @SerializedName("device_name")
        private String deviceName;//设备名称
        @SerializedName("battery")
        private int battery;//电量，int类型，1-5，代表5档电量（低 -> 高）
        @SerializedName("volume")
        private int volume;//音量，int类型，音量值 0 - 16
        @SerializedName("play_state")
        private int playState;//播放状态，int类型，0代表暂停，1代表播放
        @SerializedName("firmware_version")
        private String firmwareVersion;
        @SerializedName("sn")
        private String sn;
        @SerializedName("mac")
        private String mac;

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public int getBattery() {
            return battery;
        }

        public void setBattery(int battery) {
            this.battery = battery;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public int getPlayState() {
            return playState;
        }

        public void setPlayState(int playState) {
            this.playState = playState;
        }

        public String getFirmwareVersion() {
            return firmwareVersion;
        }

        public void setFirmwareVersion(String firmwareVersion) {
            this.firmwareVersion = firmwareVersion;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }
    }

    @Override
    public String toString() {

        return super.toString() +
                "DeviceInfoBean{" +
                "data=" + data +
                '}';
    }
}

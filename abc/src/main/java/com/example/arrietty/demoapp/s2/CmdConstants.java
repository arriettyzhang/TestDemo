package com.example.arrietty.demoapp.s2;

/**
 * Created by asus on 2018/2/2.
 */

public class CmdConstants {
    public static int SUCCESS_CODE=1;
    public static int PLAY_STATES_PAUSE=0;
    public static int PLAY_STATES_PLAY=1;
    //action
    public static String ACTION_APP_GET_SET_DEVICE ="com.oceanwing.soundcore.s2.action_app_req_device";
    public static String ACTION_DEVICE_ACK_APP="com.oceanwing.soundcore.s2.action_device_ack_rsp_app";
    public static String ACTION_DEVICE_REPORT_APP="com.oceanwing.soundcore.s2.action_device_report_app";
    public static String ACTION_APP_ACK_DEVICE="com.oceanwing.soundcore.s2.action_app_ack_device";
    //HEADER
    public static int APP_REQ_SET_DEVICE=0x08EE;
    public static int DEVICE_ACK_APP=0x09FF;
    public static int DEVICE_REQ_APP=0x06EE;
    public static int APP_ACK_DEVICE=0x07FF;
    //GROUP
    public static int CMD_GROUP_DEVICE_INFO=1;
    public static int CMD_GROUP_AUDIO_INFO=2;
    public static int CMD_GROUP_SELF_ADAPTION=3;
    public static int CMD_GROUP_HEART_BEATER=4;

    //ID
    public static int CMD_ID_GET_DEVICE_INFO=1;
    public static int CMD_ID_GET_DEVICE_NAME=2;
    public static int CMD_ID_GET_DEVICE_BATTERY=3;
    public static int CMD_ID_GET_DEVICE_VOLUME=4;
    public static int CMD_ID_GET_DEVICE_PLAY_STATE=5;
    public static int CMD_ID_GET_DEVICE_FIRMWARE_VERSION=6;
    public static int CMD_ID_GET_DEVICE_SN=7;
    public static int CMD_ID_GET_DEVICE_MAC=8;

    public static int CMD_ID_SET_DEVICE_VOLUME=81;
    public static int CMD_ID_SET_DEVICE_PLAY_STATE=82;//(0 PAUSE 1 PLAY)


    //audio
    public static int CMD_ID_GET_AUDIO_INFO=1;
    public static int CMD_ID_GET_AUDIO_EQ_INDEX=2;
    public static int CMD_ID_GET_AUDIO_DOLBY_AUDIO=3;
    public static int CMD_ID_GET_AUDIO_SURROUND=4;

    public static int CMD_ID_SET_AUDIO_EQ_INDEX=81;
    public static int CMD_ID_SET_AUDIO_DOLBY_AUDIO=82;
    public static int CMD_ID_SET_AUDIO_SURROUND=83;

    //adaption
    public static int CMD_ID_SET_ADAPTION=81;

    //Heart Beater
    public static int CMD_ID_GET_HEART_BEATER_IS_NORMAL =1;

}

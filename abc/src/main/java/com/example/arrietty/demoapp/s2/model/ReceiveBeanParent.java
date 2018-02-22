package com.example.arrietty.demoapp.s2.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asus on 2018/2/5.
 */

public class ReceiveBeanParent {
    @SerializedName("success_flag")
    private int successFlag;
    @SerializedName("cmd_group")
    private int cmdGroup;//1:device_info
    @SerializedName("cmd_id")
    private int cmdId;//cmd_id：1，device_info
    public int getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(int successFlag) {
        this.successFlag = successFlag;
    }

    public int getCmdGroup() {
        return cmdGroup;
    }

    public void setCmdGroup(int cmdGroup) {
        this.cmdGroup = cmdGroup;
    }

    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }

    @Override
    public String toString() {
        return "ReceiveBeanParent{" +
                "successFlag=" + successFlag +
                ", cmdGroup=" + cmdGroup +
                ", cmdId=" + cmdId +
                '}';
    }
}

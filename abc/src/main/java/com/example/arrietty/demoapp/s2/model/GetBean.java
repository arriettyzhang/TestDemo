package com.example.arrietty.demoapp.s2.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asus on 2018/2/2.
 */

public class GetBean {

    /**
     * cmd_group : 3
     * cmd_id : 81
     * data : {"pre_eq":0}
     */

    @SerializedName("cmd_group")
    private int cmdGroup;
    @SerializedName("cmd_id")
    private int cmdId;
    @SerializedName("data")
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pre_eq : 0
         */

        @SerializedName("pre_eq")
        private int preEq;

        public int getPreEq() {
            return preEq;
        }

        public void setPreEq(int preEq) {
            this.preEq = preEq;
        }
    }
}

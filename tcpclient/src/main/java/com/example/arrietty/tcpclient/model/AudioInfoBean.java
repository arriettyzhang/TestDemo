package com.example.arrietty.tcpclient.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asus on 2018/2/2.
 */

public class AudioInfoBean extends ReceiveBeanParent {

    /**
     * success_flag : 1
     * cmd_group : 2
     * cmd_id : 1
     * data : {"eq_index":1,"dolby_audio":0,"surround":0}
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
         * eq_index : 1
         * dolby_audio : 0
         * surround : 0
         */

        @SerializedName("eq_index")
        private int eqIndex;
        @SerializedName("dolby_audio")
        private int dolbyAudio;
        @SerializedName("surround")
        private int surround;

        public int getEqIndex() {
            return eqIndex;
        }

        public void setEqIndex(int eqIndex) {
            this.eqIndex = eqIndex;
        }

        public int getDolbyAudio() {
            return dolbyAudio;
        }

        public void setDolbyAudio(int dolbyAudio) {
            this.dolbyAudio = dolbyAudio;
        }

        public int getSurround() {
            return surround;
        }

        public void setSurround(int surround) {
            this.surround = surround;
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "AudioInfoBean{" +
                "data=" + data +
                '}';
    }
}


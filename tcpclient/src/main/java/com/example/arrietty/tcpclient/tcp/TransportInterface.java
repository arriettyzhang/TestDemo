package com.example.arrietty.tcpclient.tcp;

/**
 * Created by winner.wang on 2017/11/15.
 */

public interface TransportInterface {
    boolean connect();
    String sendData(String str, String action);
    boolean isConnected();
}

package com.example.arrietty.demoapp.s2.tcp;

/**
 * Created by asus on 2018/2/2.
 */

public interface TcpLinkCallback {
    void onTcpConnectSuccess();
    //0正常关闭
    void onTcpClose(int error);
}

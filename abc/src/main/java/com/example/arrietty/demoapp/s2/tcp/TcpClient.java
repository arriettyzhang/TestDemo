package com.example.arrietty.demoapp.s2.tcp;

import android.util.Log;

import com.example.arrietty.demoapp.s2.CmdConstants;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by winner.wang on 2017/11/15.
 */

public class TcpClient implements TransportInterface {

    public static final String TAG = "S2.TcpClient";
    public static SocketReceiver receiver;
    public static final int TCP_CLOSE_BY_EXCEPTION =-1;
    public static final int TCP_CLOSE_BY_NORMAL =0;
    public static final int HEART_TIME =30000;
    public static final int PORT = 5858;
    private String mIP;
    private int mPort;
    private Selector mSelector;
    private SocketChannel mSocketChannel;
    private Socket mClientSocket;
    private boolean isVlocking;
    private TcpLinkCallback linkCallback;

    public TcpClient(TcpLinkCallback linkCallback, String ip, int port){
        isVlocking = false;
        hart = 0;
        this.linkCallback = linkCallback;
        mIP =ip;
        mPort = port;
    }

    public void updateSocketInfo(String ip, int port){
        close();
        hart = 0;
        if(task!=null){
            task.cancel();
            task = null;
        }
       if(timer!=null){
           timer.cancel();
           timer = null;
       }
        mIP =ip;
        mPort = port;
        connect();
    }
    @Override
    public boolean connect() {
        ReceiveThread receive = new ReceiveThread();
        receive.start();
        return true;
    }


    @Override
    public String sendData(String data, String action) {

        SendThread send = new SendThread(data, action);
        send.start();
        return  null;

    }

    @Override
    public boolean isConnected() {
        if(mClientSocket != null){
            return mClientSocket.isConnected();
        }
//        if(mSocketChannel != null){
//            return mSocketChannel.isConnected();
//        }
        return false;
    }

    private void shortToByte(short x, byte[] bb, int pos) {
//        bb[pos] = (byte) (x >> 24);
//        bb[pos + 1] = (byte) (x >> 16);
        bb[pos] = (byte) (x >> 8);
        bb[pos + 1] = (byte) (x >> 0);
    }

    int hart;
    Timer timer;
    HartTask task;

    private class HartTask extends TimerTask {
        @Override
        public void run() {
            try {
                Log.v(TAG, "heart hart " +hart);
                hart++;
                if(hart > 100){
                    hart = 0;
                    close();
                    task.cancel();
                    task = null;
                    timer.cancel();
                    timer = null;
                }else {
                    Log.v(TAG, "heart");
                    JSONObject json = new JSONObject();
                    json.put("cmd_group", CmdConstants.CMD_GROUP_HEART_BEATER);
                    json.put("cmd_id", CmdConstants.CMD_ID_GET_HEART_BEATER_IS_NORMAL);
                    sendData(json.toString(), CmdConstants.ACTION_APP_GET_SET_DEVICE);
                   // HartThread h = new HartThread((byte)0x04, (byte)0x01, "hart");
                   // h.start();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private class ReceiveThread extends Thread{

        @Override
        public void run() {
            try {
                String receive = null;
                if(mClientSocket != null){
                    if(mClientSocket.isConnected()){
                        mClientSocket.close();
                    }
                    mClientSocket = null;
                }
                mClientSocket = new Socket();
                SocketAddress socAddress = new InetSocketAddress(mIP, mPort);
                mClientSocket.connect(socAddress, 2000);
                linkCallback.onTcpConnectSuccess();
                if(timer == null){
                    timer = new Timer();
                }
                if(task == null){
                    task = new HartTask();
                }
                timer.scheduleAtFixedRate(task, 0, HEART_TIME);
                while (mClientSocket.isConnected()){
                    InputStream is = mClientSocket.getInputStream();
                    DataInputStream dis = new DataInputStream(is);
                    hart =0;
                    parseReceiveData(dis);
                }
            }catch (Throwable e){
                Log.e(TAG, "connect error : " + e);
                close();
                linkCallback.onTcpClose(TCP_CLOSE_BY_EXCEPTION);
            }
        }
    }

    public void destroy(){
        linkCallback.onTcpClose(TCP_CLOSE_BY_NORMAL);
        close();
        linkCallback = null;
        receiver = null;
    }


    private class SendThread extends Thread {

        private String mData;
        private String mAction;
        public SendThread(String data, String action ){
            mData = data;
            mAction = action;
        }
        @Override
        public void run() {
            try {
                if(mData == null || mClientSocket == null || !mClientSocket.isConnected()) return;
                OutputStream os = mClientSocket.getOutputStream();
                byte[] buffer = new byte[1024];
                if(CmdConstants.ACTION_APP_ACK_DEVICE.equals(mAction)){
                    buffer[0] = 0x07;
                    buffer[1] = (byte) 0xFF;
                }else{
                    buffer[0] = 0x08;
                    buffer[1] = (byte) 0xEE;
                }
                short length = (short) mData.getBytes().length;
                length += 4;
                shortToByte(length, buffer, 2);
                System.arraycopy(mData.getBytes("utf8"), 0, buffer, 4, mData.getBytes("utf8").length);
                final String mHeader = numToHex16(buffer[0] &0xff) + numToHex16(buffer[1]&0xff);
                Log.d(TAG, "send : \nheader:0x" + mHeader + "\n length:" + length + "\n"+ mData);
                os.write(buffer, 0, length);
                os.flush();
            }catch (Throwable e){
                Log.e(TAG, "send data error : " + e);
                close();
            }
        }
    }

    private void close(){
        try {
            if(mClientSocket != null){
                mClientSocket.close();
                mClientSocket = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private void parseReceiveData(DataInputStream dis) throws IOException {
        String receive;
        String action="";
        int head1 = dis.readByte() & 0xff;
        int head2 = dis.readByte() & 0xff;
        short length = dis.readShort();
        if(length <= 0){
            Log.e(TAG, "illegal data");
            return;
        }
        Log.d(TAG, "head1 : " + head1 + " --- head2 : " + head2);
        byte[] bufferRead = new byte[1024];
        int len = dis.read(bufferRead, 0, length - 4);
        if(len <= 0 || (length!= len+4)){
            Log.e(TAG, "illegal data");
            return;
        }
        receive = new String(bufferRead, 0, len);
        Log.d(TAG, "receive " +receive);
        if(head1 == 0x09 && head2 == 0xFF){
            action = CmdConstants.ACTION_DEVICE_ACK_APP;
            receiver.OnReciver(action, receive);
        }else if(head1 == 0x06 && head2 == 0xEE){
            action = CmdConstants.ACTION_DEVICE_REPORT_APP;
            receiver.OnReciver(action, receive);
        }
        final String mHeader = numToHex16(head1) + numToHex16(head2);
        Log.v(TAG,"receive data: \n" + "cmd action: " + action + "\n" + "header: 0x" +
                mHeader + "\n" + "length: " + length + "\n" + "data: " + receiver);
    }
    //需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%02x", b);
    }
}

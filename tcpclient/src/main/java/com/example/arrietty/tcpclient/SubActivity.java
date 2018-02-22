package com.example.arrietty.tcpclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;

public class SubActivity extends AppCompatActivity {
    public static final String TAG = "S2.SubActivity";

    private TextView mJsonSendTv;
    private TextView mJsonReceiveTv;

    private String host = "10.1.116.176";//"10.1.114.225";
    private int PORT =5858;

    private Socket mClientSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sub);

        mJsonSendTv = (TextView) findViewById(R.id.json_send_tv);
        mJsonReceiveTv = (TextView) findViewById(R.id.json_receive_tv);

        cnnect();
    }

    // get device info
    public void getDeviceInfo(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 1);
        jsonObject.put("cmd_id", 1);
        final String mData = jsonObject.toString();
        Log.i(TAG, "jSON result"+mData);

        sendData("get device info request", mData);
    }

    // get device name
    public void getDeviceName(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 1);
        jsonObject.put("cmd_id", 2);
        final String mData = jsonObject.toString();
        Log.i("jSON result", mData);

        sendData("get device name request", mData);
    }

    // get battery
    public void getBattery(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 1);
        jsonObject.put("cmd_id", 3);
        final String mData = jsonObject.toString();
        Log.i(TAG,  "jSON result"+ mData);

        sendData("get battery request", mData);
    }

    // get volume
    public void getVolume(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 1);
        jsonObject.put("cmd_id", 4);
        final String mData = jsonObject.toString();
        Log.i(TAG,  "jSON result"+mData);

        sendData("get volume request", mData);
    }

    // get play state
    public void getPlayState(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 1);
        jsonObject.put("cmd_id", 5);
        final String mData = jsonObject.toString();
        Log.i("jSON result", mData);

        sendData("get play state request", mData);
    }

    // get firmware version
    public void getFirmwareVersion(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 1);
        jsonObject.put("cmd_id", 6);
        final String mData = jsonObject.toString();
        Log.i("jSON result", mData);

        sendData("get firmware version request", mData);
    }

    // get sn
    public void getSN(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 1);
        jsonObject.put("cmd_id", 7);
        final String mData = jsonObject.toString();
        Log.i("jSON result", mData);

        sendData("get sn request", mData);
    }

    // get mac
    public void getMac(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 1);
        jsonObject.put("cmd_id", 8);
        final String mData = jsonObject.toString();
        Log.i("jSON result", mData);

        sendData("get mac request", mData);
    }

    // get audio info
    public void getAudioInfo(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 2);
        jsonObject.put("cmd_id", 1);
        final String mData = jsonObject.toString();
        Log.i("jSON result", mData);

        sendData("get audio info request", mData);
    }

    // get eq index
    public void getEqIndex(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 2);
        jsonObject.put("cmd_id", 2);
        final String mData = jsonObject.toString();
        Log.i("jSON result", mData);

        sendData("get eq index request", mData);
    }

    // get dolby audio
    public void getDolbyAudio(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 2);
        jsonObject.put("cmd_id", 3);
        final String mData = jsonObject.toString();
        Log.i("jSON result", mData);

        sendData("get dolby audio request", mData);
    }

    // get surround
    public void getSurround(View v) throws JSONException {
        // data
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd_group", 2);
        jsonObject.put("cmd_id", 4);
        final String mData = jsonObject.toString();
        Log.i("jSON result", mData);

        sendData("get surround request", mData);
    }

    public String sendData(String cmd_name,  String data) {
        SendThread send = new SendThread(cmd_name, data);
        send.start();
        return  null;
    }

    public void cnnect(){
        ReceiveThread receive = new ReceiveThread();
        receive.start();
    }


    private class SendThread extends Thread {
        private String mCmdName;
        private String mData;

        public SendThread(String cmd_name, String data){
            mCmdName = cmd_name;
            mData = data;
        }
        @Override
        public void run() {
            try {
                if(mData == null || mClientSocket == null || !mClientSocket.isConnected()) return;
                OutputStream os = mClientSocket.getOutputStream();
                byte[] buffer = new byte[1024];
                buffer[0] = 0x08;
                buffer[1] = (byte) 0xEE;
                short length = (short) mData.getBytes().length;
                length += 4;
                final int mLength = length;
                shortToByte(length, buffer, 2);
                System.arraycopy(mData.getBytes("utf8"), 0, buffer, 4, mData.getBytes("utf8").length);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mJsonSendTv.setText("send data: \n" + "cmd name: " + mCmdName + "\n" + "header: " +
                                "08EE \n" + "length: " + mLength + "\n" + "data: " + mData);
                    }
                });

                Log.d(TAG, "send : " + mData);
                os.write(buffer, 0, length);
                os.flush();
            }catch (Throwable e){
                Log.e(TAG, "send data error : " + e);
                close();
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
                SocketAddress socAddress = new InetSocketAddress(host, PORT);//5858
                Log.v(TAG, "connected");
                mClientSocket.connect(socAddress, 2000);
                Log.v(TAG, "connected11111111");
                if(timer == null){
                    timer = new Timer();
                }
                if(task == null){
                    task = new HartTask();
                }
                timer.scheduleAtFixedRate(task, 0, 30000);
                while (mClientSocket.isConnected()){
                    InputStream is = mClientSocket.getInputStream();
                    DataInputStream dis = new DataInputStream(is);
                    int head1 = dis.readByte() & 0xff;
                    int head2 = dis.readByte() & 0xff;
                    short length = dis.readShort();
                    if(length <= 0){
                        continue;
                    }
                    Log.d(TAG, "head1 : " + head1 + " --- head2 : " + head2);
                    byte[] bufferRead = new byte[1024];
                    int len = dis.read(bufferRead, 0, length - 4);
                    if(head1 == 0x09 && head2 == 0xff){
                        if(len > 0){
                            receive = new String(bufferRead, 0, len);
                        }
                        Log.d(TAG, "read : " + "length : " + length + "---" + receive + "----");
                        JSONObject json = new JSONObject(receive);
                        int cmd_group = json.optInt("cmd_group", -1);
                        int cmd_id = json.optInt("cmd_id", -1);

                        String cmdName = "";
                        if(cmd_group == 1 && cmd_id == 1) {
                            cmdName = "get device info response";
                        } else if(cmd_group == 1 && cmd_id == 2) {
                            cmdName = "get device name response";
                        } else if(cmd_group == 1 && cmd_id == 3) {
                            cmdName = "get battery response";
                        } else if(cmd_group == 1 && cmd_id == 4) {
                            cmdName = "get volume response";
                        } else if(cmd_group == 1 && cmd_id == 5) {
                            cmdName = "get play state response";
                        } else if(cmd_group == 1 && cmd_id == 6) {
                            cmdName = "get firmware version response";
                        } else if(cmd_group == 1 && cmd_id == 7) {
                            cmdName = "get sn response";
                        } else if(cmd_group == 1 && cmd_id == 8) {
                            cmdName = "get mac response";
                        } else if(cmd_group == 2 && cmd_id == 1) {
                            cmdName = "get audio info response";
                        } else if(cmd_group == 2 && cmd_id == 2) {
                            cmdName = "get eq index response";
                        } else if(cmd_group == 2 && cmd_id == 3) {
                            cmdName = "get dolby audio response";
                        } else if(cmd_group == 2 && cmd_id == 4) {
                            cmdName = "get surround response";
                        } else if(cmd_group == 4 && cmd_id == 1) {
                            cmdName = "heart beater response";
                        }

                        final String mCmdName = cmdName;
                        final String mHeader = numToHex16(head1) + numToHex16(head2);
                        final int mLength = length;
                        final String mData = receive;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mJsonReceiveTv.setText("receive data: \n" + "cmd name: " + mCmdName + "\n" + "header: " +
                                        mHeader + "\n" + "length: " + mLength + "\n" + "data: " + mData);
                            }
                        });

                        if(cmd_group == 4){
                            hart--;
                            if(hart < 0){
                                hart = 0;
                            }
                        }
                    }
                }
            }catch (Throwable e){
                Log.e(TAG, "connect error : " + e);
                close();
            }
        }
    }


    int hart;
    Timer timer;
    HartTask task;

    private class HartTask extends TimerTask {
        @Override
        public void run() {
            try {
                hart++;
                if(hart > 2){
                    hart = 0;
                    close();
                    task.cancel();
                    task = null;
                    timer.cancel();
                    timer = null;
                }else {
                    JSONObject json = new JSONObject();
                    json.put("cmd_group", 4);
                    json.put("cmd_id", 1);
                    sendData("heart beater",json.toString());
                    // HartThread h = new HartThread((byte)0x04, (byte)0x01, "hart");
                    // h.start();
                }
            } catch (Throwable e) {
                e.printStackTrace();
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

    private void shortToByte(short x, byte[] bb, int pos) {
        bb[pos] = (byte) (x >> 8);
        bb[pos + 1] = (byte) (x >> 0);
    }

    //需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%02x", b);
    }
}

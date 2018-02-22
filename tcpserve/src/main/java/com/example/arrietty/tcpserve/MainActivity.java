package com.example.arrietty.tcpserve;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private TextView mJsonReceiveTv;
    private Button btn;

    volatile Socket mSocket;
    ServerSocket server;
    //需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%02x", b);
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==3){
                mJsonReceiveTv.setText((String) msg.obj);
            }else if(msg.what==0x01){
//                Toast.makeText(MainActivity.this,(String) msg.obj, 500).show();
                mJsonReceiveTv.setText((String) msg.obj);
                btn.setEnabled(true);
            }else if(msg.what==0x02){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(TAG, "客户端连接----读取客户端发来的数据");
                            InputStream ins = mSocket.getInputStream();
                            byte[] bufferRead = new byte[1024];
                            int len = ins.read(bufferRead);
                            if(len <= 0){
                                return;
                            }

                            // header
                            int header1 = bufferRead[0] & 0xff;
                            int header2 = bufferRead[1] & 0xff;
                            Log.d(TAG, "head1 : " + numToHex16(header1) + " --- head2 : " + numToHex16(header2));

                            // length
                            short length = (short) ((bufferRead[2] << 8) + bufferRead[3]);
                            Log.d(TAG, "length: " + len);

                            String receive = new String(bufferRead, 4, length);
                            Log.d(TAG, "receive : " + receive);

                            //第一步，生成Json字符串格式的JSON对象
                            JSONObject jsonObject = new JSONObject(receive);
                            //第二步，从JSON对象中取值如果JSON 对象较多，可以用json数组
                            String cmd_group = jsonObject.getString("cmd_group");
                            String cmd_id = jsonObject.getString("cmd_id");

                            StringBuffer sb = new StringBuffer();
                            sb.append("header: 0x");
                            sb.append(numToHex16(header1));
                            sb.append(numToHex16(header2));
                            sb.append("\n");

                            sb.append("length: ");
                            sb.append(length);
                            sb.append("\n");

                            sb.append("data: ");
                            sb.append(jsonObject.toString());

                            Looper.prepare();
                            Message message=Message.obtain();
                            message.what=0x01;
                            message.obj=sb.toString();
                            mHandler.sendMessage(message);
                            Log.v("MainActvity","receive data " +sb.toString() );
                            Looper.loop();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            if(mSocket!=null){
                                try {
                                    mSocket.close();
                                    mSocket=null;
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }).start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate=");
        setContentView(R.layout.activity_main);

        mJsonReceiveTv = (TextView) findViewById(R.id.json_receive_tv);
        btn = (Button) findViewById(R.id.get_device_info_btn);
        String ip = getIPAddress(this);
        Log.v(TAG, "ip=" +ip);
        mJsonReceiveTv.setText(ip);
    }

    public class SocketServerDemo extends Thread {
        //Socket服务器
        ServerSocket serverSocket = null;
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();

            //Socket客户端
            Socket client = null;
            //输入流
            InputStream ips = null;
            //字节
            try {
                //服务器端口号
                Log.i(TAG, "阻塞，等待客户端连接<<<<<<<<<");
                if(serverSocket==null){
                    serverSocket = new ServerSocket(5858);
                }
                //循环接收客户端
                while (true) {
                    // 等待客户端请求，如果没有客户端请求，会一直堵塞在这里

                    client = serverSocket.accept();
                    Log.i(TAG,"客户端连接成功<<<<<<<<<客户端连接成功");
                    ips = client.getInputStream();
                    try {
                        //循环接收客户端信息
                        boolean flag = true;
                        while(flag){
                            byte[] bufferRead = new byte[1024];
                            Log.v(TAG, "client " +client);
                                int len = ips.read(bufferRead);
                                Log.v(TAG, "client11111111111 " +client);
                                Log.v(TAG, "len " +len);
                                if(len < 0){
                                    flag =false;
                                    break;
                                }
                                if(len == 0){
                                    break;
                                }



                            // header
                            int header1 = bufferRead[0] & 0xff;
                            int header2 = bufferRead[1] & 0xff;
                            Log.d(TAG, "head1 : " + numToHex16(header1) + " --- head2 : " + numToHex16(header2));

                            // length
                            short length = (short) ((bufferRead[2] << 8) + bufferRead[3]);
                            Log.d(TAG, "length: " + length);

                            String receive = new String(bufferRead, 4, length-4);
                            Log.d(TAG, "receive : " + receive);

                            //第一步，生成Json字符串格式的JSON对象
                            JSONObject jsonObject = new JSONObject(receive);
                            //第二步，从JSON对象中取值如果JSON 对象较多，可以用json数组
                            String cmd_group = jsonObject.getString("cmd_group");
                            String cmd_id = jsonObject.getString("cmd_id");

                            StringBuffer sb = new StringBuffer();
                            sb.append("header: 0x");
                            sb.append(numToHex16(header1));
                            sb.append(numToHex16(header2));
                            sb.append("\n");

                            sb.append("length: ");
                            sb.append(length);
                            sb.append("\n");

                            sb.append("data: ");
                            sb.append(jsonObject.toString());
                            mHandler.sendMessage(mHandler.obtainMessage(3, sb.toString()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v(TAG, " e.getMessage()" +e.getMessage());
                        if(client!=null){
                            client.close();
                            client=null;
                        }

                    }
                }
            } catch (IOException e1) {
                Log.v(TAG, " 1111111111111111e.getMessage()" +e1.getMessage());
                e1.printStackTrace();
                close();

            }
        }
        public void close(){
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                    serverSocket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            btn.setEnabled(true);
        }

    }
    private SocketServerDemo socketServerDemo;
    public void submit(View v){
        btn.setEnabled(false);
        if(socketServerDemo ==null){
            socketServerDemo = new SocketServerDemo();
            socketServerDemo.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(socketServerDemo!=null){
            socketServerDemo.close();
        }
    }

    public void submit1(View v) throws JSONException, IOException {
        btn.setEnabled(false);

        new Thread(new  Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "阻塞，等待客户端连接<<<<<<<<<");
                    if(server==null){
                        server=new ServerSocket(8156);
                    }
                    mSocket=server.accept();
                    Log.i(TAG,"客户端连接成功<<<<<<<<<客户端连接成功");
                    Looper.prepare();
                    Message message=Message.obtain();
                    message.what=0x02;
                    mHandler.sendMessage(message);
                    Looper.loop();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            Log.v(TAG, "info.getType() " +info.getType());
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
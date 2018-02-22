package com.example.arrietty.demoapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.arrietty.demoapp.battery.GetBatteryInfo;
import com.example.arrietty.demoapp.fcm.FirebaseActivity;
import com.example.arrietty.demoapp.ga.GoogleAnalycisActivity;
import com.example.arrietty.demoapp.gsonformat.JsonActivity;
import com.example.arrietty.demoapp.recyclelist.RecycleMainActivity;
import com.example.arrietty.demoapp.s2.S2MainActivity;
import com.example.arrietty.demoapp.spandemo.SpanActivity;
import com.example.arrietty.demoapp.utils.AppUtil;
import com.example.arrietty.demoapp.utils.DensityUtil;
import com.example.arrietty.demoapp.utils.SystemUtil;

import java.util.Locale;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "RecycleMainActivity";
    private static String NN = "\n\n";
    private String sUrl = "https://a000.linkplay.com/alexa.php?code=ANLfjnEKpqiLHMvFHAGR&scope=alexa%3Aall&state=208257577ll0975l93l2l59l895857093449424";

    private LinearLayout layout1;
    private Button fcmBtn, gaBtn, recycleBtn, spanBtn, getbatteryBtn, jsonBtn, tcpBtn;
    private EditText  mHighlightBgEdit;
    private TextView mSpanText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHighlightBgEdit = (EditText)findViewById(R.id.highlightBgEdit);
        mHighlightBgEdit.selectAll();

        mSpanText = (TextView)findViewById(R.id.spanText) ;
        String str = "dhsaohfpao";
        String subStr =str.substring(0, str.length()/2);
        SpannableStringBuilder style=new SpannableStringBuilder(str);
        style.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.edittext_focus_bg_color)),0,subStr.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSpanText.setText(style);

        MyApplication application = (MyApplication) getApplication();
        fcmBtn = (Button)findViewById(R.id.fcmBtn);
        fcmBtn.setOnClickListener(this);
        gaBtn = (Button)findViewById(R.id.gaBtn);
        gaBtn.setOnClickListener(this);
        recycleBtn = (Button)findViewById(R.id.recycleBtn);
        recycleBtn.setOnClickListener(this);
        spanBtn = (Button)findViewById(R.id.spanBtn);
        spanBtn.setOnClickListener(this);
        tcpBtn = (Button)findViewById(R.id.tcpCLientBtn);
        tcpBtn.setOnClickListener(this);
        getbatteryBtn = (Button)findViewById(R.id.getbatteryBtn);
        getbatteryBtn.setOnClickListener(this);
        jsonBtn = (Button)findViewById(R.id.jsonBtn);
        jsonBtn.setOnClickListener(this);
        requestPhonePermiss();
        int x = 1|2|4;
        int y =2;
        Log.v(TAG, "x" + x + "  y "+y);
        Log.v(TAG, "y&x" + (y&x));

    }
    private static final int READ_PHONE_CODE =100;
    private void requestPhonePermiss(){
        PermissionGen.with(this)
                .addRequestCode(READ_PHONE_CODE)
                .permissions(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                .request();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    @PermissionSuccess(requestCode = READ_PHONE_CODE)
    public void requestPhotoSuccess(){
        //成功之后的处理
        //.......
        showSystemParameter();
    }

    @PermissionFail(requestCode = READ_PHONE_CODE)
    public void requestPhotoFail(){
        //失败之后的处理，我一般是跳到设置界面
        AppUtil.goToSetting(this);
    }

    private void showSystemParameter() {
        Log.v(TAG, "手机厂商：" + SystemUtil.getDeviceBrand());
        Log.v(TAG, "手机型号：" + SystemUtil.getSystemModel());
        Log.v(TAG, "手机当前系统语言：" + SystemUtil.getSystemLanguage());
        Log.v(TAG, "Android系统版本号：" + SystemUtil.getSystemVersion());
        Log.v(TAG, "手机IMEI：" + SystemUtil.getIMEI(getApplicationContext()));
    }
    /*
    通过URI的方式可以从URL中获取query信息或者query里面的具体的key对应的内容
     */
    private void getQueryFromUrl(){
        Uri url = Uri.parse(sUrl);
        String query = url.getQuery();
        String param1 = url.getQueryParameter("code");
        String code = sUrl.split("\\?")[1].split("\\&")[0].substring(5);
        Log.v(TAG, "query = " + query);
        Log.v(TAG, "param1 = " + param1);
        Log.v(TAG, "code = " + code);
    }
    private void getScreenInfo(){
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Log.v(TAG, "屏幕尺寸1: 宽度 = "+display.getWidth()+"高度 = :"+display.getHeight());

        Log.v(TAG, "10dp = "+ DensityUtil.dip2px(this, 10) + "px");
        Log.v(TAG, "30px = "+ DensityUtil.px2dip(this, 30) + "dp");
    }

    //zh_CN (1)方法1得到zh (2)方法2得到cn (3)方法3得到zh_CN
    private String getFirstLanguage() {
        String locale = Locale.getDefault().getLanguage();
        return locale;
    }

    private String getCountry() {
        String locale = Locale.getDefault().getCountry();
        return locale;
    }

    private String getLastLanguage(Context context) {
        Resources res = context.getResources();
        Configuration config = res.getConfiguration();
        String locale = config.locale.getCountry();
        return locale;
    }

    private String getLanguage() {
        String locale = Locale.getDefault().toString();

        return locale;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.v(TAG,"onBackPressed");
        if(dialog!=null&& dialog.isShowing()){
            dialog.dismiss();
            Log.v(TAG,"dialog");
        }else{
            Log.v(TAG,"dialog11");
            finish();
        }

    }

    AlertDialog dialog;
    private void showDialog(){
        if(dialog!=null&& dialog.isShowing()){
            dialog.dismiss();
        }
        dialog= new AlertDialog.Builder(this)
                .setTitle("title")
                .setMessage("message")
                .show();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    private int charToint(char c){
        int x= c;
        int y='0';
        return (x-y);
    }
    private String transeGenieMac1ToMac2(String mac){
        Log.v(TAG, "mac="+ mac);
        StringBuilder str= new StringBuilder("");
        if(mac==null){
            return null;
        }
        String[] arr = mac.split(":");
        for(int i =0; i<arr.length;i++){
            str.append(arr[i]);
        }
        Log.v(TAG,"str " +str.length());
        Log.v(TAG,"str " +str.substring(0,1));
        Log.v(TAG,"str " +str.substring(11,12));
        double d=0;
        for(int i=str.length();i>0;i--){
            //Log.v(TAG,"str.charAt(i) " +str.charAt(i));
            int tmp;
            {
                tmp = Integer.parseInt(str.substring(i-1,i),16);
            }

            Log.v(TAG,"tmp " +tmp);
            d = d+ (tmp << (4 *(str.length() -i)));
            Log.v(TAG, "d " +d);
        }
        d = d+2;

        StringBuilder result = new StringBuilder();
        for(int j =11;j>=0;j--){
            int t = (int)(d/(16^j));
            d =  d-t;
            Log.v(TAG, "t=" + t+"   d " +d);
            result.append(t);
            if(j!=0 && j%2==0){
                result.append(":");
            }
        }
        Log.v(TAG,"result ="+result.toString());
        return result.toString();
    }
    private String macAdd2(String mac){
        int max= 0xff;
        if(mac ==null){
            return null;
        }
        String[] arr = mac.split(":");
        int[] intArr = new int[arr.length];
        for(int i=0;i<arr.length;i++){
            intArr[i]= Integer.valueOf(arr[i], 16);
        }
        for(int i=intArr.length-1;i>0;i--){
            if(i==intArr.length-1){
                if( intArr[i]+2>max){
                    intArr[i] = intArr[i]+2-(max+1);
                    intArr[i-1] = intArr[i-1]+1;
                    continue;
                }else{
                    intArr[i] = intArr[i]+2;
                    break;
                }
            }
            if(intArr[i]>max){
                intArr[i] = intArr[i]-(max+1);
                intArr[i-1] = intArr[i-1]+1;
            }else{
                break;
            }
        }
        if(intArr[0]>max){
            intArr[0] = intArr[0]-(max+1);
        }
        return intArrToString(intArr);


    }
    private String  intArrToString(int[] arr){
        StringBuilder sb = new StringBuilder();
        int unit=0x0f;
        for(int i=0;i<arr.length;i++){
            if(arr[i]<unit){
                sb.append("0");
            }
            sb.append(Integer.toHexString(arr[i]));
            sb.append(":");
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        Class<?> cls = MainActivity.class;
        Intent i = new Intent();

        switch (v.getId()){
            case R.id.spanBtn:
                cls = SpanActivity.class;
                break;
            case R.id.gaBtn:
                cls = GoogleAnalycisActivity.class;
                break;
            case R.id.fcmBtn:
                cls = FirebaseActivity.class;
                break;
            case R.id.recycleBtn:
                cls = RecycleMainActivity.class;
                break;
            case R.id.getbatteryBtn:
                cls = GetBatteryInfo.class;
                break;
            case R.id.jsonBtn:
                cls = JsonActivity.class;
                break;
            case R.id.tcpCLientBtn:
                cls = S2MainActivity.class;
                break;
        }
        i.setClass(MainActivity.this, cls);
        startActivity(i);
    }

    public void onCLickFun(View v){
        Class<?> cls = MainActivity.class;
        Intent i = new Intent();
        switch (v.getId()){
            case R.id.gradientBtn:
                cls = GradientActivity.class;
                break;

        }
        i.setClass(MainActivity.this, cls);
        startActivity(i);
    }
}

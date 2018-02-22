package com.example.arrietty.demoapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

/**
 * Created by ocean on 2017/4/17.
 */

public class NetworkUtil {
    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }

        return false ;
    }
    public static boolean isMobileConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
             if (type.equalsIgnoreCase("MOBILE")) {
                 return true;
             }
        }
        return false;
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null && mWifiManager.isWifiEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null || cm.getActiveNetworkInfo()==null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 获取wifi连接的ssid
     */
    public static String getWifiSsid(Context context) {
        WifiManager wm = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wm.getConnectionInfo();
        if (connectionInfo == null) {
            return "";
        } else {
            return wm.getConnectionInfo().getSSID();
        }

    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent =  new Intent(Settings.ACTION_WIFI_SETTINGS);
        activity.startActivity(intent);
    }


    /**
     * Gps是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = locationManager.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }
    public static Boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();

    }

    public static String getConnectedWifiMacAddress(Context context) {
        String connectedWifiMacAddress = null;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> wifiList;

        if (wifiManager != null) {
            wifiList = wifiManager.getScanResults();
            WifiInfo info = wifiManager.getConnectionInfo();
            if (wifiList != null && info != null) {
                for (int i = 0; i < wifiList.size(); i++) {
                    ScanResult result = wifiList.get(i);
                    if (info.getBSSID().equals(result.BSSID)) {
                        connectedWifiMacAddress = result.BSSID;
                    }
                }
            }
        }
        if(connectedWifiMacAddress!=null){
            connectedWifiMacAddress = connectedWifiMacAddress.toUpperCase();
        }
        return connectedWifiMacAddress;
    }
    public static String macAdd2ForGenie(String mac){
        int max= 0xff;
        if(mac ==null){
            return null;
        }
        String[] arr = mac.split(":");
        int[] intArr = new int[arr.length];
        try{
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
            return intArrToString(intArr).toUpperCase();
        }catch(NumberFormatException e){
            Log.e("", "mac string error");
            return null;
        }




    }
    private static String  intArrToString(int[] arr){
        StringBuilder sb = new StringBuilder();
        int unit=0x0f;
        for(int i=0;i<arr.length;i++){
            if(arr[i]<unit){
                sb.append("0");
            }
            sb.append(Integer.toHexString(arr[i]));
            if(i==arr.length-1){
                break;
            }
            sb.append(":");
        }
        return sb.toString();
    }


}
package com.example.arrietty.demoapp.battery;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by asus on 2017/12/8.
 */

public class LocalPowerProfile {
    public static final String TAG ="LocalPowerProfile";
    public static LocalPowerProfile instance;
    public static Class powerProfile =null;
    public static Object o;
    public static boolean creatSucess =false;
    public static synchronized LocalPowerProfile getInstance(Context context){
        if(instance==null){
            instance = new LocalPowerProfile(context);
        }
        return  instance;
    }
    private LocalPowerProfile(Context context){

        HashMap<String, Object> powerInfo = new HashMap<>();
        try {
            powerProfile = Class.forName("com.android.internal.os.PowerProfile");
            Constructor constructor = powerProfile.getConstructor(new Class[]{Context.class});
            o = constructor.newInstance(context.getApplicationContext());
            creatSucess = true;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "create LocalPowerProfile "+ e.getMessage());
            creatSucess = false;
        }

    }
    public static double getTotalBatteryCapacity(){
        if(!creatSucess){
            Log.e(TAG, "create LocalPowerProfile failed");
            return -1;
        }
        double  capacity=0;
        try {
            Method getBatteryCapacity = powerProfile.getMethod("getBatteryCapacity");
            capacity  = (double ) getBatteryCapacity.invoke(o);
            Log.v(TAG, "capacity " + capacity);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(TAG, "getTotalBatteryCapacity " + e.getMessage());
        }catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e(TAG, "getTotalBatteryCapacity" + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.e(TAG, "getTotalBatteryCapacity"+e.getMessage());
        }finally {
            return capacity;
        }
    }
    public static HashMap<String, Object> getPowerProfileInfo(){
        if(!creatSucess){
            Log.e(TAG, "create LocalPowerProfile failed");
            return null;
        }
        try {
            HashMap<String, Object> powerInfo = new HashMap<>();
//            Class powerProfile = Class.forName("com.android.internal.os.PowerProfile");
//            Constructor constructor = powerProfile.getConstructor(new Class[]{Context.class});
//            Object o = constructor.newInstance(activity);
            Field field = powerProfile.getDeclaredField("sPowerMap");
            field.setAccessible(true);
            Object values = field.get(o);
            HashMap<String, Object> powerMap = (HashMap<String, Object>) values;
            Iterator<Map.Entry<String, Object>> entryit = powerMap.entrySet().iterator();
            while (entryit.hasNext()) {
                Map.Entry<String, Object> entry = entryit.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                powerInfo.put(key,value);
            }
            toStringPowerProfileInfo(powerInfo);

            return powerInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void toStringPowerProfileInfo(HashMap<String, Object> powerInfo){
        if(powerInfo!=null){
            for (Map.Entry<String, Object> entry : powerInfo.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if(value instanceof Double[]){
                    Double[] vs = (Double[]) value;
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for (double v : vs) {
                        sb.append(v + ",");
                    }
                    sb.append("]");
                    Log.i(TAG, key + ":" + sb.toString());
                } else {
                    Log.i(TAG, key + ":" + value.toString());
                }

            }
        }
    }
    public static double getAveragePower(String level){
        if(!creatSucess){
            Log.e(TAG, "create LocalPowerProfile failed");
            return -1;
        }
        double averagePower = 0;
        try {
            Method get = powerProfile.getDeclaredMethod("getAveragePower", String.class,  String.class);
            averagePower = (double ) get.invoke(o, "cpu.active.cluster0", level);
            Log.v(TAG, "averagePower " + averagePower + "   level " + level);
        } catch (Exception e) {
            averagePower =-1;
            e.printStackTrace();
            Log.e(TAG, "getAveragePower"+e.getMessage());
        }finally {
            return averagePower;
        }
    }
}

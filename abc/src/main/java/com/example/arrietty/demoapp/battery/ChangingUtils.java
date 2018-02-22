package com.example.arrietty.demoapp.battery;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by asus on 2017/12/7.
 */

public class ChangingUtils {
    public static final String TAG ="DemoApp.ChangingUtils";
    public static final String FILE_PATH_DC_VOL="/sys/class/power_supply/ac/voltage_now";
    public static final String FILE_PATH_DC_CURRENT="/sys/class/power_supply/ac/current_now";
    public static final String FILE_PATH_USB_VOL="/sys/class/power_supply/usb/voltage_now";
    public static final String FILE_PATH_USB_CURRENT="/sys/class/power_supply/usb/current_now";
    public static final String FILE_PATH_BATTERY_VOL="/sys/class/power_supply/battery/voltage_now";
    public static final String FILE_PATH_BATTERY_CURRENT="/sys/class/power_supply/battery/current_now";
    public static final String FILE_PATH_CHARGER_UEVENT= "sys/class/power_supply/battery/uevent";

    /**
     * 获取当前电流
     */
    public static String getCurrent() {
        String result = "null";
        try {
            Class systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getDeclaredMethod("get", String.class);
            String platName = (String) get.invoke(null, "ro.hardware");
            if (platName.startsWith("mt") || platName.startsWith("MT")) {
                String filePath = "/sys/class/power_supply/battery/device/FG_Battery_CurrentConsumption";
                // MTK平台该值不区分充放电，都为负数，要想实现充放电电流增加广播监听充电状态即可
                result = "当前电流为：" + Math.round(getMeanCurrentVal(filePath, 5, 0) / 10.0f) + "mA";
                result += ", 电压为：" + readFile("/sys/class/power_supply/battery/batt_vol", 0) + "mV";
                Log.v(TAG, "getCurrent platName= mt ， result " + result );
            } else if (platName.startsWith("qcom")) {

                //getCurrentInfoByPath(FILE_PATH_DC_CURRENT, FILE_PATH_DC_VOL);
                //getCurrentInfoByPath(FILE_PATH_USB_CURRENT, FILE_PATH_USB_VOL);
                result = getCurrentInfoByPath(FILE_PATH_BATTERY_CURRENT, FILE_PATH_BATTERY_VOL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getCurrent catch e.getMessage() " + e.getMessage());
        }
        return result;
    }
    public static  String  getCurrentInfoByPath(String filePathCur, String filePathVol){
        String result = "null";
        int current = Math.round(getMeanCurrentVal(filePathCur, 5, 0) / 10.0f);
        int voltage = readFile(filePathVol, 0) / 1000;
        // 高通平台该值小于0时电池处于放电状态，大于0时处于充电状态
        if (current < 0) {
            result = "充电电流为：" + (-current) + "mA, 电压为：" + voltage + "mV";
        } else {
            result = "放电电流为：" + current + "mA, 电压为：" + voltage + "mV";
        }
        Log.v(TAG, "getCurrent platName= qcom ， result " + result +"   filePathCur " +filePathCur);
        return result;
    }

    /**
     * 获取平均电流值
     * 获取 filePath 文件 totalCount 次数的平均值，每次采样间隔 intervalMs 时间
     */
    public static float getMeanCurrentVal(String filePath, int totalCount, int intervalMs) {
        float meanVal = 0.0f;
        if (totalCount <= 0) {
            return 0.0f;
        }
        for (int i = 0; i < totalCount; i++) {
            try {
                float f = Float.valueOf(readFile(filePath, 0));
                meanVal += f / totalCount;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (intervalMs <= 0) {
                continue;
            }
            try {
                Thread.sleep(intervalMs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return meanVal;
    }

    public static int readFile(String path, int defaultValue) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    path));
            int i = Integer.parseInt(bufferedReader.readLine(), 10);
            bufferedReader.close();
            return i;
        } catch (IOException e) {
            Log.e(TAG, "e.getMessage() " + e.getMessage());
        }
        return defaultValue;

    }


    public static String readTextFile(File file, int max, String ellipsis) throws IOException {
        InputStream input = new FileInputStream(file);
        try {
            long size = file.length();
            if (max > 0 || (size > 0 && max == 0)) {  // "head" mode: read the first N bytes
                if (size > 0 && (max == 0 || size < max)) max = (int) size;
                byte[] data = new byte[max + 1];
                int length = input.read(data);
                if (length <= 0) return "";
                if (length <= max) return new String(data, 0, length);
                if (ellipsis == null) return new String(data, 0, max);
                return new String(data, 0, max) + ellipsis;
            } else if (max < 0) {  // "tail" mode: keep the last N
                int len;
                boolean rolled = false;
                byte[] last = null, data = null;
                do {
                    if (last != null) rolled = true;
                    byte[] tmp = last;
                    last = data;
                    data = tmp;
                    if (data == null) data = new byte[-max];
                    len = input.read(data);
                } while (len == data.length);

                if (last == null && len <= 0) return "";
                if (last == null) return new String(data, 0, len);
                if (len > 0) {
                    rolled = true;
                    System.arraycopy(last, len, last, 0, last.length - len);
                    System.arraycopy(data, 0, last, last.length - len, len);
                }
                if (ellipsis == null || !rolled) return new String(last);
                return ellipsis + new String(last);
            } else {  // "cat" mode: size unknown, read it all in streaming fashion
                ByteArrayOutputStream contents = new ByteArrayOutputStream();
                int len;
                byte[] data = new byte[1024];
                do {
                    len = input.read(data);
                    if (len > 0) contents.write(data, 0, len);
                } while (len == data.length);
                return contents.toString();
            }
        } finally {
            input.close();
        }
    }
    //获取点容量
    public static HashMap<String, Object> getPowerProfileInfo(Activity activity){
        try {
            HashMap<String, Object> powerInfo = new HashMap<>();
            Class powerProfile = Class.forName("com.android.internal.os.PowerProfile");
            Constructor constructor = powerProfile.getConstructor(new Class[]{Context.class});
            Object o = constructor.newInstance(activity);
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
                if (value instanceof Double[]) {
                    Double[] vs = (Double[]) value;
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for (double v : vs) {
                        sb.append(v + ",");
                    }
                    sb.append("]");
                    Log.i("power", key + ":" + sb.toString());
                } else {
                    Log.i("power", key + ":" + value.toString());
                }
            }
            //zfuyan start
            Method getBatteryCapacity = powerProfile.getMethod("getBatteryCapacity");
            Log.v("power", "getBatteryCapacity " + getBatteryCapacity);
            if(getBatteryCapacity!=null){
                double  capacity = (double ) getBatteryCapacity.invoke(o);
                Log.v("power", "capacity " + capacity);
            }
            Method get = powerProfile.getDeclaredMethod("getAveragePower", String.class);
            Log.v("power", "get " + get);
            if(get!=null){
                double  averagePower = (double ) get.invoke(o, "cpu.active.cluster0");
                Log.v("power", "averagePower " + averagePower);
            }
            return powerInfo;
            //zfuyan end
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

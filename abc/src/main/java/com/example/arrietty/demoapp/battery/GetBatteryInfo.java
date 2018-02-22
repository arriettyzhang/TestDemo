package com.example.arrietty.demoapp.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arrietty.demoapp.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus on 2017/12/6.
 */

public class GetBatteryInfo extends AppCompatActivity {
    private static final String TAG ="DemoApp.GetBatteryInfo";
    private BatteryReceive mReceive;
    @BindView(R.id.batteryHealth)
     TextView batteryHealth;
    @BindView(R.id.batteryLevelScale)
     TextView batteryLevelScale;
    @BindView(R.id.batteryStatus)
     TextView batteryStatus;
    @BindView(R.id.batteryPluged)
    TextView batteryPluged;
    @BindView(R.id.batteryTechnology)
     TextView batteryTechnology;
    @BindView(R.id.batteryTemperature)
     TextView batteryTemperature;
    @BindView(R.id.batteryVoltage)
     TextView batteryVoltage;
    @BindView(R.id.changeEnent)
    TextView changeEnent;
    private boolean mIsActivity = true;
    private Toast mToast;
    private static final int MSG_WHAT_SHOW_CURRENT_VAL=1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_WHAT_SHOW_CURRENT_VAL:
                    showToast(ChangingUtils.getCurrent());
                    if (mIsActivity) {
                        // 因为Toast.LENGTH_SHORT的默认值是2000
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_SHOW_CURRENT_VAL, 1900);
                    }
                    break;
            }

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        Log.v(TAG, "111111111batteryHealth " + batteryHealth);
        ButterKnife.bind(this);
        Log.v(TAG, "22222batteryHealth " + batteryHealth);
        IntentFilter filter2 = new IntentFilter();
        //ACTION_BATTERY_LOW ACTION_BATTERY_OKAY
        filter2.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter2.addAction(Intent.ACTION_POWER_CONNECTED);
        filter2.addAction(Intent.ACTION_POWER_DISCONNECTED);
        mReceive = new BatteryReceive();
        registerReceiver(mReceive, filter2);
        mHandler.sendEmptyMessage(MSG_WHAT_SHOW_CURRENT_VAL);
        getCurrentVal();
        getEvent();
        LocalPowerProfile.getInstance(this).getPowerProfileInfo();
        LocalPowerProfile.getInstance(this).getTotalBatteryCapacity();

    }
    private void getCurrentVal(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            BatteryManager batteryManager=(BatteryManager)getSystemService(BATTERY_SERVICE);
            Log.v(TAG, "BATTERY_PROPERTY_CHARGE_COUNTER" + batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER));
            Log.v(TAG, "BATTERY_PROPERTY_CAPACITY" + batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
            Log.v(TAG, "BATTERY_PROPERTY_CURRENT_AVERAGE" + batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE));
            Log.v(TAG, "BATTERY_PROPERTY_CURRENT_NOW" + batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW));
            Log.v(TAG, "BATTERY_PROPERTY_ENERGY_COUNTER" + batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER));

        }

    }

    private void getEvent(){
        try {
            String mode1 = ChangingUtils.readTextFile(new File(ChangingUtils.FILE_PATH_CHARGER_UEVENT), 0, null).trim();
            changeEnent.setText(mode1);
            Log.e(TAG,mode1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG,"getEvent e.getMessage() " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void batteryInfoChange(PhoneBatteryInfo info){
        String status;
        if(info.getStatus() == BatteryManager.BATTERY_STATUS_CHARGING){
            status = getResources().getString(R.string.battery_status_changing);
        }else if(info.getStatus() == BatteryManager.BATTERY_STATUS_DISCHARGING){
            status = getResources().getString(R.string.battery_status_dischanging);
        }else if(info.getStatus() == BatteryManager.BATTERY_STATUS_FULL){
            status = getResources().getString(R.string.battery_status_full);
        }else if(info.getStatus() == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
            status = getResources().getString(R.string.battery_status_not_changing);
        }else{
            status = getResources().getString(R.string.battery_status_unknown);
        }
            String health;
        if(info.getHealth() == BatteryManager.BATTERY_HEALTH_COLD){
            health = getResources().getString(R.string.battery_health_cold);
        }else if(info.getHealth() == BatteryManager.BATTERY_HEALTH_DEAD){
            health = getResources().getString(R.string.battery_health_dead);
        }else if(info.getHealth() == BatteryManager.BATTERY_HEALTH_GOOD){
            health = getResources().getString(R.string.battery_health_good);
        }else if(info.getHealth() == BatteryManager.BATTERY_HEALTH_OVERHEAT){
            health = getResources().getString(R.string.battery_health_overheat);
        }else if(info.getHealth() == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
            health = getResources().getString(R.string.battery_health_unspecifed_failure);
        }else if(info.getHealth() == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
            health = getResources().getString(R.string.battery_health_over_voltage);
        }else{
            health = getResources().getString(R.string.battery_health_unknown);
        }
        StringBuilder  pluged = new StringBuilder();
        if((info.getPlugged() & BatteryManager.BATTERY_PLUGGED_AC)>0 ){
            if( pluged.length()>0){
                pluged.append("|") ;
            }
            pluged.append(getResources().getString(R.string.battery_plugged_ac));
        }
        Log.v(TAG, "info.getPlugged() & BatteryManager.BATTERY_PLUGGED_USB " + (info.getPlugged() & BatteryManager.BATTERY_PLUGGED_USB));
        if((info.getPlugged() & BatteryManager.BATTERY_PLUGGED_USB)>0){
            if( pluged.length()>0){
                pluged.append("|") ;
            }
            pluged.append(getResources().getString(R.string.battery_plugged_usb));
        }
        if((info.getPlugged() & BatteryManager.BATTERY_PLUGGED_WIRELESS)>0 ){
            if( pluged.length()>0){
                pluged.append("|") ;
            }
            pluged.append(getResources().getString(R.string.battery_plugged_wireless));
        }
        batteryStatus.setText(getResources().getString(R.string.battery_status, status));
        batteryHealth.setText(getResources().getString(R.string.battery_health, health));
        batteryPluged.setText(getResources().getString(R.string.battery_plugged, pluged.toString()));
        batteryLevelScale.setText(getResources().getString(R.string.battery_level_scale, info.getLevel()* 100 /info.getScale()+"%"));
        batteryVoltage.setText(getResources().getString(R.string.battery_voltage, info.getVoltage()+""));
        batteryTemperature.setText(getResources().getString(R.string.battery_temperature, (info.getTemperature()*1.0 / 10)+""));
        batteryTechnology.setText(getResources().getString(R.string.battery_technology, info.getTechnology()+""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceive);
        mHandler.removeMessages(MSG_WHAT_SHOW_CURRENT_VAL);
        mIsActivity = false;
    }
    private void showToast(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(GetBatteryInfo.this, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private class BatteryReceive extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(Intent.ACTION_BATTERY_CHANGED.equals(action)){
                PhoneBatteryInfo info = new PhoneBatteryInfo();
                info.setStatus(intent.getIntExtra(BatteryManager.EXTRA_STATUS,0));
                info.setLevel(intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0));
                info.setScale(intent.getIntExtra(BatteryManager.EXTRA_SCALE,0));
                info.setVoltage(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0));
                info.setHealth(intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0));
                info.setTemperature(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0));
                info.setTechnology(intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));
                info.setIcon_small(intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0));
                info.setPlugged(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0));
                info.setPresent(intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT,false));
                Log.v(TAG, info.toString());
                batteryInfoChange(info);
            }
        }
    }
}

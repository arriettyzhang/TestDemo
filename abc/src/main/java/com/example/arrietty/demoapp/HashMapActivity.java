package com.example.arrietty.demoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by asus on 2017/11/9.
 */

public class HashMapActivity extends Activity {
    private static final String TAG = "HashMapActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hhhh);
        hashMapFun();
        arrayMapFun();

    }
    private void hashMapFun(){
        HashMap<String, String> hm = new HashMap<>();
        hm.put("key1", "value1");
        hm.put("key2", "value2");
        hm.put("key3", "value3");
        hm.put("key4", "value4");
        hm.put("key5", "value5");
        hm.put("key6", "value6");
        hm.put("key7", "value7");
        hm.put("key6", "value8");
        hm.put(null, "value null");
        Log.v(TAG, "hm = "+hm);

        Set<String>keys = hm.keySet();
        Set<Map.Entry<String, String>> entry = hm.entrySet();
        Collection<String>values = hm.values();
        for(Iterator<String>it=keys.iterator();it.hasNext();){
            String key = it.next();
            String value = hm.get(key);
            Log.v(TAG, key+" - "+value);
        }
        for(Iterator<Map.Entry<String , String>> it = entry.iterator(); it.hasNext();){
            Map.Entry<String,String>ent= it.next();
            String key = ent.getKey();
            String value = ent.getValue();
            Log.v(TAG, "entry=" +key+" - "+value);
        }
        for(Iterator<String>it=values.iterator();it.hasNext();){
            String value = it.next();
            Log.v(TAG, "values= "+value);
        }



    }
    private void arrayMapFun(){
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key1", "value3");
        Log.v(TAG,"map.size()="+ map.size());
        Log.v(TAG,"map="+ map);
        map.remove("key2");
        Log.v(TAG,"map.size()="+ map.size());
        Log.v(TAG,"map="+ map);

    }


}

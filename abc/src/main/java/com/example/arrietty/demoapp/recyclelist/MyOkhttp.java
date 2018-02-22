package com.example.arrietty.demoapp.recyclelist;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by 赵晨璞 on 2016/6/16.
 */
public class MyOkhttp {
    private static final String TAG ="DemoApp.MyOkhttp";

    public static OkHttpClient client = new OkHttpClient();

    public static String get(String url){
        try {
         client.newBuilder().connectTimeout(10000, TimeUnit.MILLISECONDS);
        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
            Log.v(TAG , "response.isSuccessful() " +response.isSuccessful());
        if (response.isSuccessful()) {

            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG , "ioexcepter " +e.getMessage());
        }finally {

        }
        return null;
    }


}

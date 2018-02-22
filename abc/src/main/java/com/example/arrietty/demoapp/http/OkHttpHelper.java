package com.example.arrietty.demoapp.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.example.arrietty.demoapp.utils.BitmapUtils;
import com.example.arrietty.demoapp.utils.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public final class OkHttpHelper {

    private static final String TAG = "OkHttpHelper";


    public static final int CONNECT_TIMEOUT_S = 10;
    public static final int READ_TIMEOUT = 10;
    public static final int WRITE_TIMEOUT = 10;

    public static OkHttpClient getClient() {
        return getInstance().mClient;
    }

    public static final String DEFAULT_HEADER_KEY_CATAGORY = "category";
    public static final String DEFAULT_HEADER_VALUE_CATAGORY = "Home";

    public static final String DEFAULT_HEADER_KEY_TOKEN = "token";
    public static final String DEFAULT_HEADER_KEY_UID = "uid";
    public static final String udid = Build.SERIAL;

    // reserve all the running calls for cancel.
    private final ArrayMap<String, Set<Call>> mRunningCalls;

    private static OkHttpHelper mInstance;
    static private OkHttpClient mClient;
    private Handler mHandler;

    public static String[] headers;
    private static final int TYPE_GET=0;
    private static final int TYPE_POST=1;
    private static final int TYPE_PUT=2;
    private static final int TYPE_DELETE=3;

    private synchronized static OkHttpHelper getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpHelper();
        }
        return mInstance;
    }

    private OkHttpHelper() {
        mClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT_S, TimeUnit.SECONDS)//设置连接超时时间
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        mHandler = new Handler(Looper.getMainLooper());
        mRunningCalls = new ArrayMap<>();
    }

    static void addDefaultHeaders(Request.Builder builder) {
        builder.addHeader(DEFAULT_HEADER_KEY_CATAGORY, DEFAULT_HEADER_VALUE_CATAGORY);
        builder.addHeader(DEFAULT_HEADER_KEY_TOKEN, DEFAULT_HEADER_KEY_TOKEN);
        builder.addHeader(DEFAULT_HEADER_KEY_UID, DEFAULT_HEADER_KEY_UID);
        builder.addHeader("openudid", udid);
        builder.addHeader("language", java.util.Locale.getDefault().getLanguage());
        builder.addHeader("country", java.util.Locale.getDefault().getCountry());
    }

    //==============================get====================================
    //get的同步方式execute()
    public static String get(String url) {
        Request.Builder builder = new Request.Builder().url(url).get();
        addDefaultHeaders(builder);
        Request request = builder.build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            Log.d(TAG, "IOException:" + e);
            e.printStackTrace();
        }finally {
            if(response==null){
                return null;
            }
        }

        if (!response.isSuccessful()){
            try {
                throw new IOException("Unexpected code " + response);
            } catch (IOException e) {
                Log.d(TAG, "IOException:" + e);
                e.printStackTrace();
            }finally {
                return null;
            }
        }

        Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
        }
        try {
            return response.body().string();
        } catch (IOException e) {
            Log.d(TAG, "IOException:" + e);
            e.printStackTrace();
        }
        return null;
    }
    /*
    *tag用来取消请求
    *action用来区分事件
     */
//get 的异步方法enqueue（）

    public static void getAsync(Context context, String url, OnReqCallBack callback, String tag, String action) {
        if (!NetworkUtil.isConnected(context)) {
            if (callback != null) {
                callback.onReqNoNetwork(tag);
                return;
            }
        }
        Request.Builder builder = new Request.Builder().url(url).get();
        if (tag != null) builder.tag(tag);
        addDefaultHeaders(builder);
        Request request = builder.build();
        getInstance().requestAsyn(TYPE_GET,request, callback, action);
        //getInstance().enqueueGet(request, callback, action);
    }

    private void enqueueGet(Request request, final OnReqCallBack callback,final String action) {
        Call call = mClient.newCall(request);
        cacheCallForCancel(call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (isCancel(call)) return;
                failedCallBack(call,e, e.getMessage(),action,callback);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (isCancel(call)) return;
                final String s = response.body().string();
                if(response.isSuccessful()){
                    final String body = response.body().string();
                    successCallBack(call,body,action,callback);
                }else{
                    failedCallBack(call,null,"get failed",action,callback);
                }
            }
        });
    }
    //================================end get======================


    //=====================================delete==============================
    public static void delete(Context context, String url, OnReqCallBack callback, String tag, String action) {
        if (!NetworkUtil.isConnected(context)) {
            if (callback != null) {
                callback.onReqNoNetwork(tag);
                return;
            }
        }
        Request.Builder builder = new Request.Builder().url(url).delete();
        if (tag != null) builder.tag(tag);
        addDefaultHeaders(builder);
        Request request = builder.build();

        getInstance().requestAsyn(TYPE_DELETE,request, callback, action);
    }

    //======================================end delete===========================

    //=========================================post==============================
/*
*tag用于cancle时使用
 */

    public static void post(Context context, String url, String tag, String action, OnReqCallBack callback) {
        if (!NetworkUtil.isConnected(context)) {
            if (callback != null) {
                callback.onReqNoNetwork(tag);
                return;
            }
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "");
        Request.Builder builder = new Request.Builder().url(url).post(body);
        addDefaultHeaders(builder);
        if (tag != null) builder.tag(tag);
        Request request = builder.build();
        getInstance().requestAsyn(TYPE_POST,request, callback, action);
    }
    /*
    tag 用于取消call使用
    action用于标识事件
     */
    public static void post(Context context,String url, Map<String, String> params, String tag, String action,OnReqCallBack callback) {

        JSONObject j = new JSONObject(params);
        post(context,url, j, tag, action, callback);
    }
    public static void post(Context context, String url, JSONObject j, String tag, String action,OnReqCallBack callback) {
        if (!NetworkUtil.isConnected(context) && !tag.equals("bindDevice")) {
            if (callback != null) {
                callback.onReqNoNetwork(tag);
                return;
            }
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), j.toString());
        Request.Builder builder = new Request.Builder().url(url).post(body);
        addDefaultHeaders(builder);
        if (tag != null) builder.tag(tag);
        Request request = builder.build();
        getInstance().requestAsyn(TYPE_POST, request, callback, action);
    }

    public static void post(Context context, String url, String[] hkeys, String[] hvalues, JSONObject j, String tag, String action, OnReqCallBack callback) {
        if (!NetworkUtil.isConnected(context)) {
            if (callback != null) {
                callback.onReqNoNetwork(tag);
                return;
            }
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), j.toString());
        Request.Builder builder = new Request.Builder().url(url).post(body);
        addDefaultHeaders(builder);
        for (int i = 0; i < hkeys.length; i++) {
            builder.addHeader(hkeys[i], hvalues[i]);
        }

        if (tag != null) builder.tag(tag);
        Request request = builder.build();
        getInstance().requestAsyn(TYPE_POST, request, callback, action);
    }

    /**
     * upload image file to the server
     *
     * @param url      url
     * @param param    param
     * @param path     file path
     * @param type     MIME type 不同的文件类型，mime type是不一样的
     * @param tag      tag of context, where the function called.
     * @param callBack callback
     */

    public static void uploadFile(String url, Map<String, String> param, String path, MediaType type, String tag, String action, OnReqCallBack callBack) {

        File file = new File(path);
        MultipartBody.Builder mBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//设置表单
        //追加参数
        if(param!=null){
            for (String key : param.keySet()) {
                String value = param.get(key);
                mBuilder.addFormDataPart(key, value);
            }
        }
        mBuilder.addFormDataPart("name", file.getName(), RequestBody.create(type, file));
        RequestBody requestBody = mBuilder.build();
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        addDefaultHeaders(builder);
        if (tag != null) builder.tag(tag);
        Request request = builder.build();
        getInstance().requestAsyn(TYPE_POST, request, callBack, action);

    }
    /*
    type =MediaType.parse("image/png")
     */
    public  void uploadBitmap(String url, Map<String, String> param, String bitmapPath, String tag, String action,OnReqCallBack callBack) {
        File file = new File(bitmapPath);
        if (!file.exists()) {
            Log.e(TAG, "file not exist");
            failedCallBack(null,null,"file not exist",action,callBack);
        } else {
            uploadBitmap(url, param, BitmapUtils.readFromFileAndSize(bitmapPath), tag, action, callBack);
        }

    }
    public  void uploadBitmap(String url, Map<String, String> param, Bitmap bitmap, String tag, String action, OnReqCallBack callBack) {
        JSONObject jsonObject = new JSONObject(param);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//设置表单
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"json\""),
                        RequestBody.create(MediaType.parse("application/json"), jsonObject.toString()))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"avatar\";filename=\"file.jpg\""),
                        RequestBody.create(MediaType.parse("image/png"), byteArray)
                ).build();
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        addDefaultHeaders(builder);
        if (tag != null) builder.tag(tag);
        Request request = builder.build();
        getInstance().requestAsyn(TYPE_POST, request, callBack, action);

    }

    //==============================end post======================

    //===================================put===============================

    public static void put(Context context, String url, JSONObject j, String tag, OnReqCallBack callback, String action) {
        if (!NetworkUtil.isConnected(context)) {
            if (callback != null) {
                callback.onReqNoNetwork(tag);
                return;
            }
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), j.toString());
        Request.Builder builder = new Request.Builder().url(url).put(body);

        addDefaultHeaders(builder);
        if (tag != null) builder.tag(tag);
        Request request = builder.build();
        getInstance().requestAsyn(TYPE_PUT,request, callback, action);
    }


    private void requestAsyn(int type, Request request, final OnReqCallBack callback, final String action) {
        final String notify;
        switch (type){
            case TYPE_GET:
                notify ="get failed";
                break;
            case TYPE_POST:
                notify ="post failed";
                break;
            case TYPE_PUT:
                notify ="put failed";
                break;
            case TYPE_DELETE:
                notify ="delete failed";
                break;
            default:
                return;
        }
        Call call = mClient.newCall(request);
        cacheCallForCancel(call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (isCancel(call)) return;
                failedCallBack(call,e,e.getMessage(),action,callback);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (isCancel(call)) return;
                if(response.isSuccessful()){
                    final String body = response.body().string();
                    successCallBack(call,body,action,callback);
                }else{
                    failedCallBack(call,null,notify,action,callback);
                }
            }
        });
    }

    private void cacheCallForCancel(Call call) {
        String tag = (String) (call.request().tag());
        if (tag != null) {
            synchronized (mRunningCalls) {
                Set<Call> calls = mRunningCalls.get(tag);
                if (calls == null) {
                    calls = new HashSet<>();
                }
                calls.add(call);
                mRunningCalls.put(tag, calls);
            }
        }
    }

    private static String printRunningCalls() {
        return "\n" + getInstance().mRunningCalls.toString();
    }

    private String printCall(Call call) {
        return "call: " + call.toString() + " with tag " + call.request().tag();
    }

    private boolean isCancel(Call call) {
        return isCancel(call, false);
    }

    private boolean isCancel(Call call, boolean remove) {
        String tag = (String) call.request().tag();
        if (tag == null) return false;
        synchronized (mRunningCalls) {
            Set<Call> calls = mRunningCalls.get(tag);
            if (calls == null || !calls.contains(call)) return true;
            if (remove) {
                calls.remove(call);
                if (calls.isEmpty()) {
                    mRunningCalls.remove(tag);
                    //LogUtils.d(TAG, "finish remove Call: " + printCall(call) + " \ncurrent running calls: " + printRunningCalls());
                }
            }
        }
        return false;
    }
//清除tag请求,可能是多个call
    public static void cancel(String tag) {
        getInstance().mRunningCalls.remove(tag);
        for(Call call : mClient.dispatcher().queuedCalls()) {
            if(call.request().tag().equals(tag))
                call.cancel();
        }
        for(Call call : mClient.dispatcher().runningCalls()) {
            if(call.request().tag().equals(tag))
                call.cancel();
        }
    }

    public static void cancelAllRequest() {
        getInstance().mRunningCalls.clear();
        mClient.dispatcher().cancelAll();
    }

    /**
     * 上传文件
     *
     * @param actionUrl 接口地址
     * @param paramsMap 参数,可上传多个文件
     * @param callBack  回调

     */
    public  void upLoadLogFile(String actionUrl, HashMap<String, Object> paramsMap, String tag ,final String action,final OnReqCallBack callBack) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            Request.Builder requestBuilder = new Request.Builder().url(actionUrl).post(body);
            if (tag != null) requestBuilder.tag(tag);
           Request request =  requestBuilder .build();

            Call call = mClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, e.toString());
                    if (isCancel(call)) return;
                    failedCallBack(call,e, e.getMessage(),action,callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e("log", "response -----> " + response.message());
                    if(isCancel(call))return;
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        successCallBack(call,  json, action,callBack);
                    } else {
                        failedCallBack(call, null,"upload failed",action, callBack);
                        Log.e("log", "response -----> failed");
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
    public interface OnReqCallBack {
        void onReqFailure(String errMsg, String action);

        void onReqTimeout(String action);

        void onReqSuccess(String result, String action) throws JSONException;

        void onReqNoNetwork(String action);
    }
    private <T> void successCallBack(final Call call, final String result, final String action, final OnReqCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isCancel(call, true)) return;
                if (callBack != null) {
                    try {
                        callBack.onReqSuccess(result, action);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private <T> void failedCallBack(final Call call, final IOException e, final String errMsg,final String action, final OnReqCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                if(call ==null){
                    callBack.onReqFailure(errMsg, action);
                    return;
                }
                if (isCancel(call, true)) return;
                if (callBack != null) {
                    if(e!=null){
                        if(e.getCause().equals(SocketTimeoutException.class)) {
                            callBack.onReqTimeout(action);
                        } else {
                            callBack.onReqFailure(errMsg, action);
                        }
                    }else{
                        callBack.onReqFailure(errMsg, action);
                    }

                }
            }
        });
    }

}

package com.example.arrietty.demoapp.retrofit;

import android.content.Context;
import android.database.Observable;
import android.os.Environment;
import android.util.Log;

import com.example.arrietty.demoapp.retrofit.model.PhoneInfo;
import com.example.arrietty.demoapp.utils.CacheUtil;
import com.example.arrietty.demoapp.retrofit.okhttp.interceptor.OfflineCacheControlInterceptor;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asus on 2017/12/14.
 */

public class RetrofitServiceManager {
    public static String BASE_URL = "http://192.168.1.8:8080/UploadFile/";
    private static final String API_KEY = "8e13586b86e4b7f3758ba3bd6c9c9135";
    private static RetrofitServiceManager instance;
    private static RetrofitAPI retrofitService;
    private Context mContext;
    private RetrofitServiceManager(Context context){
        mContext = context.getApplicationContext();
        retrofitService =  new Retrofit2CLient(mContext).getRetrofitBuilder()
                .baseUrl(BASE_URL)
                .build()
                .create(RetrofitAPI.class);
    }
    public static synchronized RetrofitServiceManager getInstance(Context context){
        if(instance == null){
            instance = new RetrofitServiceManager( context);
        }
        return instance;
    }
    private OkHttpClient getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OfflineCacheControlInterceptor cacheInterceptor = new OfflineCacheControlInterceptor(mContext);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //打印日志
                .addInterceptor(interceptor)

                //设置Cache目录
                .cache(CacheUtil.getCache())

                //设置缓存
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                //失败重连
                .retryOnConnectionFailure(true)
                //time out
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)

                .build();

        return okHttpClient;

    }
    public void getResult(String user, String phoneNum){
        Call<PhoneInfo> call = retrofitService.getResult(user, API_KEY, phoneNum);

        //3.发送请求
        call.enqueue(new Callback<PhoneInfo>() {
            @Override
            public void onResponse(Call<PhoneInfo> call, Response<PhoneInfo> response) {
                Headers heads =response.headers();
                String message = response.message();
                ResponseBody pby= response.errorBody();
                pby.toString();
//主线程
                //4.处理结果
                if (response.isSuccessful()){
                    PhoneInfo result = response.body();
                }
            }

            @Override
            public void onFailure(Call<PhoneInfo> call, Throwable t) {

            }
        });
    }
    public void searchPsw(String owner, String number){
        Call<PhoneInfo> call = retrofitService.searchPsw(owner, number);

        //3.发送请求
        call.enqueue(new Callback<PhoneInfo>() {
            @Override
            public void onResponse(Call<PhoneInfo> call, Response<PhoneInfo> response) {
                //4.处理结果
                if (response.isSuccessful()){
                    PhoneInfo result = response.body();
                }
            }

            @Override
            public void onFailure(Call<PhoneInfo> call, Throwable t) {

            }
        });
    }
    /*
    单文件上传携带参数
     */
    public void uploadfile(String fileName, String userName, String password){
        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", fileName);
        //设置Content-Type:application/octet-stream
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //设置Content-Disposition:form-data; name="photo"; filename="xuezhiqian.png"
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), photoRequestBody);
        //添加参数用户名和密码，并且是文本类型
        RequestBody userNameBody = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody passWordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        Call<ResponseBody> loadCall = retrofitService.uploadfile(photo, userNameBody,passWordBody);
        loadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("APP", response.body().source().toString());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

/*
    多文件上传携带参数
     */
    public void upLoadMultiFiles(String fileName1, String fileName2, String userName, String password){
        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", fileName1);
        File file2 = new File(Environment.getExternalStorageDirectory()+"/Pictures", fileName2);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody photoRequestBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), file2);
        RequestBody userNameBody = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody passWordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        Map<String,RequestBody> photos = new HashMap<>();
        //添加文件一
        photos.put("photos\"; filename=\""+file.getName(), photoRequestBody);
     //添加文件二
        photos.put("photos\"; filename=\""+file2.getName(), photoRequestBody2);
        //添加用户名参数
        photos.put("username",  userNameBody);
        Call<ResponseBody> loadCall = retrofitService.uploadMultifiles(photos, passWordBody);
        loadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("APP", response.body().source().toString());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

}
        private void Login(PhoneInfo info){
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(info));

            Observable<PhoneInfo> login =  retrofitService.Login(body);

        }

}

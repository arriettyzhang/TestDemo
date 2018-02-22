package com.example.arrietty.demoapp.retrofit;

import android.content.Context;

import com.example.arrietty.demoapp.utils.CacheUtil;
import com.example.arrietty.demoapp.retrofit.okhttp.interceptor.OfflineCacheControlInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asus on 2017/12/18.
 */

public class   Retrofit2CLient {
    private Context mContext;
    private final Retrofit.Builder retrofitBuilder;
     public  Retrofit2CLient(Context context){
        mContext = context.getApplicationContext();
        retrofitBuilder = new Retrofit.Builder()
                .client(getClient())
                // .addConverterFactory(ScalarsConverterFactory.create())//增加返回值为String的支持
                //.addConverterFactory(ProtoConverterFactory.create())//protobuf类型，需要卸载Gson前面，否则会以为是json
                .addConverterFactory(GsonConverterFactory.create())//增加返回值为Gson的支持(以实体类返回)
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//增加返回值为Oservable<T>的支持
               ;
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
    public Retrofit.Builder getRetrofitBuilder() {
        return retrofitBuilder;
    }
}

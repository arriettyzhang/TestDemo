package com.example.arrietty.demoapp.retrofit.okhttp.interceptor;

import android.content.Context;
import android.util.Log;

import com.example.arrietty.demoapp.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by asus on 2017/12/15.
 * 离线读取本地缓存，在线获取最新数据(读取单个请求的请求头，亦可统一设置)。
 */

public class OfflineCacheControlInterceptor implements Interceptor {
    private static final String TAG = "OfflineCacheControl";
    private Context mContext;
    public OfflineCacheControlInterceptor(Context context){
        mContext = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            request = request.newBuilder()
                    //强制使用缓存
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);

        if (NetworkUtil.isNetworkAvailable(mContext)) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            Log.v(TAG, "has network ,cacheControl=" + cacheControl);
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            Log.i(TAG, "network error ,maxStale="+maxStale);
            return response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale="+maxStale)
                    .removeHeader("Pragma")
                    .build();
        }

    }

}

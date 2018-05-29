//
// * Copyright © 2015-2018 Anker Innovations Technology Limited All Rights Reserved.
// * The program and materials is not free. Without our permission, any use, including but not limited to reproduction, retransmission, communication, display, mirror, download, modification, is expressly prohibited. Otherwise, it will be pursued for legal liability.

//
package com.example.arrietty.demoapp.http2;

import android.text.TextUtils;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * 网络请求基类request
 *
 * @author Cao Mingming
 * @date 2016-5-4 11:51:21
 */
public abstract class BaseOkHttpRequest {

    private final static String TAG = "BaseOkHttpRequest";

    /**
     * 调用get请求
     */
    public static final int METHOD_GET = 0;
    /**
     * 调用post请求
     */
    public static final int METHOD_POST = 1;
    /**
     * 调用put请求
     */
    public static final int METHOD_PUT = 2;
    /**
     * 调用delete请求
     */
    public static final int METHOD_DELETE = 3;

    private final static String MEDIA_TYPE_JSON = "application/json; charset=utf-8";

    /**
     * 网络请求结果标记
     */
    protected boolean result;

    /**
     * callback
     */
    private NetCallBack mCallBack;

    /**
     * request server
     *
     * @param id
     * @param service
     * @param body
     * @param headers
     * @param connTimeout
     * @param readTimeout
     * @param writeTimeout
     */
    protected void request(int id, String service, String body, Map<String, String> headers, long
            connTimeout, long readTimeout, long writeTimeout, int methodType) {

        if (mCallBack == null) {
            mCallBack = new NetCallBack();
        }

        OkHttpRequestBuilder requestBuilder = null;

        // 调用相应的方法
        switch (methodType) {
            case METHOD_GET:
                requestBuilder = OkHttpUtils.get();
                break;
            case METHOD_POST:
                requestBuilder = OkHttpUtils.postString();
                break;
            case METHOD_PUT:
                requestBuilder = OkHttpUtils.put();
                break;
            case METHOD_DELETE:
                requestBuilder = OkHttpUtils.delete();
                break;
            default:
                break;
        }

        requestBuilder.url(getRootUrl().concat(service)).headers(headers).tag(this).id(id);
        // 添加各自需要参数
        switch (methodType) {

            case METHOD_POST:
                ((PostStringBuilder) requestBuilder).content(body).mediaType(MediaType.parse
                        (MEDIA_TYPE_JSON));
                break;
            case METHOD_PUT:
            case METHOD_DELETE:
                ((OtherRequestBuilder) requestBuilder).requestBody(body);
                break;
            case METHOD_GET:
            default:
                break;
        }
        Log.e(TAG, "headers start==========");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            Log.e(TAG, header.getKey() + " -> " + header.getValue());
        }
        Log.e(TAG, "headers end==========\n");
        if (!TextUtils.isEmpty(body)) {
            Log.e(TAG, "body start==========\n");
            Log.e(TAG, body);
            Log.e(TAG, "body end==========\n");
        }
        requestBuilder.build().connTimeOut(connTimeout).readTimeOut(readTimeout).writeTimeOut
                (writeTimeout).execute(mCallBack);

    }

    /**
     * get root url
     *
     * @return
     */
    public abstract String getRootUrl();

    /**
     * 取消所有网络请求
     */
    public void cancelAllRequests() {
        OkHttpUtils.getInstance().cancelTag(this);
    }

    /**
     * 网络请求回调类
     *
     * @author Administrator
     */
    private class NetCallBack extends StringCallback {

        @Override
        public void onBefore(Request request, int id) {
            BaseOkHttpRequest.this.onBefore(id);
        }

        @Override
        public void onAfter(int id) {
            BaseOkHttpRequest.this.onAfter(result, id);
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            result = false;
            BaseOkHttpRequest.this.onError(call,e ,id);
        }

        @Override
        public void onResponse(String json, int id) {
            BaseOkHttpRequest.this.onResponse(json, id);
        }
    }

    /**
     * 请求后
     *
     * @param result
     * @param id
     */
    protected abstract void onAfter(boolean result, int id);

    /**
     * 请求前
     *
     * @param id
     */
    protected abstract void onBefore(int id);

    /**
     * 请求响应
     *
     * @param json
     * @param id
     */
    protected abstract void onResponse(String json, int id);

    /**
     *
     * @param call
     * @param e
     * @param id
     */
    protected abstract void onError(Call call, Exception e, int id);
}

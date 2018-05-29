//
// * Copyright © 2015-2018 Anker Innovations Technology Limited All Rights Reserved.
// * The program and materials is not free. Without our permission, any use, including but not limited to reproduction, retransmission, communication, display, mirror, download, modification, is expressly prohibited. Otherwise, it will be pursued for legal liability.

//
package com.example.arrietty.demoapp.http2;


import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * 网络请求基类model
 *
 * @author Administrator
 */
public abstract class BaseRequest extends BaseOkHttpRequest {

    /**
     * 网络请求回调
     */
    public WeakReference<OnResponseListener> weakReference;

    private RequestConfig requestConfig;

    public BaseRequest() {
        requestConfig = new RequestConfig();
    }

    public RequestConfig getRequestConfig() {
        return requestConfig.resetConfig();
    }


    /**
     * 请求服务器
     *
     * @param requestConfig
     * @param l
     */
    public void request(RequestConfig requestConfig, OnResponseListener l, int methodType) {
        if (weakReference == null) {
            weakReference = new WeakReference<>(l);
        }
        if (requestConfig == null) {
            throw new RuntimeException("RequestBuilder can not be null!");
        }
        requestConfig.headers = getHeaders();
        request(requestConfig.id, requestConfig.service, requestConfig.body, requestConfig
                .headers, requestConfig.connTimeout, requestConfig.readTimeout, requestConfig
                .writeTimeout, methodType);
    }

    public abstract Map<String, String> getHeaders();

    @Override
    protected void onBefore(int id) {
        if (weakReference.get() != null) {
            weakReference.get().onBefore(id);
        }

    }

    @Override
    protected void onAfter(boolean result, int id) {

        if (weakReference.get() != null) {
            weakReference.get().onAfter(result, id);
        }

    }

    public static class RequestConfig {
        private int id;
        private String service;
        private String body;
        private Map<String, String> headers;
        private long connTimeout = 10l * 1000;
        private long readTimeout = 10l * 1000;
        private long writeTimeout = 10l * 1000;

        public RequestConfig resetConfig() {
            this.id = 0;
            this.service = null;
            this.body = null;
            connTimeout = 10 * 1000L;
            readTimeout = 10 * 1000L;
            writeTimeout = 10 * 1000L;
            return this;
        }

        public RequestConfig setService(String service) {
            this.service = service;
            return this;
        }

        public RequestConfig setBody(String body) {
            this.body = body;
            return this;
        }

        public RequestConfig setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public RequestConfig setConnTimeout(long connTimeout) {
            this.connTimeout = connTimeout;
            return this;
        }

        public RequestConfig setReadTimeout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public RequestConfig setWriteTimeout(long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public RequestConfig setId(int id) {
            this.id = id;
            return this;
        }

    }

}

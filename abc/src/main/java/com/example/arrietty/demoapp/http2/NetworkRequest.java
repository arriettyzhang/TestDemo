//
// * Copyright © 2015-2018 Anker Innovations Technology Limited All Rights Reserved.
// * The program and materials is not free. Without our permission, any use, including but not limited to reproduction, retransmission, communication, display, mirror, download, modification, is expressly prohibited. Otherwise, it will be pursued for legal liability.

//
package com.example.arrietty.demoapp.http2;

import android.util.Log;

import com.example.arrietty.demoapp.BuildConfig;
import com.example.arrietty.demoapp.utils.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by MD01 on 2018/1/30.
 */

public class NetworkRequest extends BaseRequest {
    private static final String TAG ="NetworkRequest";

    private final static int SUCCESS_CODE = 1;

    public NetworkRequest() {
        super();
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new LinkedHashMap<>();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String token = MD5Util.md5Encrypt(timestamp + "FA77F824B", false);
        headers.put("timestamp", timestamp);
        headers.put("AnkerBG", "SPEAKER");
        headers.put("token", token);
        headers.put("language", Locale.getDefault().getLanguage());
        return headers;
    }

    @Override
    public String getRootUrl() {

        return BuildConfig.ROOT_URL;
    }

    @Override
    protected void onResponse(String json, int id) {

        Log.e(TAG, json);
        try {

            JSONObject jsonObj = new JSONObject(json);
            int responseCode = jsonObj.optInt("res_code", 0);
            result = responseCode == SUCCESS_CODE;
            if (weakReference != null && weakReference.get() != null) {

                if (responseCode == SUCCESS_CODE) {
                    weakReference.get().onSuccess(json, id);
                } else {
                    String errorMessage = jsonObj.optString("message", "");
                    weakReference.get().onNot200(errorMessage);
                }
            }
        } catch (JSONException e) {

            result = false;
        }
    }

    @Override
    protected void onError(Call call, Exception e, int id) {
        if (weakReference != null && weakReference.get() != null) {
            weakReference.get().onError(call, e, id);
        }
    }
}

//
// * Copyright © 2015-2018 Anker Innovations Technology Limited All Rights Reserved.
// * The program and materials is not free. Without our permission, any use, including but not limited to reproduction, retransmission, communication, display, mirror, download, modification, is expressly prohibited. Otherwise, it will be pursued for legal liability.

//
package com.example.arrietty.demoapp.http2;

import okhttp3.Call;

/**
 * server request listener
 *
 * @author MD01
 */
public interface OnResponseListener {

    /**
     * before server request callback
     *
     * @param id
     */
    void onBefore(int id);

    /**
     * after server request callback
     *
     * @param result
     * @param id
     */
    void onAfter(boolean result, int id);

    /**
     * server request success callback
     *
     * @param json
     * @param id
     */
    void onSuccess(String json, int id);

    void onNot200(String errorMessage);
    void onError(Call call, Exception e, int id);
}

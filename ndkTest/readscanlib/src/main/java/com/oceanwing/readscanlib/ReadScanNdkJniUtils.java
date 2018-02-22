package com.oceanwing.readscanlib;

/**
 * Created by asus on 2018/2/9.
 */

public class ReadScanNdkJniUtils {
    static {
        System.loadLibrary("audio_readscan_jni");
    }
    public static native int select_eq(String file_path);
}

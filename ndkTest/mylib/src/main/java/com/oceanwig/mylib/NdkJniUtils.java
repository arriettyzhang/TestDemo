package com.oceanwig.mylib;

/**
 * Created by asus on 2018/2/9.
 */

public class NdkJniUtils {
    static {
        System.loadLibrary("hello");//.so文件格式为:lib+库名+.so
    }
    public static native String getStringFromJni();//函数名与C代码的函数名保持一致
}

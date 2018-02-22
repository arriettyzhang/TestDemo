//
// Created by asus on 2018/2/9.
//
#include <jni.h>
JNIEXPORT jstring JNICALL Java_com_oceanwig_mylib_NdkJniUtils_getStringFromJni
    (JNIEnv* env, jclass obj){
    char *str ="Hello,NDK";
    jstring jstr = (*env)->NewStringUTF(env, str);
    return jstr;

    }


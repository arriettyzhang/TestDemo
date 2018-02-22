//
// Created by asus on 2018/2/9.
//
#include <jni.h>
#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdbool.h>
#include <android/log.h>

#include <sys/mman.h>
#include <string.h>
#include <math.h>

#include "audio_fft.h"
#include "audio_readscan.h"
#include "com_oceanwing_readscanlib_ReadScanNdkJniUtils.h"
#define LOG_TAG "UNZIP_AND_ENCRYPT"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

char* Jstring2CStr(JNIEnv* env, jstring jstr) {
     char* rtn = NULL;
     jclass clsstring = (*env)->FindClass(env, "java/lang/String");
     jstring strencode = (*env)->NewStringUTF(env, "UTF-8");
     jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
     jbyteArray barr= (jbyteArray)(*env)->CallObjectMethod(env, jstr, mid, strencode);
     jsize alen = (*env)->GetArrayLength(env, barr);
     jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);

     if(alen > 0) {
      rtn = (char*)malloc(alen+1);
      memcpy(rtn, ba, alen);
      rtn[alen]=0;
     }
     (*env)->ReleaseByteArrayElements(env, barr, ba, 0);
     return rtn;
}
JNIEXPORT jint JNICALL Java_com_oceanwing_readscanlib_ReadScanNdkJniUtils_select_1eq
  (JNIEnv *env, jclass obj, jstring filePath){
    LOGI("11");

    char* path = (char*)Jstring2CStr(env, filePath);
    LOGI("path %s", path);
    return select_eq(path);
  }



LOCAL_PATH := $(call my-dir)     #提定当前路径

include $(CLEAR_VARS)            #清除全局配置变量，LOCAL_XXX,除了LOCAL_PATH

LOCAL_MODULE    := audio_readscan_jni        #指定生成动态库名hello，生成的动态库文件libhello.so
LOCAL_SRC_FILES := audio_readscan_jni.c \ audio_fft.c \ audio_readscan.c          #用到的c文件
LOCAL_LDLIBS+= -llog

include $(BUILD_SHARED_LIBRARY) #提定生成动态库
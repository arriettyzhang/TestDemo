package com.example.arrietty.demoapp.utils;

import android.content.Context;

import com.example.arrietty.demoapp.utils.AppUtil;
import com.example.arrietty.demoapp.utils.StorageUtil;

import java.io.File;

import okhttp3.Cache;

/**
 * Created by asus on 2017/12/15.
 */

public class CacheUtil {

    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;

    private static File getCacheDir() {
        //设置缓存路径
        final File baseDir = StorageUtil.getCacheDir(AppUtil.getContext());
        final File cacheDir = new File(baseDir, "HttpResponseCache");
        return cacheDir;
    }

    public static Cache getCache() {
        return new Cache(getCacheDir(), HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
    }
}

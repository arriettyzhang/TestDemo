package com.example.arrietty.demoapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import com.example.arrietty.demoapp.MyApplication;

import java.io.File;
import java.util.List;

/**
 * Created by MD01 on 2017/6/1.
 */

public class AppUtil {

    private AppUtil() {
    }
    /**
     * 获取到上下文对象 *
     */
    public static Context getContext() {
        return MyApplication.getBaseApplication();
    }


    /**
     * 根据包名判断app是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        boolean retValue = false;
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                if (packageName.equals(pinfo.get(i).packageName)) {
                    retValue = true;
                    break;
                }
            }
        }
        return retValue;
    }

    public static void startAppForPackage(Context context, String packName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packName);
        context.startActivity(intent);
    }

    /**
     * if eufy genie is old version
     * @param version
     * @param versionBase
     * @return
     */
    public static boolean isOldVersion(String version, String versionBase) {
        boolean retValue = false;
        String[] aryVersion = version.split("\\.");
        String[] aryVersionBase = versionBase.split("\\.");

        // main
        if (Integer.parseInt(aryVersion[0]) > Integer.parseInt(aryVersionBase[0])) {
            retValue = false;
        } else if (Integer.parseInt(aryVersion[0]) < Integer.parseInt(aryVersionBase[0])) {
            retValue = true;
        } else {
            // major
            if (Integer.parseInt(aryVersion[1]) > Integer.parseInt(aryVersionBase[1])) {
                retValue = false;
            } else if (Integer.parseInt(aryVersion[1]) < Integer.parseInt(aryVersionBase[1])) {
                retValue = true;
            } else {
                // sub
                if (Integer.parseInt(aryVersion[2]) > Integer.parseInt(aryVersionBase[2])) {
                    retValue = false;
                } else {
                    retValue = true;
                }
            }
        }

        return retValue;

    }
    /***
     * 去设置界面
     */
    public static void goToSetting(Context context){
        //go to setting view
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}

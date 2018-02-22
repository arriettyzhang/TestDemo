package com.example.arrietty.ndktest;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.oceanwing.readscanlib.ReadScanNdkJniUtils;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


public class MainActivity extends AppCompatActivity {
    private TextView mTxetView;
    private static final String TAG ="MainActivity";
    private static final int permissionode = 100;
    public static String[] apppermissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxetView = (TextView)findViewById(R.id.helloTxt);
       // mTxetView.setText(NdkJniUtils.getStringFromJni());
        requestPermission();

    }
    private void requestPermission(){
        PermissionGen.with(this)
                .addRequestCode(permissionode)
                .permissions(apppermissions)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    //权限申请成功
    @PermissionSuccess(requestCode = permissionode)
    public void permissionSuccessFun() {
        Log.v(TAG, "perSuccess");
        Log.v(TAG, "Environment.getExternalStorageDirectory().getAbsolutePath() " + Environment.getExternalStorageDirectory().getAbsolutePath());
        String filePath= Environment.getExternalStorageDirectory().getAbsolutePath() +"/ih1_40.wav";
        Log.v(TAG, "filePath " +filePath);
        int result = ReadScanNdkJniUtils.select_eq(filePath);
        Log.v(TAG, "result " +result);
    }
    @PermissionFail(requestCode = permissionode)
    public void permissionFailFun() {
        Log.v(TAG, "perFail");
    }
}

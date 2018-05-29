package com.example.arrietty.demoapp.myturantable;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.arrietty.demoapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyTurntableActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";
    private List<WheelInfo> infos = new ArrayList<>();
    private List<WheelInfo> infos2 = new ArrayList<>();
    private List<WheelInfo> infos3 = new ArrayList<>();
    /**
     * 抽奖的文字
     */
    private String[] mStrs = new String[]{"One礼包", "Two礼包", "Three礼包",
            "Four礼包", "Five礼包", "Six礼包", "Seven礼包", "Eight礼包"};
    private TurntableView mTurntableView;
    private TurntableView2 mTurntableView2;
    private TurntableView3 mTurntableView3;
    private TextView mTextView,  mTextView2, mTextView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turntableview);
        initdata();
        mTextView =(TextView)findViewById(R.id.chooseIndex);
        mTurntableView = (TurntableView)findViewById(R.id.turntableview);
        mTurntableView.setOnCallBackPosition(new TurntableView.onCallBackPosition() {
            @Override
            public void getStopPosition(int pos) {
                Log.v(TAG, "pos " +pos);
//                Toast.makeText(MainActivity.this, "choose " + mStrs[pos], Toast.LENGTH_SHORT).show();
                mTextView.setText("选中了 第[" + pos+"] 项=" + mStrs[pos]);
            }
        });
        mTurntableView.setWheelInfos(infos);
        mTurntableView.setPostion(infos.size()-1);
        mTextView2 =(TextView)findViewById(R.id.chooseIndex2);
        mTurntableView2 = (TurntableView2)findViewById(R.id.turntableview2);
        mTurntableView2.setOnCallBackPosition(new TurntableView2.onCallBackPosition() {
            @Override
            public void getStopPosition(int pos) {
                Log.v(TAG, "pos " +pos);
//                Toast.makeText(MainActivity.this, "choose " + mStrs[pos], Toast.LENGTH_SHORT).show();
                mTextView2.setText("选中了 第[" + pos+"] 项=" + mStrs[pos]);
            }
        });
        mTurntableView2.setWheelInfos(infos2);
        mTurntableView2.setPostion(infos2.size()-2);

        mTextView3 =(TextView)findViewById(R.id.chooseIndex3);
        mTurntableView3 = (TurntableView3)findViewById(R.id.turntableview3);
        mTurntableView3.setOnCallBackPosition(new TurntableView3.onCallBackPosition() {
            @Override
            public void getStopPosition(int pos) {
                Log.v(TAG, "pos " +pos);
//                Toast.makeText(MainActivity.this, "choose " + mStrs[pos], Toast.LENGTH_SHORT).show();
                mTextView3.setText("选中了 第[" + pos+"] 项=" + mStrs[pos]);
            }
        });
        mTurntableView3.setWheelInfos(infos3);
        mTurntableView3.setPostion(infos3.size()-2);
    }
    private void initdata(){
        for (int i = 0; i < mStrs.length; i++) {
            infos.add(new WheelInfo(mStrs[i], BitmapFactory.decodeResource(getResources(), R.mipmap.ic_youxi)));
        }
        for (int i = 0; i < mStrs.length; i++) {
            infos2.add(new WheelInfo(mStrs[i], BitmapFactory.decodeResource(getResources(), R.mipmap.ic_youxi)));
        }
        for (int i = 0; i < mStrs.length; i++) {
            infos3.add(new WheelInfo(mStrs[i], BitmapFactory.decodeResource(getResources(), R.mipmap.ic_youxi)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        infos.clear();
        infos2.clear();
        infos3.clear();

    }
}

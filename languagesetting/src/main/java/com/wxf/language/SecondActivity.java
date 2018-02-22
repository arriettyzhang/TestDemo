package com.wxf.language;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by asus on 2017/12/21.
 */

public class SecondActivity extends BaseActivity {
    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("第二个Activity");
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.btn_setting);
        button.setText("setting");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, SettingAc.class));
            }
        });
        tv = (TextView)findViewById(R.id.tv);
        tv.setText(R.string.text_content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package com.wxf.language;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends BaseAc {
    private String name;
    private TextView tv;

    @Override
    void updateAllViewWithText() {

        if(tv!=null){
            tv.setText(R.string.text_content);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("第一个Activity");
        View view = new View(this);
        Button button = (Button)findViewById(R.id.btn_setting);
        button.setText("second");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
         tv = (TextView)findViewById(R.id.tv);
        tv.setText(R.string.text_content);
    }
}

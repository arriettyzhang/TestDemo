package com.example.arrietty.demoapp.recyclelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.arrietty.demoapp.R;

public class RecycleMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_main);
        findViewById(R.id.line1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecycleMainActivity.this, LineLocalActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.line).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecycleMainActivity.this, LineActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.line2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecycleMainActivity.this, LineActivity2.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.grid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecycleMainActivity.this, GridActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.staggered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecycleMainActivity.this, StaggeredActivity.class);
                startActivity(intent);
            }
        });


    }
}

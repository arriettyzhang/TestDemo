package com.example.arrietty.demoapp.spandemo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.example.arrietty.demoapp.R;
import com.example.arrietty.demoapp.utils.AppUtil;

/**
 * Created by asus on 2017/11/22.
 */

public class SpanActivity extends AppCompatActivity {
    private static final String TAG ="SpanActivity";
    private TextView mTextview;
    String t1 ="xxxxx";
    String t2 ="yyyyy";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span);
        mTextview = (TextView)findViewById(R.id.tv);
//        String totalString = getString(R.string.android_genie_pandora_text_1);
//        String clickString = getString(R.string.amazon_alexa_app);
//        String colorString = getString(R.string.settings);
//        setSpanForView(mTextview, totalString, clickString, colorString);

//        setImage(mTextview, R.drawable.ic_launcher_round);
        String tt = t1+ getResources().getString(R.string.dot) +t2;

        setSpanForView(mTextview, tt, t1, t2);
    }
    private void setSpanForView(View view, String totalString, String clickString, String colorString){



        int fstart = totalString.indexOf(clickString);
        int fend = fstart + clickString.length();
        //zfuyan end
        SpannableStringBuilder style = new SpannableStringBuilder(totalString);
        style.setSpan(new ColorUnderlineClickableSpan(SpanActivity.this, R.color.lightBlue) {

            @Override
            public void onClick(View widget) {
                // open alexa app
                if (AppUtil.isAppInstalled(getApplicationContext(), "com.amazon.dee.app")) {
                    AppUtil.startAppForPackage(SpanActivity.this, "com.amazon.dee.app");

                } else {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://play.google" +
                            ".com/store/apps/details?id=com.amazon.dee.app");
                    intent.setData(content_url);
                    startActivity(intent);
                }

            }
        }, fstart, fend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        fstart = totalString.indexOf(colorString);
        fend = fstart + colorString.length();
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                fstart, fend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextview.setText(style);
        // 去掉点击背景色
        mTextview.setHighlightColor(getResources().getColor(R.color
                .color_transparent));
        // 设置可以点击
        mTextview.setMovementMethod(LinkMovementMethod.getInstance());

    }
    private void setImage(TextView mShowTv, int dra) {
        Drawable drawable = getResources().getDrawable(dra);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        SpannableString spannableString = new SpannableString("pics");

        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);

        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        mShowTv.append(t1);
        mShowTv.append(spannableString);//将图片转为文字显示，这个是重点
        mShowTv.append(t2);

    }
}

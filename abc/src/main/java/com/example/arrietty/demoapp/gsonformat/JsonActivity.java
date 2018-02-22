package com.example.arrietty.demoapp.gsonformat;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.arrietty.demoapp.R;
import com.google.gson.Gson;

/**
 * Created by asus on 2017/12/14.
 */

public class JsonActivity extends AppCompatActivity {
    private static final String TAG ="JsonActivity";
    private TextView mTextView;
    private String jsonSrc ="{\n" +
            "    \"error_code\": 0,\n" +
            "    \"reason\": \"Success\",\n" +
            "    \"result\": {\n" +
            "        \"data\":[\n" +
            "            {\n" +
            "                \"content\":\"床不在好，有女(你)就行，枕不在久，整完就走，斯是陋室，惟吾色心！！…\",\n" +
            "                \"hashId\":\"accd4dea540fe0b2f2205f9234114335\",\n" +
            "                \"unixtime\":1478579630,\n" +
            "                \"updatetime\":\"2016-11-08 12:33:50\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"content\":\"老婆问老公：亲爱的你觉得我是灰姑娘吗？老公回答：当然不是了。老婆：那你的意思说是我是白雪公主？老公回答：不，你是黑姑娘。\",\n" +
            "                \"hashId\":\"8144c8979f02c539841968ef8046db98\",\n" +
            "                \"unixtime\":1478577230,\n" +
            "                \"updatetime\":\"2016-11-08 11:53:50\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"content\":\"睡的正香，老婆把我叫醒说她要去卫生间，我说你上厕所就上呗！她说我是要叫你起来穿衣服的，我说那你上厕所我起来穿衣服干啥？我又不冷！然后就看着老婆把被子抱在身上去上厕所了。\",\n" +
            "                \"hashId\":\"76b7f6c0ba6c3455fbd216820ce4a68c\",\n" +
            "                \"unixtime\":1478577230,\n" +
            "                \"updatetime\":\"2016-11-08 11:53:50\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"content\":\"老奶奶加了出租车，到了之后车费显示是8块，但是她只给了3块。司机赶紧叫住老奶奶：“老太太，这车费8块块你怎么只付3元呢？”老奶奶回答：“我刚才坐车的时候计价器显示5元块了。那我不就是在给三块就可以了。\",\n" +
            "                \"hashId\":\"85a82ecf746981ea3f48e5066eb034e1\",\n" +
            "                \"unixtime\":1478577230,\n" +
            "                \"updatetime\":\"2016-11-08 11:53:50\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"content\":\"每次看战争片时总有一些疑惑，在冲锋时前面的士兵一边奔跑着一边狂打枪，可以理解。但总是看到后面的士兵也紧跟着跑开着枪，难道子弹会拐弯，打不到前面的士兵吗？……求解答，为什么打不到队友？\",\n" +
            "                \"hashId\":\"d3ddd517e97a6dbeac24c82d4ce34e72\",\n" +
            "                \"unixtime\":1478575431,\n" +
            "                \"updatetime\":\"2016-11-08 11:23:51\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span);
        mTextView = (TextView)findViewById(R.id.tv);
        BeanInfo info = strToBeanByGson(jsonSrc);
        mTextView.setText(beanToJsonByGson(info));
    }
    /*
    Json数据自动生成Bean对象
     */
    private BeanInfo strToBeanByGson(String src){
        Gson gson = new Gson();
        BeanInfo info = gson.fromJson(src,BeanInfo.class);
        Log.v(TAG, "" + info.getErrorCode());
        return  info;

    }
    /*
    Bean对象转化为Json数据
     */
    private String beanToJsonByGson(BeanInfo info){
        String jsonSti = new Gson().toJson(info);
        return jsonSti;
    }
    /*
    Json数据自动生成Bean对象
     */
    private BeanInfo jsonToBeanByFastJson(String jsonSrc){
        BeanInfo info = JSON.parseObject(jsonSrc,BeanInfo.class);
        return info;
    }
    /*
    Bean对象转化为Json数据
     */
    private String beanToJsonByFastJson(BeanInfo info){
        return JSON.toJSONString(info);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

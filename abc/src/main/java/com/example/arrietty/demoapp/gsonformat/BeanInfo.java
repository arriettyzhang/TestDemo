package com.example.arrietty.demoapp.gsonformat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by asus on 2017/12/14.
 */

public class BeanInfo {

    /**
     * error_code : 0
     * reason : Success
     * result : {"data":[{"content":"床不在好，有女(你)就行，枕不在久，整完就走，斯是陋室，惟吾色心！！\u2026","hashId":"accd4dea540fe0b2f2205f9234114335","unixtime":1478579630,"updatetime":"2016-11-08 12:33:50"},{"content":"老婆问老公：亲爱的你觉得我是灰姑娘吗？老公回答：当然不是了。老婆：那你的意思说是我是白雪公主？老公回答：不，你是黑姑娘。","hashId":"8144c8979f02c539841968ef8046db98","unixtime":1478577230,"updatetime":"2016-11-08 11:53:50"},{"content":"睡的正香，老婆把我叫醒说她要去卫生间，我说你上厕所就上呗！她说我是要叫你起来穿衣服的，我说那你上厕所我起来穿衣服干啥？我又不冷！然后就看着老婆把被子抱在身上去上厕所了。","hashId":"76b7f6c0ba6c3455fbd216820ce4a68c","unixtime":1478577230,"updatetime":"2016-11-08 11:53:50"},{"content":"老奶奶加了出租车，到了之后车费显示是8块，但是她只给了3块。司机赶紧叫住老奶奶：\u201c老太太，这车费8块块你怎么只付3元呢？\u201d老奶奶回答：\u201c我刚才坐车的时候计价器显示5元块了。那我不就是在给三块就可以了。","hashId":"85a82ecf746981ea3f48e5066eb034e1","unixtime":1478577230,"updatetime":"2016-11-08 11:53:50"},{"content":"每次看战争片时总有一些疑惑，在冲锋时前面的士兵一边奔跑着一边狂打枪，可以理解。但总是看到后面的士兵也紧跟着跑开着枪，难道子弹会拐弯，打不到前面的士兵吗？\u2026\u2026求解答，为什么打不到队友？","hashId":"d3ddd517e97a6dbeac24c82d4ce34e72","unixtime":1478575431,"updatetime":"2016-11-08 11:23:51"}]}
     */

    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private ResultBean result;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        @SerializedName("data")
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * content : 床不在好，有女(你)就行，枕不在久，整完就走，斯是陋室，惟吾色心！！…
             * hashId : accd4dea540fe0b2f2205f9234114335
             * unixtime : 1478579630
             * updatetime : 2016-11-08 12:33:50
             */

            @SerializedName("content")
            private String content;
            @SerializedName("hashId")
            private String hashId;
            @SerializedName("unixtime")
            private int unixtime;
            @SerializedName("updatetime")
            private String updatetime;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getHashId() {
                return hashId;
            }

            public void setHashId(String hashId) {
                this.hashId = hashId;
            }

            public int getUnixtime() {
                return unixtime;
            }

            public void setUnixtime(int unixtime) {
                this.unixtime = unixtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }
        }
    }
}

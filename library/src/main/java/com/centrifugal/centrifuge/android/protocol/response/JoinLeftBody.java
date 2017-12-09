package com.centrifugal.centrifuge.android.protocol.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    JoinLeftBody.java
 * ClassName:    JoinLeftBody
 * <p>
 * Description: 加入退出消息体.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:24
 */
public class JoinLeftBody {

    /**
     * {
     *   "method": "join",
     *   "body": {
     *      "channel": "public:yptv_11565bbe6",
     *      "data": {
     *          "user": "0",
     *          "client": "27446145-d430-4f78-8083-b18c293b5698"
     *       }
     *   }
     * }
     *
     * {
     *   "method": "leave",
     *   "body": {
     *       "channel": "public:yptv_11586316c",
     *       "data": {
     *          "user": "2221513169",
     *          "client": "c63e1802-44de-4877-bde1-fe0ef9cc3e4f"
     *       }
     *   }
     * }
     */

    @SerializedName("channel")
    private String channel;

    @SerializedName("data")
    private JsonObject data;

    public String getChannel() {
        return channel;
    }

    public JsonObject getData() {
        return data;
    }
}

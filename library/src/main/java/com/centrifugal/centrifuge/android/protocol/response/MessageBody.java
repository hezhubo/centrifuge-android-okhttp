package com.centrifugal.centrifuge.android.protocol.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    MessageBody.java
 * ClassName:    MessageBody
 * <p>
 * Description: 消息信息消息体.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:26
 */
public class MessageBody {

    @SerializedName("uid")
    private String uuid;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("channel")
    private String channel;

    @SerializedName("data")
    private JsonObject data;

    public String getUuid() {
        return uuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getChannel() {
        return channel;
    }

    public JsonObject getData() {
        return data;
    }
}

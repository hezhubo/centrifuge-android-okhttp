package com.centrifugal.centrifuge.android.protocol.response;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.Nullable;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    SubscribeBody.java
 * ClassName:    SubscribeBody
 * <p>
 * Description: 订阅信息消息体.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:27
 */
public class SubscribeBody {

    @SerializedName("channel")
    private String channel;

    @Nullable
    @SerializedName("status")
    private Boolean status;

    @SerializedName("last")
    private String lastMessageId;

    /**
     * 需要恢复(未接收)的消息 (未使用)
     */
    @Nullable
    @SerializedName("messages")
    private JsonElement recoveredMessages;

    @SerializedName("recovered")
    private boolean recovered;

    public String getChannel() {
        return channel;
    }

    public Boolean isStatus() {
        return status;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public JsonElement getRecoveredMessages() {
        return recoveredMessages;
    }

    public boolean isRecovered() {
        return recovered;
    }
}

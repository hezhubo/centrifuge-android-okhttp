package com.centrifugal.centrifuge.android.protocol.request;

import io.reactivex.annotations.NonNull;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    SubscriptionMessage.java
 * ClassName:    SubscriptionMessage
 * <p>
 * Description: 订阅频道的消息.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:33
 */
public class SubscriptionMessage extends CommonMessage {

    public SubscriptionMessage(@NonNull String uuid, @NonNull String method, @NonNull String
            channel) {
        super(uuid, method, channel);
    }

    /**
     * 设置私有频道信息
     *
     * @param sign
     * @param clientId
     * @param info
     */
    public void setPrivate(String sign, String clientId, String info) {
        params.sign = sign;
        params.clientId = clientId;
        params.info = info;
    }

    public String getChannel() {
        return params.channel;
    }

    /**
     * 更新收到的最后一条消息的id
     *
     * @param lastMessageId
     */
    public void updateLastMessage(@NonNull String lastMessageId) {
        params.lastMessageId = lastMessageId;
        params.recover = true;
    }

    public String getLastMessageId() {
        return params.lastMessageId;
    }

}

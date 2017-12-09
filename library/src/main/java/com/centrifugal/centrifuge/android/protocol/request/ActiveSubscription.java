package com.centrifugal.centrifuge.android.protocol.request;

import io.reactivex.annotations.NonNull;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    ActiveSubscription.java
 * ClassName:    ActiveSubscription
 * <p>
 * Description: 订阅过(有效)的频道.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:32
 */
public class ActiveSubscription {

    private SubscriptionMessage subscribedMessage;

    private boolean connected = false;

    public ActiveSubscription(@NonNull SubscriptionMessage subscriptionMessage) {
        subscribedMessage = subscriptionMessage;
    }

    public String getLastMessageId() {
        return subscribedMessage.getLastMessageId();
    }

    public void updateLastMessage(@NonNull String lastMessageId) {
        subscribedMessage.updateLastMessage(lastMessageId);
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public SubscriptionMessage getSubscribedMessage() {
        return subscribedMessage;
    }
}

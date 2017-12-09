package com.centrifugal.centrifuge.android.event;

import com.centrifugal.centrifuge.android.protocol.CommandMethod;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    SubscriptionEvent.java
 * ClassName:    SubscriptionEvent
 * <p>
 * Description: 订阅相关事件.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:31
 */
public class SubscriptionEvent extends BaseEvent {

    private String channel;
    private String method;
    private String error;

    /**
     * 订阅/取消订阅
     *
     * @param method  {@link CommandMethod#SUBSCRIBE}，{@link CommandMethod#UNSUBSCRIBE}
     * @param channel
     */
    public SubscriptionEvent(String method, String channel) {
        super(TYPE_SUBSCRIBED);
        if (CommandMethod.UNSUBSCRIBE.equals(method)) {
            type = TYPE_UNSUBSCRIBED;
        }
        this.method = method;
        this.channel = channel;
    }

    /**
     * 订阅/取消订阅失败
     *
     * @param method  {@link CommandMethod#SUBSCRIBE}，{@link CommandMethod#UNSUBSCRIBE}
     * @param channel
     */
    public SubscriptionEvent(String method, String channel, String error) {
        super(TYPE_SUBSCRIPTION_ERROR);
        this.method = method;
        this.channel = channel;
        this.error = error;
    }

    public String getChannel() {
        return channel;
    }

    public String getMethod() {
        return method;
    }

    public String getError() {
        return error;
    }

}


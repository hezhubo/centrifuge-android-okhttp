package com.centrifugal.centrifuge.android.event;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    BaseEvent.java
 * ClassName:    BaseEvent
 * <p>
 * Description: Rx事件.
 *
 * @author hezhubo
 * @date 2017年12月08日 17:38
 */
public class BaseEvent {

    public static final int TYPE_CONNECTING = 0;
    public static final int TYPE_WEB_SOCKET_OPEN = 1;
    public static final int TYPE_CONNECTED = 2;
    public static final int TYPE_DISCONNECTED = 3;
    public static final int TYPE_SUBSCRIBED = 4;
    public static final int TYPE_UNSUBSCRIBED = 5;
    public static final int TYPE_SUBSCRIPTION_ERROR = 6;
    public static final int TYPE_JOIN = 7;
    public static final int TYPE_LEAVE = 8;
    public static final int TYPE_RECEIVE_MESSAGE = 9;
    public static final int TYPE_ALIVE = 10;

    protected int type;

    public BaseEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

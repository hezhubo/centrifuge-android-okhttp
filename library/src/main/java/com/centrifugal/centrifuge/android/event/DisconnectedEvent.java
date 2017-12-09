package com.centrifugal.centrifuge.android.event;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    DisconnectedEvent.java
 * ClassName:    DisconnectedEvent
 * <p>
 * Description: 断开连接事件.
 *
 * @author hezhubo
 * @date 2017年12月08日 17:42
 */
public class DisconnectedEvent extends BaseEvent {

    private int code;
    private String reason;

    public DisconnectedEvent(int code, String reason) {
        super(TYPE_DISCONNECTED);
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
package com.centrifugal.centrifuge.android.listener;

import com.centrifugal.centrifuge.android.protocol.response.JoinLeftBody;
import com.centrifugal.centrifuge.android.protocol.response.MessageBody;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    CommandListener.java
 * ClassName:    CommandListener
 * <p>
 * Description: 监听器.
 *
 * @author hezhubo
 * @date 2017年12月08日 17:44
 */
public interface CommandListener {

    /**
     * 连接中(开始连接)
     */
    void onConnecting();

    /**
     * websocket 连接成功
     */
    void onWebSocketOpen();

    /**
     * centrifugo 连接成功
     */
    void onConnected();

    /**
     * 连接断开
     *
     * @param code
     * @param reason
     */
    void onDisconnected(int code, String reason);

    /**
     * 订阅
     *
     * @param channel
     */
    void onSubscribed(String channel);

    /**
     * 取消订阅
     *
     * @param channel
     */
    void onUnSubscribed(String channel);

    /**
     * 订阅/取消订阅 失败
     *
     * @param method
     * @param channel
     * @param error
     */
    void onSubscriptionError(String method, String channel, String error);

    /**
     * 用户加入
     *
     * @param joinLeftBody
     */
    void onJoin(JoinLeftBody joinLeftBody);

    /**
     * 用户退出
     *
     * @param joinLeftBody
     */
    void onLeave(JoinLeftBody joinLeftBody);

    /**
     * 收到消息
     *
     * @param messageBody
     */
    void onReceiveMessage(MessageBody messageBody);

    /**
     * 发送ping命令后，服务端有响应时的回调
     */
    void onAlive();
}

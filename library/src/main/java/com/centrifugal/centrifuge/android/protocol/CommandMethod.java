package com.centrifugal.centrifuge.android.protocol;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    CommandMethod.java
 * ClassName:    CommandMethod
 * <p>
 * Description: 协议名称.
 *
 * @author hezhubo
 * @date 2017年12月08日 17:46
 */
public class CommandMethod {

    /**
     * 连接
     */
    public static final String CONNECT = "connect";
    /**
     * 订阅
     */
    public static final String SUBSCRIBE = "subscribe";
    /**
     * 取消订阅
     */
    public static final String UNSUBSCRIBE = "unsubscribe";
    /**
     * 客户端直接往频道发消息（需服务端开启）
     */
    public static final String PUBLISH = "publish";
    /**
     * 频道在线情况（需服务端开启）
     */
    public static final String PRESENCE = "presence";
    /**
     * 频道历史消息
     */
    public static final String HISTORY = "history";
    /**
     * 加入频道
     */
    public static final String JOIN = "join";
    /**
     * 退出频道
     */
    public static final String LEAVE = "leave";
    /**
     * 消息
     */
    public static final String MESSAGE = "message";
    /**
     * 未知命令
     */
    public static final String REFRESH = "refresh";
    /**
     * 校验是否在线
     */
    public static final String PING = "ping";
}

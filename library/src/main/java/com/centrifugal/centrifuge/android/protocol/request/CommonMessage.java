package com.centrifugal.centrifuge.android.protocol.request;

import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    CommonMessage.java
 * ClassName:    CommonMessage
 * <p>
 * Description: 通用的请求消息.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:34
 */
public class CommonMessage {

    /**
     * {
     *   "uid": "UNIQUE COMMAND ID",
     *   "method": "COMMAND NAME TO WHICH THIS RESPONSE REFERS TO",
     *   "params": {
     *       "channel": "CHANNEL",
     *   }
     * }
     */

    @SerializedName("uid")
    protected String uuid;

    @SerializedName("method")
    protected String method;

    @Nullable
    @SerializedName("params")
    protected CommonParams params;

    /**
     * 构造方法
     *
     * @param uuid   连接成功后返回的client
     * @param method 要请求的协议
     */
    public CommonMessage(@NonNull String uuid, @NonNull String method) {
        this.uuid = uuid;
        this.method = method;
    }

    /**
     * 构造方法
     *
     * @param uuid    连接成功后返回的client
     * @param method  要请求的协议
     * @param channel 频道
     */
    public CommonMessage(@NonNull String uuid, @NonNull String method, @NonNull String channel) {
        this.uuid = uuid;
        this.method = method;
        this.params = new CommonParams();
        this.params.channel = channel;
    }
}

package com.centrifugal.centrifuge.android.protocol.request;

import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.Nullable;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    CommonParams.java
 * ClassName:    CommonParams
 * <p>
 * Description: 通用的协议请求参数.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:34
 */
public class CommonParams {

    @SerializedName("channel")
    String channel;

    @Nullable
    @SerializedName("sign")
    String sign;

    @Nullable
    @SerializedName("client")
    String clientId;

    @Nullable
    @SerializedName("info")
    String info;

    @Nullable
    @SerializedName("last")
    String lastMessageId;

    @Nullable
    @SerializedName("recover")
    Boolean recover;
}

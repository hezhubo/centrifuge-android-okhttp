package com.centrifugal.centrifuge.android.protocol.request;

import android.text.TextUtils;

import com.centrifugal.centrifuge.android.protocol.CommandMethod;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    ConnectMessage.java
 * ClassName:    ConnectMessage
 * <p>
 * Description: 建立连接的消息.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:36
 */
public class ConnectMessage {

    /**
     * {
     *   "uid": "UNIQUE COMMAND ID",
     *   "method": "connect",
     *   "params": {
     *       "user": "USER ID STRING",
     *       "timestamp": "STRING WITH CURRENT TIMESTAMP SECONDS",
     *       "info": "OPTIONAL JSON ENCODED STRING",
     *       "token": "SHA-256 HMAC TOKEN GENERATED FROM PARAMETERS ABOVE"
     *   }
     * }
     */

    @SerializedName("uid")
    private String uuid;

    @SerializedName("method")
    private String method = CommandMethod.CONNECT;

    @SerializedName("params")
    private Params params;

    /**
     * @param userId         用户ID
     * @param tokenTimestamp 令牌对应时间戳
     * @param token          令牌
     * @param info           描述信息
     */
    public ConnectMessage(@NonNull String userId, @NonNull String tokenTimestamp, @NonNull String
            token, @Nullable String info) {
        uuid = UUID.randomUUID().toString(); // 随机生成uuid
        params = new Params();
        params.userId = userId;
        params.tokenTimestamp = tokenTimestamp;
        if (TextUtils.isEmpty(info)) {
            params.info = "";
        } else {
            params.info = info;
        }
        params.token = token;

    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    class Params {
        @SerializedName("user")
        String userId;

        @SerializedName("timestamp")
        String tokenTimestamp;

        @SerializedName("info")
        String info;

        @SerializedName("token")
        String token;
    }
}

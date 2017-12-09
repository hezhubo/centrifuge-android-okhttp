package com.centrifugal.centrifuge.android.protocol.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.Nullable;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    BaseMessage.java
 * ClassName:    BaseMessage
 * <p>
 * Description: 返回的基础数据消息.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:14
 */
public class BaseMessage {

    /**
     * {
     *   "uid": "ECHO BACK THE SAME UNIQUE COMMAND ID SENT IN REQUEST COMMAND",
     *   "method": "COMMAND NAME TO WHICH THIS RESPONSE REFERS TO",
     *   "error": "ERROR STRING, IF NOT EMPTY THEN SOMETHING WENT WRONG AND BODY SHOULD NOT BE PROCESSED",
     *   "body": "RESPONSE BODY, CONTAINS USEFUL RESPONSE DATA"
     * }
     */

    @SerializedName("uid")
    protected String requestUUID; // 请求的UUID

    @SerializedName("method")
    protected String method;

    @Nullable
    @SerializedName("error")
    protected String error; // 错误信息

    @SerializedName("body")
    protected JsonObject body;

    public String getRequestUUID() {
        return requestUUID;
    }

    public String getMethod() {
        return method;
    }

    @Nullable
    public String getError() {
        return error;
    }

    public JsonObject getBody() {
        return body;
    }
}

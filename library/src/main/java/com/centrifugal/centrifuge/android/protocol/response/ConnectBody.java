package com.centrifugal.centrifuge.android.protocol.response;

import com.google.gson.annotations.SerializedName;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    ConnectBody.java
 * ClassName:    ConnectBody
 * <p>
 * Description: 连接信息消息体.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:22
 */
public class ConnectBody {

    /**
     * {
     *   "version":
     *   "client": "UNIQUE CLIENT ID SERVER GAVE TO THIS CONNECTION",
     *   "expires": "false",
     *   "expired": false,
     *   "ttl": 0
     * }
     */

    @SerializedName("version")
    private String version;

    @SerializedName("client")
    private String clientId; // UNIQUE CLIENT ID SERVER GAVE TO THIS CONNECTION

    @SerializedName("expires")
    private boolean expires;

    @SerializedName("expired")
    private boolean expired;

    @SerializedName("ttl")
    private int ttl;

    public String getVersion() {
        return version;
    }

    public String getClientId() {
        return clientId;
    }

    public boolean isExpires() {
        return expires;
    }

    public boolean isExpired() {
        return expired;
    }

    public int getTtl() {
        return ttl;
    }
}

package com.centrifugal.centrifuge.android;

import android.text.TextUtils;

import com.centrifugal.centrifuge.android.event.BaseEvent;
import com.centrifugal.centrifuge.android.event.DisconnectedEvent;
import com.centrifugal.centrifuge.android.event.JoinLeftEvent;
import com.centrifugal.centrifuge.android.event.MessageEvent;
import com.centrifugal.centrifuge.android.event.SubscriptionEvent;
import com.centrifugal.centrifuge.android.listener.CommandListener;
import com.centrifugal.centrifuge.android.protocol.CommandMethod;
import com.centrifugal.centrifuge.android.protocol.request.ActiveSubscription;
import com.centrifugal.centrifuge.android.protocol.request.CommonMessage;
import com.centrifugal.centrifuge.android.protocol.request.ConnectMessage;
import com.centrifugal.centrifuge.android.protocol.request.SubscriptionMessage;
import com.centrifugal.centrifuge.android.protocol.response.BaseMessage;
import com.centrifugal.centrifuge.android.protocol.response.ConnectBody;
import com.centrifugal.centrifuge.android.protocol.response.JoinLeftBody;
import com.centrifugal.centrifuge.android.protocol.response.MessageBody;
import com.centrifugal.centrifuge.android.protocol.response.SubscribeBody;
import com.centrifugal.centrifuge.android.util.LogUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    Centrifugo.java
 * ClassName:    Centrifugo
 * <p>
 * Description: Centrifugo客户端.
 *
 * @author hezhubo
 * @date 2017年12月08日 17:23
 */
public class Centrifugo {

    private static final String TAG = Centrifugo.class.getSimpleName();

    /**
     * socket连接超时时间
     */
    private static final int SOCKET_CONNECT_TIMEOUT = 15;

    /**
     * 状态 未连接
     */
    private static final int STATE_NOT_CONNECTED = 0;
    /**
     * 状态 错误
     */
    private static final int STATE_ERROR = 1;
    /**
     * 状态 已连接
     */
    private static final int STATE_CONNECTED = 2;
    /**
     * 状态 断开中
     */
    private static final int STATE_DISCONNECTING = 3;
    /**
     * 状态 连接中
     */
    private static final int STATE_CONNECTING = 4;

    /**
     * 状态 默认未连接
     */
    private int currentState = STATE_NOT_CONNECTED;

    private Gson mGson;

    private OkHttpClient mOkHttpClient;

    private ObservableEmitter<BaseEvent> mEmitter;

    private WebSocket mWebSocket;

    /**
     * 监听器
     */
    private CommandListener mCommandListener;

    /**
     * 服务器地址
     */
    private String serverUrl;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 令牌
     */
    private String token;
    /**
     * 令牌对应时间戳
     */
    private String tokenTimestamp;
    /**
     * 客户端ID （连接成功，由服务端返回）
     */
    private String clientId;
    /**
     * 描述信息 （暂未使用）
     */
    private String info;

    /**
     * 要订阅的频道
     */
    private List<String> toSubscribeChannels;
    /**
     * 要订阅的私有频道
     */
    private Map<String, SubscriptionMessage> toSubscribePrivateMap;
    /**
     * 已订阅的频道
     */
    private Map<String, ActiveSubscription> activeSubscriptionMap;

    public Centrifugo(String serverUrl, String userId, String token, String tokenTimestamp,
                      String info) {
        this.serverUrl = serverUrl;
        this.userId = userId;
        this.token = token;
        this.tokenTimestamp = tokenTimestamp;
        this.info = info;

        mGson = new Gson();

        toSubscribeChannels = new ArrayList<>();
        toSubscribePrivateMap = new HashMap<>();
        activeSubscriptionMap = new HashMap<>();

        initOkHttpClient();

        initRx();
    }

    private void initOkHttpClient() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(SOCKET_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(SOCKET_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(SOCKET_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private void initRx() {
        Observable.create(new ObservableOnSubscribe<BaseEvent>() {
            @Override
            public void subscribe(ObservableEmitter<BaseEvent> e) throws Exception {
                mEmitter = e;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseEvent>() {
            @Override
            public void accept(BaseEvent socketEvent) throws Exception {
                if (mCommandListener == null) {
                    return;
                }
                switch (socketEvent.getType()) {
                    case BaseEvent.TYPE_CONNECTING:
                        mCommandListener.onConnecting();
                        break;
                    case BaseEvent.TYPE_WEB_SOCKET_OPEN:
                        mCommandListener.onWebSocketOpen();
                        break;
                    case BaseEvent.TYPE_CONNECTED:
                        mCommandListener.onConnected();
                        break;
                    case BaseEvent.TYPE_DISCONNECTED:
                        if (socketEvent instanceof DisconnectedEvent) {
                            DisconnectedEvent event = (DisconnectedEvent) socketEvent;
                            mCommandListener.onDisconnected(event.getCode(), event.getReason());
                        }
                        break;
                    case BaseEvent.TYPE_SUBSCRIBED:
                        if (socketEvent instanceof SubscriptionEvent) {
                            mCommandListener.onSubscribed(((SubscriptionEvent) socketEvent)
                                    .getChannel());
                        }
                        break;
                    case BaseEvent.TYPE_UNSUBSCRIBED:
                        if (socketEvent instanceof SubscriptionEvent) {
                            mCommandListener.onUnSubscribed(((SubscriptionEvent) socketEvent)
                                    .getChannel());
                        }
                        break;
                    case BaseEvent.TYPE_SUBSCRIPTION_ERROR:
                        if (socketEvent instanceof SubscriptionEvent) {
                            SubscriptionEvent event = (SubscriptionEvent) socketEvent;
                            mCommandListener.onSubscriptionError(event.getMethod(), event
                                    .getChannel(), event.getError());
                        }
                        break;
                    case BaseEvent.TYPE_JOIN:
                        if (socketEvent instanceof JoinLeftEvent) {
                            mCommandListener.onJoin(((JoinLeftEvent) socketEvent).getJoinLeftBody
                                    ());
                        }
                        break;
                    case BaseEvent.TYPE_LEAVE:
                        if (socketEvent instanceof JoinLeftEvent) {
                            mCommandListener.onLeave(((JoinLeftEvent) socketEvent)
                                    .getJoinLeftBody());
                        }
                        break;
                    case BaseEvent.TYPE_RECEIVE_MESSAGE:
                        if (socketEvent instanceof MessageEvent) {
                            mCommandListener.onReceiveMessage(((MessageEvent) socketEvent)
                                    .getMessageBody());
                        }
                        break;
                    case BaseEvent.TYPE_ALIVE:
                        mCommandListener.onAlive();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 设置回调监听器
     *
     * @param l
     */
    public void setCommandListener(CommandListener l) {
        mCommandListener = l;
    }

    /**
     * 连接
     */
    public void connect() {
        currentState = STATE_CONNECTING;
        if (mEmitter != null && !mEmitter.isDisposed()) {
            mEmitter.onNext(new BaseEvent(BaseEvent.TYPE_CONNECTING));
        }
        Request request = new Request.Builder().url(serverUrl).build();
        mWebSocket = mOkHttpClient.newWebSocket(request, new IWebSocketListener());
    }

    /**
     * 订阅频道
     *
     * @param channel
     */
    public void subscribe(@NonNull String channel) {
        if (activeSubscriptionMap.containsKey(channel) && activeSubscriptionMap.get(channel)
                .isConnected()) {
            return;
        }
        if (mWebSocket != null && currentState == STATE_CONNECTED) {
            mWebSocket.send(mGson.toJson(new SubscriptionMessage(clientId, CommandMethod
                    .SUBSCRIBE, channel)));
        } else {
            toSubscribeChannels.add(channel);
        }
    }

    /**
     * 订阅频道 （用于私有频道订阅）
     *
     * @param subscriptionMessage
     */
    public void subscribe(@NonNull SubscriptionMessage subscriptionMessage) {
        String channel = subscriptionMessage.getChannel();
        if (activeSubscriptionMap.containsKey(channel)) {
            if (activeSubscriptionMap.get(channel).isConnected()) {
                return;
            }
            activeSubscriptionMap.remove(channel);
        }

        toSubscribePrivateMap.put(channel, subscriptionMessage);

        if (mWebSocket != null && currentState == STATE_CONNECTED) {
            mWebSocket.send(mGson.toJson(subscriptionMessage));
        } else {
            toSubscribeChannels.add(channel);
        }
    }

    /**
     * 取消订阅
     *
     * @param channel
     */
    public void unSubscribe(@NonNull String channel) {
        toSubscribeChannels.remove(channel);
        toSubscribePrivateMap.remove(channel);
        if (activeSubscriptionMap.containsKey(channel) && activeSubscriptionMap.get(channel)
                .isConnected()) {
            mWebSocket.send(mGson.toJson(new SubscriptionMessage(clientId, CommandMethod
                    .UNSUBSCRIBE, channel)));
        } else {
            if (mCommandListener != null) {
                mCommandListener.onUnSubscribed(channel);
            }
        }
    }

    /**
     * 发送ping消息，校验是连接是否断开
     */
    public void ping() {
        if (mWebSocket != null && currentState == STATE_CONNECTED) {
            mWebSocket.send(mGson.toJson(new CommonMessage(clientId, CommandMethod.PING)));
        }
    }

    /**
     * 断开
     */
    public void disconnect() {
        if (mWebSocket != null) {
            mWebSocket.close(1000, null);
        }
    }

    /**
     * @return 是否已连接
     */
    public boolean isConnected() {
        return currentState == STATE_CONNECTED;
    }

    /**
     * 释放资源
     */
    public void release() {
        toSubscribeChannels.clear();
        toSubscribePrivateMap.clear();
        activeSubscriptionMap.clear();
        mEmitter = null;
        mCommandListener = null;
        if (mWebSocket != null) {
            mWebSocket.cancel();
        }
    }

    class IWebSocketListener extends WebSocketListener {
        IWebSocketListener() {
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            LogUtil.i(TAG, "websocket open : " + response.message());
            // webSocket 连接成功
            if (mEmitter != null && !mEmitter.isDisposed()) {
                mEmitter.onNext(new BaseEvent(BaseEvent.TYPE_WEB_SOCKET_OPEN));
            }
            // 构造 centrifugo 协议的连接信息
            webSocket.send(mGson.toJson(new ConnectMessage(userId, tokenTimestamp, token, info)));
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            LogUtil.i(TAG, "websocket message : " + text);

            BaseMessage baseMessage = mGson.fromJson(text, BaseMessage.class);
            String method = baseMessage.getMethod();
            if (CommandMethod.MESSAGE.equals(method)) { // 普通消息
                if (mEmitter != null && !mEmitter.isDisposed()) {
                    MessageBody messageBody = mGson.fromJson(baseMessage.getBody(), MessageBody
                            .class);
                    if (messageBody != null) {
                        mEmitter.onNext(new MessageEvent(messageBody));
                    }
                }
            } else if (CommandMethod.CONNECT.equals(method)) { // 连接消息
                ConnectBody connectBody = mGson.fromJson(baseMessage.getBody(), ConnectBody.class);
                clientId = connectBody.getClientId();
                currentState = STATE_CONNECTED;

                if (mEmitter != null && !mEmitter.isDisposed()) {
                    mEmitter.onNext(new BaseEvent(BaseEvent.TYPE_CONNECTED));
                }

                // 订阅频道
                if (toSubscribeChannels.size() > 0) { // 处理要订阅的频道
                    for (String channel : toSubscribeChannels) {
                        subscribe(channel);
                    }
                    toSubscribeChannels.clear(); // 清空要订阅的频道
                }
                if (activeSubscriptionMap.size() > 0) { // 处理订阅过的频道
                    for (ActiveSubscription activeSubscription : activeSubscriptionMap.values()) {
                        mWebSocket.send(mGson.toJson(activeSubscription.getSubscribedMessage()));
                    }
                }

            } else if (CommandMethod.SUBSCRIBE.equals(method)) { // 订阅消息
                SubscribeBody subscribeBody = mGson.fromJson(baseMessage.getBody(), SubscribeBody
                        .class);

                if (!TextUtils.isEmpty(baseMessage.getError()) || subscribeBody.isStatus() ==
                        null || !subscribeBody.isStatus()) {
                    // 订阅失败
                    if (mEmitter != null && !mEmitter.isDisposed()) {
                        mEmitter.onNext(new SubscriptionEvent(CommandMethod.SUBSCRIBE,
                                subscribeBody.getChannel(), baseMessage.getError()));
                    }
                } else {
                    String channel = subscribeBody.getChannel();
                    SubscriptionMessage subscriptionMessage;
                    if (toSubscribePrivateMap.containsKey(channel)) {
                        subscriptionMessage = toSubscribePrivateMap.get(channel);
                        toSubscribePrivateMap.remove(channel);
                    } else {
                        subscriptionMessage = new SubscriptionMessage(clientId, CommandMethod
                                .SUBSCRIBE, subscribeBody.getChannel());
                    }
                    ActiveSubscription activeSubscription = new ActiveSubscription
                            (subscriptionMessage);
                    activeSubscription.setConnected(true);
                    activeSubscriptionMap.put(channel, activeSubscription); // 记录已订阅成功频道

                    // 订阅成功
                    if (mEmitter != null && !mEmitter.isDisposed()) {
                        mEmitter.onNext(new SubscriptionEvent(CommandMethod.SUBSCRIBE,
                                subscribeBody.getChannel()));
                    }

                    // 处理未接收消息 JsonElement recoveredMessages = subscribeBody.getRecoveredMessages();
                    // TODO 订阅成功默认返回历史消息
                }

            } else if (CommandMethod.UNSUBSCRIBE.equals(method)) { // 取消订阅消息
                SubscribeBody subscribeBody = mGson.fromJson(baseMessage.getBody(), SubscribeBody
                        .class);
                if (!TextUtils.isEmpty(baseMessage.getError()) || subscribeBody.isStatus() ==
                        null || !subscribeBody.isStatus()) {
                    // 取消订阅失败
                    if (mEmitter != null && !mEmitter.isDisposed()) {
                        mEmitter.onNext(new SubscriptionEvent(CommandMethod.UNSUBSCRIBE,
                                subscribeBody.getChannel(), baseMessage.getError()));
                    }
                } else {
                    activeSubscriptionMap.remove(subscribeBody.getChannel());
                    if (mEmitter != null && !mEmitter.isDisposed()) {
                        mEmitter.onNext(new SubscriptionEvent(CommandMethod.UNSUBSCRIBE,
                                subscribeBody.getChannel()));
                    }
                }
            } else if (CommandMethod.JOIN.equals(method) || CommandMethod.LEAVE.equals(method)) {
                // 加入消息 || 离开消息
                if (mEmitter != null && !mEmitter.isDisposed()) {
                    JoinLeftBody joinLeftBody = mGson.fromJson(baseMessage.getBody(),
                            JoinLeftBody.class);
                    mEmitter.onNext(new JoinLeftEvent(method, joinLeftBody));
                }
            } else if (CommandMethod.PING.equals(method)) { // ping消息
                if (mEmitter != null && !mEmitter.isDisposed()) {
                    mEmitter.onNext(new BaseEvent(BaseEvent.TYPE_ALIVE));
                }
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            LogUtil.i(TAG, "websocket message(bytes) : " + bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            LogUtil.i(TAG, "websocket closing : code =  " + code + " reason = " + reason);
            currentState = STATE_DISCONNECTING;
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            LogUtil.i(TAG, "websocket closed : code =  " + code + " reason = " + reason);
            currentState = STATE_NOT_CONNECTED;
            if (mEmitter != null && !mEmitter.isDisposed()) {
                mEmitter.onNext(new DisconnectedEvent(code, reason));
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            LogUtil.i(TAG, "websocket failure : throwable = " + t + " response = " + response);
            currentState = STATE_ERROR;
            if (mEmitter != null && !mEmitter.isDisposed()) {
                int code = -1;
                String message = "response : ";
                if (response != null) {
                    code = response.code();
                    message = message + response.message();
                }
                if (t != null) {
                    message = message + ", throwable : " + t.getMessage();
                }
                mEmitter.onNext(new DisconnectedEvent(code, message));
            }
        }

    }
}

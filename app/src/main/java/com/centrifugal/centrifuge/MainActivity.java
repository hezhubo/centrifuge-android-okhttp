package com.centrifugal.centrifuge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.centrifugal.centrifuge.android.Centrifugo;
import com.centrifugal.centrifuge.android.listener.CommandListener;
import com.centrifugal.centrifuge.android.protocol.response.JoinLeftBody;
import com.centrifugal.centrifuge.android.protocol.response.MessageBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditServerUrl;
    private EditText mEditUserId;
    private EditText mEditToken;
    private EditText mEditTimestamp;
    private EditText mEditChannel;
    private Button mBtnConnect;
    private Button mBtnSubscribe;
    private Button mBtnUnSubscribe;
    private Button mBtnPing;
    private ListView mListMessage;

    private SimpleAdapter mSimpleAdapter;
    private List<Map<String, String>> messageList;

    private Centrifugo mCentrifugo;

    private String channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditServerUrl = findViewById(R.id.et_server_url);
        mEditUserId = findViewById(R.id.et_user_id);
        mEditToken = findViewById(R.id.et_token);
        mEditTimestamp = findViewById(R.id.et_timestamp);
        mEditChannel = findViewById(R.id.et_channel);
        mBtnConnect = findViewById(R.id.btn_connect);
        mBtnSubscribe = findViewById(R.id.btn_subscribe);
        mBtnUnSubscribe = findViewById(R.id.btn_unsubscribe);
        mBtnPing = findViewById(R.id.btn_ping);
        mListMessage = findViewById(R.id.lv_message);

        mBtnConnect.setOnClickListener(this);
        mBtnSubscribe.setOnClickListener(this);
        mBtnUnSubscribe.setOnClickListener(this);
        mBtnPing.setOnClickListener(this);

        messageList = new ArrayList<>();
        mSimpleAdapter = new SimpleAdapter(this, messageList, android.R.layout
                .simple_list_item_1, new String[]{"msg"}, new int[]{android.R.id.text1});
        mListMessage.setAdapter(mSimpleAdapter);

        // TODO test
        mEditServerUrl.setText("");
        mEditUserId.setText("");
        mEditToken.setText("");
        mEditTimestamp.setText("");
        mEditChannel.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCentrifugo != null) {
            mCentrifugo.disconnect();
            mCentrifugo.release();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                String serverUrl = mEditServerUrl.getText().toString();
                String userId = mEditUserId.getText().toString();
                String token = mEditToken.getText().toString();
                String timestamp = mEditTimestamp.getText().toString();
                if (TextUtils.isEmpty(serverUrl) || TextUtils.isEmpty(userId) || TextUtils
                        .isEmpty(token) || TextUtils.isEmpty(timestamp)) {
                    Toast.makeText(this, "params error!", Toast.LENGTH_SHORT).show();
                    break;
                }
                connectCentrifugo(serverUrl, userId, token, timestamp);
                break;
            case R.id.btn_subscribe:
                channel = mEditChannel.getText().toString();
                if (TextUtils.isEmpty(channel)) {
                    Toast.makeText(this, "channel error!", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (mCentrifugo != null) {
                    mCentrifugo.subscribe(channel);
                }
                break;
            case R.id.btn_unsubscribe:
                if (TextUtils.isEmpty(channel)) {
                    Toast.makeText(this, "channel error!", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (mCentrifugo != null) {
                    mCentrifugo.unSubscribe(channel);
                }
                break;
            case R.id.btn_ping:
                if (mCentrifugo != null && mCentrifugo.isConnected()) {
                    mCentrifugo.ping();
                }
                break;
            default:
                break;
        }
    }

    private void connectCentrifugo(String serverUrl, String userId, String token, String
            timestamp) {
        if (mCentrifugo != null) {
            mCentrifugo.disconnect();
            mCentrifugo.release();
        }

        mCentrifugo = new Centrifugo(serverUrl, userId, token, timestamp, null);
        if (!TextUtils.isEmpty(channel)) {
            mCentrifugo.subscribe(channel);
        }

        mCentrifugo.setCommandListener(new CommandListener() {
            @Override
            public void onConnecting() {
                addMsg("connecting ! ");
            }

            @Override
            public void onWebSocketOpen() {
                addMsg("webSocketOpen ! ");
            }

            @Override
            public void onConnected() {
                addMsg("connected ! ");
            }

            @Override
            public void onDisconnected(int code, String reason) {
                addMsg("disconnected :  " + code + ",  " + reason);
            }

            @Override
            public void onSubscribed(String channel) {
                addMsg("subscribed : " + channel);
            }

            @Override
            public void onUnSubscribed(String channel) {
                addMsg("unsubscribed : " + channel);
            }

            @Override
            public void onSubscriptionError(String method, String channel, String error) {
                addMsg("subscriptionError : " + method + ",  " + channel + ",  " + error);
            }

            @Override
            public void onJoin(JoinLeftBody joinLeftBody) {
                addMsg("join : " + joinLeftBody.getData());
            }

            @Override
            public void onLeave(JoinLeftBody joinLeftBody) {
                addMsg("leave : " + joinLeftBody.getData());
            }

            @Override
            public void onReceiveMessage(MessageBody messageBody) {
                addMsg("message : " + messageBody.getData());
            }

            @Override
            public void onAlive() {
                addMsg("alive ! ");
            }
        });

        mCentrifugo.connect();
    }

    private void addMsg(String msg) {
        Map<String, String> map = new HashMap<>();
        map.put("msg", msg);
        messageList.add(map);
        mSimpleAdapter.notifyDataSetChanged();
    }
}

package com.centrifugal.centrifuge.android.event;

import com.centrifugal.centrifuge.android.protocol.response.MessageBody;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    MessageEvent.java
 * ClassName:    MessageEvent
 * <p>
 * Description: 消息事件.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:28
 */
public class MessageEvent extends BaseEvent {

    private MessageBody messageBody;

    /**
     * 消息
     *
     * @param messageBody
     */
    public MessageEvent(MessageBody messageBody) {
        super(TYPE_RECEIVE_MESSAGE);
        this.messageBody = messageBody;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}

package com.centrifugal.centrifuge.android.event;

import com.centrifugal.centrifuge.android.protocol.CommandMethod;
import com.centrifugal.centrifuge.android.protocol.response.JoinLeftBody;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    JoinLeftEvent.java
 * ClassName:    JoinLeftEvent
 * <p>
 * Description: 加入/退出事件.
 *
 * @author hezhubo
 * @date 2017年12月08日 17:43
 */
public class JoinLeftEvent extends BaseEvent {

    private JoinLeftBody joinLeftBody;

    /**
     * 加入/退出
     *
     * @param method       {@link CommandMethod#JOIN}，{@link CommandMethod#LEAVE}
     * @param joinLeftBody
     */
    public JoinLeftEvent(String method, JoinLeftBody joinLeftBody) {
        super(TYPE_JOIN);
        if (CommandMethod.LEAVE.equals(method)) {
            type = TYPE_LEAVE;
        }
        this.joinLeftBody = joinLeftBody;
    }

    public JoinLeftBody getJoinLeftBody() {
        return joinLeftBody;
    }

}

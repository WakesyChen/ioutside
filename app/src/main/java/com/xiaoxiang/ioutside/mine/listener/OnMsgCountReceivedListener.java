package com.xiaoxiang.ioutside.mine.listener;

import com.xiaoxiang.ioutside.mine.model.MessageCount;

public interface OnMsgCountReceivedListener {
    void onReceivedMsg(MessageCount.DataBean msgCountInfo);
}
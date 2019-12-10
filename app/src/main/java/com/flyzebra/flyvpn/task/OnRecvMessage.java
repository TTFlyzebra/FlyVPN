package com.flyzebra.flyvpn.task;

import com.flyzebra.flyvpn.data.MpcMessage;

/**
 * ClassName: OnRecvMessage
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午11:17
 */
public interface OnRecvMessage {
    void recv(MpcMessage message);
}

package com.czl.chatClient.sender;

import com.czl.chatClient.bean.NettyMessage;

public interface SendMessageLisenter {

	void sendSusccess(NettyMessage message);

	void oncancellSend(NettyMessage message);

	void sendFailed(NettyMessage message,Throwable cause);

	void sendStart(NettyMessage message);

}

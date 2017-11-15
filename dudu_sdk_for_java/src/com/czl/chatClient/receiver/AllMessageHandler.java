package com.czl.chatClient.receiver;

import java.util.List;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.sender.JsonParser;

import io.netty.channel.Channel;

public abstract class AllMessageHandler implements RecivMessageCallBack {

	@Override
	public final List<AppServerType> getServerType() {
		// TODO Auto-generated method stub
		return AppServerType.allvalues();
	}

	@Override
	public final void onRecivMessage(Channel ctx, NettyMessage message,
			String tag, JsonParser paser) {
		// TODO Auto-generated method stub
		onReceiveMessage(message);
	}

	public abstract void onReceiveMessage(NettyMessage message);

}

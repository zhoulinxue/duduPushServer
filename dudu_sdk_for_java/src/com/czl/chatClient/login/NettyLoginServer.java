package com.czl.chatClient.login;

import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.bean.NettyServer;

import io.netty.channel.Channel;

/**
 * Created by zhouxue on 2017/8/4.
 */

public interface NettyLoginServer {
	NettyServer getServer(String server);

	void connect();

	void connect(String ip, int port, onConnetCallBack callBack);

	void reconnect();

	void changeAccout(DuduUser table);

	void changeServer(String ip, int port, onConnetCallBack callBack);

	Channel getChannel();

	void sendMessage(Channel channel, NettyMessage loginMessage);
}

package com.czl.chatClient.login;

import com.czl.chatClient.DuduClient;
import io.netty.channel.Channel;

/**
 * Created by Administrator on 2017/8/4.
 */

public interface onConnetCallBack {
	public void onConnectSucess(Channel channel);
	public void onConnectFailed();
	public boolean isReconnect();
	public void disconnect(Channel channel);
	public void addLisenter(DuduClient client);
}

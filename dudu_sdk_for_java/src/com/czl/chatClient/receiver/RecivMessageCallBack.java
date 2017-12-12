package com.czl.chatClient.receiver;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.sender.JsonParser;

import io.netty.channel.Channel;

/**
 * Created by zhouxue on 2017/8/4.
 */

public interface RecivMessageCallBack {

	public List<AppServerType> getServerType();

	public void onRecivMessage(Channel ctx, NettyMessage message, String tag, JsonParser parser)throws UnsupportedEncodingException;
}

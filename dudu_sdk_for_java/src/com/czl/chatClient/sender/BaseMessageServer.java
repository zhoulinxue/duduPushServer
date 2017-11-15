package com.czl.chatClient.sender;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.JsonString;
import com.czl.chatClient.bean.NettyMessage;

import io.netty.channel.Channel;

public interface BaseMessageServer {
	// 获取 消息头
	public String buildHead(NettyMessage msg);

	// 组件 消息对象
	public NettyMessage buildMessage(AppServerType header, JsonString content) throws UnsupportedEncodingException;

	public NettyMessage buildMessage(AppServerType header) throws UnsupportedEncodingException;

	// 分隔符
	public String seporate() throws Exception;

	// 获取内容的byte[]
	public byte[] getContentByte(JsonString content);

	// 发送消息
	public void sendMessage(NettyMessage message, Channel ctx) throws Exception;

	// 关闭 通道
	public void closeSocket(Channel ctx, String errorMsg);

	// 回复 客户端
	public void responeClient(Channel ctx, NettyMessage msg) throws UnsupportedEncodingException;

	// 回复 消息
	public void responeRs(Channel ctx, String header, JsonString content) throws UnsupportedEncodingException;

}

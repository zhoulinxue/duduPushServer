package com.czl.chatClient.handler;

import com.czl.chatClient.Constants;
import com.czl.chatClient.login.onConnetCallBack;
import com.czl.chatClient.receiver.RecivMessageCallBack;
import com.czl.chatClient.sender.JsonParser;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * Created by Administrator on 2017/8/4.
 */

public class MSChannelInitializer extends ChannelInitializer<SocketChannel> {
	private RecivMessageCallBack callBack;
	private onConnetCallBack connect;
	private JsonParser parser;
	private String content;

	public MSChannelInitializer(RecivMessageCallBack callBack, onConnetCallBack connect, JsonParser parser,String content) {
		this.callBack = callBack;
		this.connect = connect;
		this.parser = parser;
		this.content=content;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024 * 1024, false,
				Unpooled.copiedBuffer(Constants.MESSAFE_END_TAG.getBytes())));
		socketChannel.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
		socketChannel.pipeline().addLast("NettyMessageEncoder", new NettyMessageEncoder());
		socketChannel.pipeline().addLast("SocketClientShortHandler", new SocketClientShortHandler(callBack,connect,parser,content));

	}
}

package com.czl.chatClient.handler;

import java.util.concurrent.TimeUnit;

import com.czl.chatClient.Constants;
import com.czl.chatClient.login.onConnetCallBack;
import com.czl.chatClient.receiver.RecivMessageCallBack;
import com.czl.chatClient.sender.JsonParser;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by Administrator on 2017/8/4.
 */

public class NSChannelInitializer extends ChannelInitializer<SocketChannel> {
	private RecivMessageCallBack callBack;
	private onConnetCallBack connect;
	private JsonParser parser;

	public NSChannelInitializer(RecivMessageCallBack callBack, onConnetCallBack connect, JsonParser parser) {
		super();
		this.callBack = callBack;
		this.connect = connect;
		this.parser = parser;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024 * 1024, false,
				Unpooled.copiedBuffer(Constants.MESSAFE_END_TAG.getBytes())));
		socketChannel.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
		socketChannel.pipeline().addLast("NettyMessageEncoder", new NettyMessageEncoder());
		socketChannel.pipeline().addLast("heartbeatHandler",new HeartBeatHandler());
		socketChannel.pipeline().addLast("readTimeoutHandler", new IdleStateHandler(50, 50, 50, TimeUnit.SECONDS));
		socketChannel.pipeline().addLast("SocketNSHandler", new NettymessageHandler(callBack, connect, parser));

	}
}

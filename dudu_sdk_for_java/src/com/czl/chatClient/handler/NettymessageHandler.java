package com.czl.chatClient.handler;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.login.onConnetCallBack;
import com.czl.chatClient.receiver.RecivMessageCallBack;
import com.czl.chatClient.sender.JsonParser;
import com.czl.chatClient.utils.Log;
import com.czl.chatClient.utils.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by Administrator on 2017/8/4.
 */

public class NettymessageHandler extends ChannelInboundHandlerAdapter {
	private RecivMessageCallBack callBack;
	private onConnetCallBack connect;
	private JsonParser parser;

	public NettymessageHandler(RecivMessageCallBack callBack, onConnetCallBack connect, JsonParser parser) {
		super();
		this.callBack = callBack;
		this.connect = connect;
		this.parser = parser;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
		callBack.onRecivMessage(ctx.channel(), (NettyMessage) msg, callBack.getClass().getSimpleName(), parser);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		Log.e("Dudu_SDK","exceptionCaught......"+cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		connect.disconnect(ctx.channel());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		super.userEventTriggered(ctx, evt);
		Log.e("Dudu_SDK", "IdleStateEvent");
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			Log.e("Dudu_SDK", "IdleStateEvent"+e.state());
			switch (e.state()) {
			case READER_IDLE:
				break;
			case WRITER_IDLE:
				ctx.writeAndFlush(buildHeatBeat(e.state()+""));
				break;
			default:
				break;
			}
		}
	}

	private NettyMessage buildHeatBeat(String type) {
		NettyMessage message = new NettyMessage();
		message.setHeader(AppServerType.HEART_BEAT);
		message.setMessageId(StringUtils.getRandomMsgId());
		message.setContent("".getBytes());
		return message;
	}
}

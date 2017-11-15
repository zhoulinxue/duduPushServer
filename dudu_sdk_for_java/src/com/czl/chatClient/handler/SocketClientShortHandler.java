package com.czl.chatClient.handler;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.login.onConnetCallBack;
import com.czl.chatClient.receiver.RecivMessageCallBack;
import com.czl.chatClient.sender.JsonParser;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class SocketClientShortHandler extends ChannelInboundHandlerAdapter {
	public RecivMessageCallBack messageBuff;
	private JsonParser parser;

	public SocketClientShortHandler(RecivMessageCallBack stringBuffer, onConnetCallBack connect, JsonParser parser) {
		this.messageBuff = stringBuffer;
		this.parser=parser;
	}

	/**
	 * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward to the
	 * next {@link ChannelHandler} in the {@link ChannelPipeline}.
	 * 
	 * Sub-classes may override this method to change behavior.
	 */
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(buildLoginReq());// AC
		// Demo.ctx=ctx;
	}

	/**
	 * Calls {@link ChannelHandlerContext#fireChannelRead(Object)} to forward to
	 * the next {@link ChannelHandler} in the {@link ChannelPipeline}.
	 * 
	 * Sub-classes may override this method to change behavior.
	 */
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		messageBuff.onRecivMessage(ctx.channel(),message,messageBuff.getClass().getCanonicalName(),parser);
		ctx.fireChannelRead(msg);
		ReferenceCountUtil.release(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
		ctx.flush();
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.fireExceptionCaught(cause);
	}
	///////////////////////////////////////////////

	private NettyMessage buildLoginReq() {
		NettyMessage message = new NettyMessage();

		message.setHeader0((byte) 65);// A
		message.setHeader1((byte) 67);
		try {
			message.setContent(("").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	// 设置心跳时间 结束

	// 利用写空闲发送心跳检测消息
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case WRITER_IDLE:
				ctx.writeAndFlush(buildHeatBeat());
				break;
			default:
				break;
			}
		}
	}

	private NettyMessage buildHeatBeat() {
		NettyMessage message = new NettyMessage();

		message.setHeader0((byte) 67);
		message.setHeader1((byte) 75);
		try {
			message.setContent(("").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
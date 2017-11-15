package com.czl.chatClient.handler;

import java.io.IOException;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

	public NettyMessageEncoder() throws IOException {

	}

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {
//		byte[] msgId = null;
//		if (msg.getMessageId() != null) {
//			msgId = msg.getMessageId();
//		} else {
//			msgId = StringUtils.getRandomMsgId();
//		}
//		sendBuf.writeByte(msgId.length);
//		sendBuf.writeBytes(msgId);
		sendBuf.writeByte(msg.getHeader0());
		sendBuf.writeByte(msg.getHeader1());
		AppServerType type= AppServerType.ofCommand(msg.getHeader());
		if (AppServerType.byteValus().contains(type)) {
			sendBuf.writeInt(msg.getCtxLength());
			sendBuf.writeBytes(msg.getContent());
		}else {
			sendBuf.writeByte(124);
			sendBuf.writeBytes(msg.getContent());
			sendBuf.writeByte(10);
		}
		Log.printeNettymsg(msg, "发送的消息");
		// ctx.writeAndFlush(sendBuf);
	}
}

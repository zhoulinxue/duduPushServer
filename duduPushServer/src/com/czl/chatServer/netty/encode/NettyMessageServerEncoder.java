package com.czl.chatServer.netty.encode;

import java.io.IOException;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述：发出消息 编码类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public final class NettyMessageServerEncoder extends MessageToByteEncoder<NettyMessage> {

	public NettyMessageServerEncoder() throws IOException {

	}

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {

		try {
			sendBuf.writeByte(msg.getHeader0());
			sendBuf.writeByte(msg.getHeader1());
			sendBuf.writeByte(124);
			
			if (msg.getHeader0() == 83 &&( msg.getHeader1() == 77||msg.getHeader1() == 71)) {
				sendBuf.writeInt(msg.getCtxLength());
				sendBuf.writeBytes(msg.getContent());
			}  else {
				sendBuf.writeBytes(msg.getContent());
			}
			if(msg.getFromUerId()!=null && msg.getFromUerId().length!=0){
				sendBuf.writeBytes(msg.getFromUerId());
			}
//			if(msg.getMessageId()!=null){
//				sendBuf.writeBytes(msg.getMessageId());
//			}else{
//				byte[] msgId=ManageAPI.getRandomMsgId();
//				sendBuf.writeBytes(msgId);
//			}			
			Log.printeNettymsg(msg, "发送的消息");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		  ReferenceCountUtil.release(msg);
		}
	}
}

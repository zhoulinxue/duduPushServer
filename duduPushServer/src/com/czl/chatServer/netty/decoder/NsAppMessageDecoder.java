package com.czl.chatServer.netty.decoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述：前端指令 解码类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class NsAppMessageDecoder extends LengthFieldBasedFrameDecoder {

	public NsAppMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);

	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

		if (in.readableBytes() < 2) {
			return null;
		}
		in.markReaderIndex();//
		NettyMessage message = new NettyMessage();
		message.setHeader0(in.readByte());
		message.setHeader1(in.readByte());
	
		if (message.getHeader0() == 83 && message.getHeader1() == 77) {
			if (in.readableBytes() < 4) {
				in.resetReaderIndex();
				return null;
			} else {
				message.setCtxLength(in.readInt());
				if (message.getCtxLength() > 1024 * 1024) {
					in.resetReaderIndex();
					if (in.readableBytes() > 1024 * 1024) {
						in.skipBytes(1024 * 1020);
						in.discardReadBytes();
					}
					return null;
				}
				if (in.readableBytes() < message.getCtxLength()) {
					System.out.println("内容大小——大于可读字节"+message.getCtxLength()+"@@"+in.readableBytes());
					in.resetReaderIndex();
					return null;
				} else {
					System.out.println("小于可读字节"+message.getCtxLength()+"@@"+in.readableBytes());
					byte[] req = new byte[message.getCtxLength()];
					in.readBytes(req);
					message.setContent(req);
					
					if(in.readableBytes()<2){
						in.discardReadBytes();
						return message;
					}
					// 前端发语音流 没有uid 所以没有“\n” 此处不用跳字节
//					byte[] idbytes=new byte[6];
//					in.readBytes(idbytes);
//					message.setMessageId(idbytes);
					
					in.discardReadBytes();					
					return message;
				}
			}
		} else if (message.getHeader0() == 83 && message.getHeader1() == 71) {
			if (in.readableBytes() < 4) {
				in.resetReaderIndex();
				return null;
			} else {
				message.setCtxLength(in.readInt());
				if (message.getCtxLength() > 1024 * 1024) {
					in.resetReaderIndex();
					if (in.readableBytes() > 1024 * 1024) {
						in.skipBytes(1024 * 1020);
						in.discardReadBytes();
					}
					return null;
				}
				if (in.readableBytes() < message.getCtxLength()) {
					System.out.println("内容大小——大于可读字节"+message.getCtxLength()+"@@"+in.readableBytes());
					in.resetReaderIndex();
					return null;
				} else {
					System.out.println("小于可读字节"+message.getCtxLength()+"@@"+in.readableBytes());
					byte[] req = new byte[message.getCtxLength()];
					in.readBytes(req);
					message.setContent(req);					
					if(in.readableBytes()<2){
						in.discardReadBytes();
						return message;
					}
					// 前端发语音流 没有uid 所以没有“\n” 此处不用跳字节
//					byte[] idbytes=new byte[6];
//					in.readBytes(idbytes);
//					message.setMessageId(idbytes);
					in.discardReadBytes();
				
					return message;

					///////////////////////
				}
			}

		} else {
			
			if (AppServerType.isCommand(message.getHeader())) {
				
				if (in.readableBytes() < 1) {
					in.resetReaderIndex();
					return null;
				}
				int nn = in.bytesBefore((byte) 10);// \n
				if (nn < 0) {
					in.resetReaderIndex();
					if (in.readableBytes() > 1536) {
						in.skipBytes(1024);
						in.discardReadBytes();
					}
					return null;
				} else {
					//content 中包含"|"
					byte[] req = new byte[nn + 2];
					in.resetReaderIndex();
					in.readBytes(req);
					message.setContent(req);
					
					if(in.readableBytes()<2){
						in.skipBytes(1);
						in.discardReadBytes();
						return message;
					}	
					// 跳过"\n"符号
					in.skipBytes(1);
//					byte[] idbytes=new byte[6];
//					in.readBytes(idbytes);
//					message.setMessageId(idbytes);		
//					System.out.println("header=:"+message.getHeader()+"   msgId=:"+message.getStringMessageId());
					
					in.discardReadBytes();
					return message;
				}
			} else {
				writeProError(ctx);
				in.clear();
				in.discardReadBytes();
				return null;
			}
		}
	}

	private void writeProError(ChannelHandlerContext ctx) throws UnsupportedEncodingException {
		ctx.write((byte) 69);
		ctx.write((byte) 88);
		ctx.write((byte) 124);
		ctx.write((byte) 70);
		ctx.write((byte) 124);
		ctx.write("协议错误".getBytes("UTF-8"));
		ctx.writeAndFlush((byte) 10);
	}
}

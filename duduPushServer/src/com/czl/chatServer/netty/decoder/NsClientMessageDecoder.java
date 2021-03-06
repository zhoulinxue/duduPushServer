package com.czl.chatServer.netty.decoder;

import java.io.IOException;

import com.czl.chatClient.bean.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述：MS 链接指令 解码类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class NsClientMessageDecoder extends LengthFieldBasedFrameDecoder {

	public NsClientMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);

	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws IOException {

		if (in.readableBytes() < 2) {
			return null;
		}
		in.markReaderIndex();//
		NettyMessage message = new NettyMessage();
        int length = in.readByte();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return null;
        }
        byte[] idbytes = new byte[length];
        in.readBytes(idbytes);
        message.setMessageId(idbytes);
        if (in.readableBytes() < 2) {
            in.resetReaderIndex();
            return null;
        }
		message.setHeader0(in.readByte());
		message.setHeader1(in.readByte());
	
		if (message.getHeader0() == 83 && message.getHeader1() == 77) {
			if (in.readableBytes() < 4) {
				in.resetReaderIndex();
				return null;
			} else {
				message.setCtxLength(in.readInt());
				if(message.getCtxLength()>1024*1024){
					in.resetReaderIndex();
					if (in.readableBytes() > 1024*1024) {
						in.skipBytes(1024*1020);
						in.discardReadBytes();
					}
					return null;
				}
				if (in.readableBytes() < message.getCtxLength()) {
					in.resetReaderIndex();
					return null;
				} else {
					byte[] req = new byte[message.getCtxLength()];
					in.readBytes(req);
					message.setContent(req);
//					System.out.println("读content"+StringUtils.toString(req));
					///////////////////////////
					int nn = in.bytesBefore((byte) 10);// \n
					if (nn < 0) {
						in.resetReaderIndex();
						return null;
					} else {
						byte[] req2 = new byte[nn];
						// in.resetReaderIndex();
						in.readBytes(req2);
						message.setFromUerId(req2);
												
						if(in.readableBytes()<2){
							in.skipBytes(1);
							in.discardReadBytes();
							return message;
						}
						// 跳过"\n"符号  
						in.skipBytes(1);
//						byte[] idbytes=new byte[6];
//						in.readBytes(idbytes);
//						message.setMessageId(idbytes);
						
						in.discardReadBytes();
						return message;
					}
				}
			}

		} else if (message.getHeader0() == 83 && message.getHeader1() == 71) {
			if (in.readableBytes() < 4) {
				in.resetReaderIndex();
				return null;
			} else {
				message.setCtxLength(in.readInt());
				if(message.getCtxLength()>1024*1024){
					in.resetReaderIndex();
					if (in.readableBytes() > 1024*1024) {
						in.skipBytes(1024*1020);
						in.discardReadBytes();
					}
					return null;
				}
				if (in.readableBytes() < message.getCtxLength()) {
					in.resetReaderIndex();
					return null;
				} else {
					byte[] req = new byte[message.getCtxLength()];
					in.readBytes(req);
					message.setContent(req);
					///////////////////////////
					int nn = in.bytesBefore((byte) 10);// \n
					if (nn < 0) {
						in.resetReaderIndex();
						return null;
					} else {
						byte[] req2 = new byte[nn];
						// in.resetReaderIndex();
						in.readBytes(req2);
						message.setFromUerId(req2);
						
						if(in.readableBytes()<2){
							in.skipBytes(1);
							in.discardReadBytes();
							return message;
						}
						// 跳过"\n"符号
						in.skipBytes(1);
//						byte[] idbytes=new byte[6];
//						in.readBytes(idbytes);
//						message.setMessageId(idbytes);
						
						in.discardReadBytes();
						return message;
					}

					///////////////////////
				}
			}

		} else {
			if ((message.getHeader0() == 48 && message.getHeader1() == 48)// 00
                 ) {
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
//					System.out.println("读id"+StringUtils.toString(idbytes));
					
					in.discardReadBytes();
					return message;
				}

			} else {
				in.resetReaderIndex();
				if (in.readableBytes() > 1536) {
					in.skipBytes(1024);
					in.discardReadBytes();
				}else{
					in.skipBytes(1);
					in.discardReadBytes();
				}
				
				return null;
			}
		}
	}
}

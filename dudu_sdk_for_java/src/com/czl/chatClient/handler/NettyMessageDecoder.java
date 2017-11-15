package com.czl.chatClient.handler;

import java.io.IOException;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);

	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws IOException {
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
						in.skipBytes(1);
//						byte[] idbytes=new byte[6];
//						in.readBytes(idbytes);
//						message.setMessageId(idbytes);

						in.discardReadBytes();
						Log.printeNettymsg(message,"接收到消息");
						return message;
					}

					///////////////////////

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
						// 跳过"\n"符号
						in.skipBytes(1);

//						byte[] idbytes=new byte[6];
//						in.readBytes(idbytes);
//						message.setMessageId(idbytes);
						in.discardReadBytes();
						Log.printeNettymsg(message,"接收到消息");
						return message;
					}

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
					byte[]  req= new byte[nn-1];
					// 跳过"|"符号
					in.skipBytes(1);
					in.readBytes(req);
					message.setContent(req);
					// 跳过"\n"符号
					in.skipBytes(1);
//					byte[] idbytes=new byte[6];
//					in.readBytes(idbytes);
//					message.setMessageId(idbytes);

					in.discardReadBytes();
					Log.printeNettymsg(message,"接收到消息");
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

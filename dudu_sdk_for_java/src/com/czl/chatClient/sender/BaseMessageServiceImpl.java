package com.czl.chatClient.sender;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.Constants;
import com.czl.chatClient.DuduClient;
import com.czl.chatClient.bean.JsonString;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;
import com.czl.chatClient.utils.StringUtils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class BaseMessageServiceImpl implements BaseMessageServer {
	protected ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

	@Override
	public String buildHead(NettyMessage message) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append((char) message.getHeader0());
		buffer.append((char) message.getHeader1());
		return buffer.toString();
	}

	@Override
	public NettyMessage buildMessage(AppServerType header) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		NettyMessage message = new NettyMessage();
		message.setMessageId(getRandomMsgId());
		message.setHeader0(header.getHeaderString().getBytes(Constants.HEAD_CHAR_SET)[0]);
		message.setHeader1(header.getHeaderString().getBytes(Constants.HEAD_CHAR_SET)[1]);
		return message;
	}

	public byte[] getRandomMsgId() {
		// TODO Auto-generated method stub
		String msgId = getRandomString(6);
		try {
			return msgId.getBytes(Constants.CONTENT_CHAR_SET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	@Override
	public String seporate() {
		// TODO Auto-generated method stub
		return Constants.SEPORATE;
	}

	@Override
	public void sendMessage(NettyMessage message, Channel channel) {
		// TODO Auto-generated method stub
		sendMessage(message, channel, DuduClient.getInstance());
	}

	public void sendMessage(final NettyMessage message, final Channel channel, final SendMessageLisenter listener) {
		// TODO Auto-generated method stub
		if (channel != null) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					ChannelFuture future = channel.writeAndFlush(message);
					listener.sendStart(message);
					future.addListener(new ChannelFutureListener() {

						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							// TODO Auto-generated method stub
							if (future.isSuccess()) {
								listener.sendSusccess(message);
							} else if (future.isCancelled()) {
								listener.oncancellSend(message);
							} else if (!future.isSuccess()) {
								listener.sendFailed(message, future.cause());
							}

						}
					});
				}
			});
		} else {
			Log.e("Dudu_SDK", "消息发送失败 请链接后重试/" + message.getHeader());
			listener.sendFailed(message, new IllegalStateException("消息发送失败 请链接后重试"));
		}
	}

	@Override
	public NettyMessage buildMessage(AppServerType header, JsonString content) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		NettyMessage message = buildMessage(header);
		message.setContent(getContentByte(content));
		return message;
	}

	@Override
	public byte[] getContentByte(JsonString content) {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(content.getContent())) {
			throw new IllegalArgumentException("JSON String  can not be null..");
		}
		return StringUtils.tobyte(content.getMessage().toString());
	}

	@Override
	public void closeSocket(Channel ctx, String errorMsg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void responeClient(Channel ctx, NettyMessage msg) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

	}

	@Override
	public void responeRs(Channel ctx, String header, JsonString content) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		NettyMessage rsmsg = buildMessage(AppServerType.RS);
		rsmsg.setContent(getContentByte(content));
		sendMessage(rsmsg, ctx);
	}

}

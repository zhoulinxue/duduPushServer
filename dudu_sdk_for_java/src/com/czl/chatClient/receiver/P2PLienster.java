package com.czl.chatClient.receiver;

import java.util.List;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.ChatMessage;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.sender.JsonParser;

import io.netty.channel.Channel;

public interface P2PLienster extends RecivMessageCallBack {

	List<AppServerType> getServerType();

	void onRecivMessage(Channel channel, NettyMessage message, String tag,
			JsonParser parser);

	public void chattingWith(DuduUser cfUser);

	public void onGetUserPosion(DuduPosition position);

	/**
	 * receive a call
	 * 
	 * @param fromUser
	 *            the caller
	 */
	public void onReceiveCall(DuduUser fromUser);

	/**
	 * chatting be huang up
	 * 
	 * @param fromUser
	 *            who huang up the call
	 */
	public void onChattingEnd(DuduUser fromUser);

	/**
	 * onRecive Audo msg
	 * 
	 * @param user
	 * @param bytes
	 */

	public void onReceiveChatMessage(DuduUser user, ChatMessage message);

	/**
	 * 呼叫 被接听
	 * 
	 * @param fromUser
	 *            who accepted your call call has been accepted by user
	 */

	public void onCallAccepted(DuduUser fromUser);

	/**
	 * 呼叫被拒绝
	 * 
	 * @param fruser
	 *            who Reject your call call has been rejected by user
	 */
	public void onCallRejected(DuduUser fruser);

	/**
	 * 呼叫 被取消
	 * 
	 * @param feuser
	 *            who canceled your call
	 */

	public void onCallCanceled(DuduUser feuser);

	/**
	 *
	 * @param etuser
	 */
	public  void onReceiveET(DuduUser etuser);
}

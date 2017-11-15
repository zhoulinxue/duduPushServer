package com.czl.chatClient.receiver;

import java.util.List;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.ChannalActiveusers;
import com.czl.chatClient.bean.ChatMessage;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.sender.JsonParser;

import io.netty.channel.Channel;

public interface GroupLienster extends RecivMessageCallBack {
	List<AppServerType> getServerType();

	void onRecivMessage(Channel ctx, NettyMessage message, String tag,
			JsonParser parser);

	public void chattingInGroup(Groupbean cggroup);

	public void onGetUserPosion(DuduPosition position);

	public void onReceiveChatMessage(DuduUser smuser, ChatMessage msg);

	public void enterChannel(ChannalActiveusers activeusers);

	public void onUserEixt(DuduUser fauser);

	public void onNewUserIn(DuduPosition feuser,Groupbean groupbean);

	public void onReceiveGroupCall(DuduUser user, Groupbean group);

	public void onReceiveGT(DuduUser etuser) ;

	public void groupActive(List<String> channemlIds, boolean b);

	public void groupInActive(String id);

	public void onKnickOutOfChannel(Groupbean ncGroup);

}

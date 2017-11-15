package com.czl.chatClient.receiver;

import java.util.List;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.bean.Pushmessage;
import com.czl.chatClient.bean.Responbean;
import com.czl.chatClient.sender.JsonParser;

import io.netty.channel.Channel;

public interface DefaultLisenter extends RecivMessageCallBack {

	List<AppServerType> getServerType();

	void onRecivMessage(Channel ctx, NettyMessage message, String tag, JsonParser parser);

	public void onReceiveIMMsg(Pushmessage pmsg);

	public void onReceivi00Msg(String string);

	public void offLinNotice(String split);

	public void onException(String data);

	public void onReceiverPositions(List<DuduPosition> positions);

	public void onReceiveRespons(Responbean responbean,String uid);

	public void onReTranfficMeta(String split);

	public void onUserOnLine(String userid);

	public void onUserOffLine(String userid);

	public void onReceiveOfflineMessage(List<Pushmessage> lists);

	public void onGroupMessageChanged(Groupbean groupbean);

	public void onReceiveFMMessage(DuduUser fromUser, String fmMsg);

	public void onReceiveDefaultMessage(NettyMessage message);
	
	public void onPositionChanged(DuduPosition pos) ;
}

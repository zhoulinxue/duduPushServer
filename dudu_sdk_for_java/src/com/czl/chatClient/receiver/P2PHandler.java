package com.czl.chatClient.receiver;

import java.util.List;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.ChatMessageType;
import com.czl.chatClient.bean.ChatMessage;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.sender.JsonParser;
import com.czl.chatClient.utils.Log;

import io.netty.channel.Channel;

public abstract class P2PHandler implements P2PLienster {

	@Override
	public final List<AppServerType> getServerType() {
		// TODO Auto-generated method stub
		return AppServerType.P2PValus();
	}

	@Override
	public final void onRecivMessage(Channel channel, NettyMessage message,
			String tag, JsonParser parser) {
		// TODO Auto-generated method stub
		try {
			String data = new String(message.getContent(), "UTF-8");
			String[] splits = data.split("\\|");
			switch (AppServerType.ofCommand(message.getHeader())) {
			case FN:
				DuduUser user = (DuduUser) parser.parseObject(splits[0],
						DuduUser.class);
				 Log.e("Dudu_SDK",
						"P2PHandler_______onReceiveCall__" + user.getUserid());
				onReceiveCall(user);
				break;
			case ET:
				DuduUser etuser = (DuduUser) parser.parseObject(splits[0],
						DuduUser.class);
				 Log.e("Dudu_SDK",
						"P2PHandler_______onReceiveET__" + etuser.getUserid());
				onReceiveET(etuser);
				break;
			case FE:
				DuduUser feuser = (DuduUser) parser.parseObject(splits[0],
						DuduUser.class);
				 Log.e(
						"Dudu_SDK",
						"P2PHandler_______onCallCanceled__"
								+ feuser.getUserid());
				onCallCanceled(feuser);
				break;
			case FA:
				DuduUser fauser = (DuduUser) parser.parseObject(splits[0],
						DuduUser.class);
				 Log.e(
						"Dudu_SDK",
						"P2PHandler_______onCallAccepted__"
								+ fauser.getUserid());
				onCallAccepted(fauser);
				break;
			case FR:
				DuduUser fruser = (DuduUser) parser.parseObject(splits[0],
						DuduUser.class);
				 Log.e(
						"Dudu_SDK",
						"P2PHandler_______onCallRejected__"
								+ fruser.getUserid());
				onCallRejected(fruser);
				break;
			case ED:
				DuduUser eduser = (DuduUser) parser.parseObject(splits[0],
						DuduUser.class);
				 Log
						.e("Dudu_SDK", "P2PHandler_______onChattingEnd__"
								+ eduser.getUserid());
				onChattingEnd(eduser);
				break;
			case XY:
				DuduPosition position = (DuduPosition) parser.parseObject(
						splits[0], DuduPosition.class);
				 Log.e(
						"Dudu_SDK",
						"GroupHandler_______onGetUserPosion__"
								+ position.getUserid());
				onGetUserPosion(position);
			case SM:
				DuduUser smuser = new DuduUser();
				smuser.setUserid(message.getStringFromUerId());
				ChatMessage msg = new ChatMessage();
				msg.setBytes(message.getContent());
				msg.setType(ChatMessageType.VOICE);
				 Log.e(
						"Dudu_SDK",
						"P2PHandler_______onReceiveChatMessage__"
								+ smuser.getUserid());
				onReceiveChatMessage(smuser, msg);
				break;
			case CF:
				DuduUser cfUser = (DuduUser) parser.parseObject(splits[0],
						DuduUser.class);
				 Log.e("Dudu_SDK",
						"P2PHandler_______chatiingWith__" + cfUser.getUserid());
				chattingWith(cfUser);
				break;
			case TX:
				DuduUser txuser = new DuduUser();
				txuser.setUserid(message.getStringFromUerId());
				ChatMessage txmsg = new ChatMessage();
				txmsg.setBytes(message.getContent());
				txmsg.setType(ChatMessageType.NOMAL);
				 Log.e(
						"Dudu_SDK",
						"P2PHandler_______onReceiveChatMessage__"
								+ txuser.getUserid());
				onReceiveChatMessage(txuser, txmsg);
				break;
			case PI:
				DuduUser piuser = new DuduUser();
				piuser.setUserid(message.getStringFromUerId());
				ChatMessage pimsg = new ChatMessage();
				pimsg.setBytes(message.getContent());
				pimsg.setType(ChatMessageType.PICTRUE);
				 Log.e(
						"Dudu_SDK",
						"P2PHandler_______onReceiveChatMessage__"
								+ piuser.getUserid());
				onReceiveChatMessage(piuser, pimsg);
				break;

			case FI:
				DuduUser fiuser = new DuduUser();
				fiuser.setUserid(message.getStringFromUerId());
				ChatMessage fimsg = new ChatMessage();
				fimsg.setBytes(message.getContent());
				fimsg.setType(ChatMessageType.PICTRUE);
				 Log.e(
						"Dudu_SDK",
						"P2PHandler_______onReceiveChatMessage__"
								+ fiuser.getUserid());
				onReceiveChatMessage(fiuser, fimsg);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

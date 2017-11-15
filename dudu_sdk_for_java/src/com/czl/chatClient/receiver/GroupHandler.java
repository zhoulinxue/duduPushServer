package com.czl.chatClient.receiver;

import java.util.ArrayList;
import java.util.List;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.ChatMessageType;
import com.czl.chatClient.bean.ChannalActiveusers;
import com.czl.chatClient.bean.ChatMessage;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.sender.JsonParser;
import com.czl.chatClient.utils.Log;

import io.netty.channel.Channel;

public abstract class GroupHandler implements GroupLienster {

	@Override
	public final List<AppServerType> getServerType() {
		// TODO Auto-generated method stub
		return AppServerType.GroupValus();
	}

	@Override
	public final void onRecivMessage(Channel ctx, NettyMessage message,
			String tag, JsonParser parser) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		try {
			String data = new String(message.getContent(), "UTF-8");
			String[] splits = data.split("\\|");
			switch (AppServerType.ofCommand(message.getHeader())) {
			case GT:
				DuduUser etuser = (DuduUser) parser.parseObject(splits[0],
						DuduUser.class);
				 Log.e("Dudu_SDK",
						"P2PHandler_______onReceiveET__" + etuser.getUserid());
				onReceiveGT(etuser);
				break;
			case GM:
				DuduUser user = (DuduUser) parser.parseObject(splits[0],
						DuduUser.class);
				Groupbean group = (Groupbean) parser.parseObject(splits[1],Groupbean.class);
				 Log.e(
						"Dudu_SDK",
						"GroupHandler_______onReceiveGroupCall__"
								+ user.getUserid());
				onReceiveGroupCall(user, group);
				List<String> channemlIds = new ArrayList<>();
				channemlIds.add(group.getGroupId());
				groupActive(channemlIds, true);
				break;
			case NT:
				DuduPosition feuser = (DuduPosition) parser.parseObject(splits[0],
						DuduPosition.class);
				Groupbean groupbean= (Groupbean) parser.parseObject(splits[1],Groupbean.class);

				 Log
						.e("Dudu_SDK", "GroupHandler_______onNewUserIn__"
								+ feuser.getUserid());
				onNewUserIn(feuser,groupbean);
				break;
			case EG:
				DuduUser fauser = (DuduUser) parser.parseObject(splits[0],
						DuduUser.class);
				 Log.e("Dudu_SDK",
						"GroupHandler_______onUserEixt__" + fauser.getUserid());
				onUserEixt(fauser);
				break;
			case AL:
				ChannalActiveusers activeusers = (ChannalActiveusers) parser.parseObject(splits[0],
						ChannalActiveusers.class);
				 Log.e(
						"Dudu_SDK",
						"GroupHandler_______enterChannel__"
								+ activeusers.getChannelId());
				enterChannel(activeusers);
				break;

			case DM:
				String id = splits[0];
				 Log.e(
						"Dudu_SDK",
						"GroupHandler_______groupInActive__");
				groupInActive(id);
				break;
			case CA:
				@SuppressWarnings("unchecked")
				List<String> cdchannemlIds = (List<String>) parser.parseArray(splits[0], String.class);
				 Log.e(
						"Dudu_SDK",
						"GroupHandler_______groupActive__");
				groupActive(cdchannemlIds, false);
				break;
			case CG:
				Groupbean cggroup = (Groupbean) parser.parseObject(splits[0],
						Groupbean.class);
				 Log.e(
						"Dudu_SDK",
						"GroupHandler_______chatiingWith__"
								+ cggroup.getGroupId());
				chattingInGroup(cggroup);
				break;
			case SM:
			case SG:
				DuduUser smuser = new DuduUser();
				smuser.setUserid(message.getStringFromUerId());
				ChatMessage msg = new ChatMessage();
				msg.setBytes(message.getContent());
				msg.setType(ChatMessageType.VOICE);
				 Log.e(
						"Dudu_SDK",
						"GroupHandler_______onReceiveChatMessage__"
								+ smuser.getUserid());
				onReceiveChatMessage(smuser, msg);
				break;
			case TX:
				DuduUser txuser = new DuduUser();
				txuser.setUserid(message.getStringFromUerId());
				ChatMessage txmsg = new ChatMessage();
				txmsg.setBytes(message.getContent());
				txmsg.setType(ChatMessageType.NOMAL);
				 Log.e(
						"Dudu_SDK",
						"GroupHandler_______onReceiveChatMessage__"
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
						"GroupHandler_______onReceiveChatMessage__"
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
						"GroupHandler_______onReceiveChatMessage__"
								+ fiuser.getUserid());
				onReceiveChatMessage(fiuser, fimsg);
				break;
			case XZ:
				DuduPosition position = (DuduPosition) parser.parseObject(
						splits[0], DuduPosition.class);
				 Log.e(
						"Dudu_SDK",
						"GroupHandler_______onGetUserPosion__"
								+ position.getUserid());
				onGetUserPosion(position);
				break;
				case NC:
					Groupbean ncGroup = (Groupbean) parser.parseObject(splits[0],
							Groupbean.class);
					 Log.e(
							"Dudu_SDK",
							"GroupHandler_______onKnickOutOfChannel__"
									+ ncGroup.getGroupId());
					onKnickOutOfChannel(ncGroup);
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

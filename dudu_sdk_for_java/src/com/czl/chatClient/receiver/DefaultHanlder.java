package com.czl.chatClient.receiver;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.bean.Pushmessage;
import com.czl.chatClient.bean.Responbean;
import com.czl.chatClient.sender.JsonParser;
import com.czl.chatClient.utils.Log;

import io.netty.channel.Channel;

public abstract class DefaultHanlder implements DefaultLisenter {

	@Override
	public final List<AppServerType> getServerType() {
		// TODO Auto-generated method stub
		return AppServerType.othersValus();
	}

	@Override
	public final void onRecivMessage(Channel ctx, NettyMessage message,
			String tag, JsonParser parser) {
		// TODO Auto-generated method stub
		try {
			String data = new String(message.getContent(), "UTF-8");
			String[] splits = data.split("\\|");
			switch (AppServerType.ofCommand(message.getHeader())) {
			case RETURN_TAG:
				 Log.e("Dudu_SDK","DefaultHanlder_______onReceivi00Msg__"
						+ splits[0]);
				onReceivi00Msg(splits[0]);
				break;
				case PU:
					List<Pushmessage> lists = JSONObject.parseArray(splits[0], Pushmessage.class);
					 Log.e("Dudu_SDK","DefaultHanlder_______onReceiveOfflineMessage__");
					onReceiveOfflineMessage(lists);
					break;
			case IM:
				Pushmessage pmsg = (Pushmessage) parser.parseObject(splits[2],
						Pushmessage.class);
				 Log.e("Dudu_SDK","DefaultHanlder_______onReceiveIMMsg__");
				onReceiveIMMsg(pmsg);
				break;
			case EX_TYPE:
				if ("100".equals(splits[0])) {
					offLinNotice(splits[1]);
				} else {
					onException(data);
				}
				break;
			case PT:
				@SuppressWarnings("unchecked")
				List<DuduPosition> positions = (List<DuduPosition>) parser
						.parseArray(splits[0], DuduPosition.class);
				 Log.e("Dudu_SDK","DefaultHanlder_______onReceiverPositions__");
				onReceiverPositions(positions);
				break;
				case RS:
					Responbean responbean= (Responbean) parser.parseObject(splits[0],Responbean.class);
					onReceiveRespons(responbean,splits[1]);
					break;
				case TA:
					 Log.e("Dudu_SDK","DefaultHanlder_______onReTranfficMeta__"
						+ message.getHeader());
					onReTranfficMeta(splits[0]);
					break;
				case ON:
					 Log.e("Dudu_SDK","DefaultHanlder_______onUserOnLine__");
					onUserOnLine(splits[0]);
					break;
				case OF:
					 Log.e("Dudu_SDK","DefaultHanlder______onUserOffLine__");
					onUserOffLine(splits[0]);
					break;
				case GC:
					Groupbean groupbean= (Groupbean) parser.parseObject(splits[0],Groupbean.class);
					 Log.e("Dudu_SDK","onGroupMessageChanged______GC__");
					onGroupMessageChanged(groupbean);
					break;
				case FM:
					DuduUser fromUser= (DuduUser) parser.parseObject(splits[splits.length-2],DuduUser.class);
					String fmMsg=splits[splits.length-1];
					 Log.e("Dudu_SDK","onReceiveFMMessage______FM__");
					onReceiveFMMessage(fromUser,fmMsg);
					break;
				case XY:
				case XZ:
					 Log.e("Dudu_SDK","onReceiveFMMessage______XY_XZ__");
					DuduPosition pos = (DuduPosition) parser
					.parseObject(splits[0], DuduPosition.class);
					onPositionChanged(pos);
					break;
					
			default:
				 Log.e("Dudu_SDK","DefaultHanlder_______default__"
						+ message.getHeader());
				 onReceiveDefaultMessage(message);
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

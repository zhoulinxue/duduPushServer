package com.czl.chatClient;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.JsonString;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.bean.Trafficebean;
import com.czl.chatClient.login.onConnetCallBack;
import com.czl.chatClient.receiver.DefaultHanlder;
import com.czl.chatClient.receiver.GroupHandler;
import com.czl.chatClient.receiver.P2PHandler;
import com.czl.chatClient.sender.SendMessageLisenter;
import com.czl.chatClient.sender.UserServer;
import com.czl.chatClient.utils.Log;

import io.netty.channel.Channel;

public class DuduClient implements SendMessageLisenter {
	private static DuduClient self;
	private static UserServer sender;
	private static DuduSDK sdk;
	private onConnetCallBack connectionCallBack;

	public static DuduClient getInstance() {
		if (self == null) {
			self = new DuduClient();
			sdk = DuduSDK.getInstance();
			sender = ServerFacoty.getInstance().getUserserver();
		}
		return self;
	}

	/**
	 * 登陆
	 *
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public void login(DuduUser user, onConnetCallBack callBack) {
		login(user, this, callBack);
	}

	/**
	 * 登陆
	 *
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public void login(DuduUser user, SendMessageLisenter lisenter, onConnetCallBack callBack) {
		this.connectionCallBack = callBack;
		if (sdk == null) {
			throwUnInit();
			return;
		}
		if(user==null){
			throwUnUnKnownUser();
			return;
		}
		sdk.setUser(user);
		if(!sdk.isConnected()){
			sdk.connect(callBack);
		}else{
			registerUser(user, getChannel(), lisenter);
		}
	}

	private static void throwUnInit() {
		// TODO Auto-generated method stub
		throw new IllegalArgumentException("call DuduSDK.init(DuduUser user) first");
	}

	private static void throwUnUnKnownUser() {
		// TODO Auto-generated method stub
		throw new IllegalArgumentException("User can not be null");
	}

	/**
	 *
	 * @param channel
	 * @param lisenter
	 *            发送消息 进程 监听
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected NettyMessage registerUser(DuduUser user, Channel channel, SendMessageLisenter lisenter) {
		// TODO Auto-generated method stub
		try {
			JsonString stringcontent = sdk.getPaser().ObjectToJsonString(user);
			stringcontent.getBuilder().append(Constants.SEPORATE).append(user.getDiviceid());
			return sender.registerUser(stringcontent, channel, lisenter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 进入频道
	 *
	 * @param info
	 *            频道信息
	 * @return 返回进频道 消息
	 * @throws UnsupportedEncodingException
	 *             编码异常
	 */
	public NettyMessage enterChannel(Groupbean info) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return enterChannel(info, this);
	}

	/**
	 *
	 * @param info
	 * @param lisenter
	 *            发送消息 进程 监听
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public NettyMessage enterChannel(Groupbean info, SendMessageLisenter lisenter) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return sender.enterChannel(getChannel(), getCurrentUser(), info, sdk.getPaser(), this);
	}

	/**
	 * 呼叫用户
	 *
	 * @param toUser
	 *            被呼叫者
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public NettyMessage callUser(DuduUser toUser) {
		// TODO Auto-generated method stub
		return callUser(toUser, this);
	}

	/**
	 *
	 * @param toUser
	 * @param lisenter
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public NettyMessage callUser(final DuduUser toUser, final SendMessageLisenter lisenter) {
		// TODO Auto-generated method stub
		try {
			NettyMessage msMessage=sender.callUser(getChannel(), sdk.getUser(), toUser, sdk.getPaser(), new SendMessageLisenter() {
				
				@Override
				public void sendSusccess(NettyMessage message) {
					// TODO Auto-generated method stub
					sdk.setCallingUser(toUser);
					sdk.setBusy(true);
					lisenter.sendSusccess(message);
				}
				
				@Override
				public void sendStart(NettyMessage message) {
					// TODO Auto-generated method stub
					lisenter.sendStart(message);
				}
				
				@Override
				public void sendFailed(NettyMessage message, Throwable cause) {
					// TODO Auto-generated method stub
					lisenter.sendFailed(message,cause);
				}
				
				@Override
				public void oncancellSend(NettyMessage message) {
					// TODO Auto-generated method stub
					lisenter.oncancellSend(message);
					
				}
			});
			return msMessage;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 注册监听 一对一对讲
	 *
	 * @param callBack
	 */
	public void addLisenter(P2PHandler callBack) {
		// TODO Auto-generated method stub
		sdk.addLisenter(P2PHandler.class, callBack);
	}

	/**
	 * 注册离线消息监听
	 *
	 * @param callBack
	 */
	public void addLisenter(DefaultHanlder callBack) {
		// TODO Auto-generated method stub
		sdk.addLisenter(DefaultHanlder.class, callBack);
	}

	/**
	 * 注册频道消息消息监听
	 *
	 * @param callBack
	 */
	public void addLisenter(GroupHandler callBack) {
		// TODO Auto-generated method stub
		sdk.addLisenter(GroupHandler.class, callBack);
	}

	/**
	 * 获取 链接对象
	 *
	 * @return 链接对象
	 */
	public Channel getChannel() {
		// TODO Auto-generated method stub
		return sdk.getChannel();
	}

	@Override
	public void sendSusccess(NettyMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void oncancellSend(NettyMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendFailed(NettyMessage message, Throwable cause) {
		Log.e("", "cause" + cause.getMessage());
		if ("消息发送失败 请链接后重试".equals(cause.getMessage())) {
			sdk.setSendMessage(message);
			reconnect();
		}
	}

	@Override
	public void sendStart(NettyMessage message) {
		// TODO Auto-generated method stub

	}

	public void connect() {
		// TODO Auto-generated method stub
		connect(null);
	}

	public void connect(onConnetCallBack callBack) {
		// TODO Auto-generated method stub
		if (sdk != null)
			sdk.connect(callBack);
	}

	/**
	 * 按id 获取 频道活跃状态
	 *
	 * @param ids
	 */

	public void getActiveChannels(List<String> ids) {
		// TODO Auto-generated method stub
		try {
			sender.getActivieChannel(sdk.getChannel(), ids, sdk.getPaser(), this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getPositions() {
		getPositions(this);
	}

	/**
	 * 获取 好友或者频道内人员的位置
	 *
	 * @param lisenter
	 */
	public void getPositions(SendMessageLisenter lisenter) {
		// TODO Auto-generated method stub
		try {
			sender.getPositon(getChannel(), getCurrentUser(), sdk.getPaser(), this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DuduUser getCurrentUser() {
		// TODO Auto-generated method stub
		return sdk.getUser();
	}

	/**
	 * 拒绝来电
	 *
	 * @param user
	 */
	public void refuseCall(DuduUser user) {
		refuseCall(user, this);
	}

	/**
	 * 拒绝来电
	 *
	 * @param user
	 * @param lisenter
	 */
	public void refuseCall(DuduUser user, SendMessageLisenter lisenter) {
		sdk.changeChattingModel(false, false);
		try {
			sender.refuseCall(getChannel(), getCurrentUser(), user, sdk.getPaser(), lisenter);
			sdk.setGroupbean(null);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void agreeCall(DuduUser user) {
		agreeCall(user, this);
	}

	/**
	 * 接受邀请
	 *
	 * @param user
	 * @param lisenter
	 */
	private void agreeCall(DuduUser user, SendMessageLisenter lisenter) {
		sdk.setFriendUser(user);
		sdk.changeChattingModel(true, false);
		try {
			sender.agreeCall(getChannel(), getCurrentUser(), user, sdk.getPaser(), lisenter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void haungUpCall() {
		haungUpCall(this);
	}

	private void haungUpCall(SendMessageLisenter lisenter) {
		sdk.changeChattingModel(false, false);
		try {
			sender.huangUp(getChannel(), getCurrentUser(), getCurrentFriend(), sdk.getPaser(), lisenter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void existChannel() {
		existChannel(this);
	}

	/**
	 * 退出频道
	 *
	 * @param lisenter
	 */
	private void existChannel(SendMessageLisenter lisenter) {
		sdk.changeChattingModel(false, false);
		try {
			sender.existChannel(getChannel(), getCurrentUser(), sdk.getPaser(), lisenter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void sendAudoiByte(byte[] bytes) {
		sendAudoiByte(bytes, this);
	}

	/**
	 * 发送语音数据
	 *
	 * @param bytes
	 * @param lisenter
	 */
	public void sendAudoiByte(byte[] bytes, SendMessageLisenter lisenter) {
		AppServerType type = AppServerType.SM;
		if (sdk.isGroupChatting()) {
			type = AppServerType.SG;
		}
		try {
			sender.sendAudoiByte(type, getChannel(), bytes, lisenter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void sendET(String time, String id) {
		sendET(time, id, this);
	}

	/**
	 *
	 * @param time
	 * @param id
	 * @param lisenter
	 */
	public void sendET(String time, String id, SendMessageLisenter lisenter) {
		try {
			sender.sendET(getChannel(), time, id, getCurrentUser(), sdk.getPaser(), lisenter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		if(sdk==null){
			return false;
		}
		return sdk.isConnected();
	}

	public boolean isGroupChatting() {
		return sdk.isGroupChatting();
	}

	public void sendtime(String time, String stringMessageId) {
		if (!isGroupChatting()) {
			sendET(time, stringMessageId);
		} else {
			sendGT(time, stringMessageId);
		}
	}

	public void sendGT(String time, String id) {
		sendGT(time, id, this);
	}

	/**
	 *
	 * @param time
	 * @param id
	 * @param lisenter
	 */
	public void sendGT(String time, String id, SendMessageLisenter lisenter) {
		try {
			sender.sendGT(getChannel(), time, id, getCurrentUser(), sdk.getPaser(), lisenter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void sendPosiontion(DuduPosition position) {
		sendPosiontion(position, this);
	}

	public void sendPosiontion(DuduPosition position, SendMessageLisenter lisenter) {
		DuduUser user = getCurrentUser();
		if (user == null) {
			return;
		}
		position.setUrl(user.getUrl());
		position.setUserid(user.getUserid());
		position.setUsername(user.getUsername());
		try {
			if (isGroupChatting()) {
				sender.onSendXZPosition(getChannel(), position, sdk.getPaser(), lisenter);
			} else {
				sender.onSendXYPosition(getChannel(), position, sdk.getPaser(), lisenter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void userIsOnLine(DuduUser duduUser) {
		userIsOnLine(duduUser, this);
	}

	/**
	 * 查询用户是否在线
	 *
	 * @param duduUser
	 * @param lisenter
	 */
	public void userIsOnLine(DuduUser duduUser, SendMessageLisenter lisenter) {
		try {
			sender.checkUserIsOnLine(getChannel(), getCurrentUser(), duduUser, sdk.getPaser(), lisenter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelRequst(DuduUser duduUser) {
		  cancelRequst(duduUser, this);
	}

	/**
	 * 取消邀请
	 *
	 * @param duduUser
	 * @param lisenter
	 */
	public void cancelRequst(DuduUser duduUser, SendMessageLisenter lisenter) {
		if(duduUser==null){
			duduUser=sdk.getCallingUser();
		}
		if(duduUser==null){
			return ;
		}
		try {
			sender.cancelRequst(getChannel(), getCurrentUser(), duduUser, sdk.getPaser(), lisenter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(duduUser!=null&&duduUser.getUserid().equals(sdk.getCallingUser())){
			sdk.setCallingUser(null);
		}
	}

	public void existLogin() {
		existLogin(this);
	}

	public void existLogin(SendMessageLisenter lisenter) {
		try {
			sender.loginOutNs(getChannel(), getCurrentUser(), sdk.getPaser(), lisenter);
			sdk.setUser(null);
			closeConnect(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getPreViewTraffic(Trafficebean bean) {
		getPreViewTraffic(bean, this);
	}

	/**
	 * 获取 交通路况图
	 *
	 * @param bean
	 * @param lisenter
	 */
	public void getPreViewTraffic(Trafficebean bean, SendMessageLisenter lisenter) {
		try {
			sender.getPreViewTraffic(getChannel(), sdk.getPaser(), bean, lisenter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isChatting() {
		return sdk.isChatting();
	}

	public void reconnect() {
		if(sdk!=null&&!sdk.isConnected()) {
			if (connectionCallBack != null) {
				login(getCurrentUser(), connectionCallBack);
			}
		}
	}

	public void closeConnect(boolean isreconnect) {
		if (sdk != null) {
			sdk.close(isreconnect);
		}
	}

	public void sendFM(DuduUser user, String s) {
		sendFM(user, s, this);
	}

	public void sendFM(DuduUser user, String s, SendMessageLisenter lisenter) {
		try {
			sender.sendFreedomMessage(getChannel(), s, user, sdk.getPaser(), lisenter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public Groupbean getCurrentGroup() {
		return sdk.getGroupbean();
	}

	public DuduUser getCurrentFriend() {
		return sdk.getFriendUser();
	}
	public boolean isBusy(){
		return sdk.isBusy();
	}
	
	public void closeConnectWithReconnect() {
		closeConnect(true);
	}
	
	public DuduUser getCallingUser(){
		return sdk.getCallingUser();
	}
}

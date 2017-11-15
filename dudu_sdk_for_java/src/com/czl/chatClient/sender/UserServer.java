package com.czl.chatClient.sender;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.JsonString;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.bean.Trafficebean;

import io.netty.channel.Channel;

public interface UserServer {
	/**
	 * 处理部分特殊命令
	 * 
	 * @param mChannel
	 *            链接对象
	 * @param message
	 *            需要处理的消息
	 * @param parser
	 *            json 解析器
	 * @throws Exception
	 *             各种异常
	 */
	public void CommadFilter(Channel mChannel, NettyMessage message, JsonParser parser) throws Exception;

	/**
	 * 用户登陆
	 * 
	 * @param table
	 *            用戶對象
	 * @param mChannel
	 *            鏈接對象
	 * @return 返回登陆 消息
	 * @throws UnsupportedEncodingException
	 *             编码异常
	 */
	public NettyMessage registerUser(JsonString table, Channel mChannel, SendMessageLisenter lisenter)
			throws UnsupportedEncodingException;

	/**
	 * 进入频道
	 * 
	 * @param user
	 *            谁进频道
	 * @param info
	 *            频道信息
	 * @param parser
	 *            json 解析器
	 * @return 返回进频道 消息
	 * @throws UnsupportedEncodingException
	 *             编码异常
	 */
	public NettyMessage enterChannel(Channel channel, DuduUser user, Groupbean info, JsonParser parser,
			SendMessageLisenter lisenter) throws UnsupportedEncodingException;

	/**
	 * 
	 * @param channel
	 * @param myinfo
	 * @param parser
	 * @param lisenter
	 * @throws UnsupportedEncodingException
	 */
	public void existChannel(Channel channel, DuduUser myinfo, JsonParser parser, SendMessageLisenter lisenter)
			throws UnsupportedEncodingException;

	/**
	 * 呼叫用户
	 * 
	 * @param channel
	 * @param myUser
	 * @param toUser
	 * @param parser
	 * @param lisenter
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public NettyMessage callUser(Channel channel, DuduUser myUser, DuduUser toUser, JsonParser parser,
			SendMessageLisenter lisenter) throws UnsupportedEncodingException;

	/**
	 * 拒绝呼叫
	 * 
	 * @param channel
	 * @param myUser
	 * @param remoteUser
	 * @param parser
	 * @param lisenter
	 * @throws UnsupportedEncodingException
	 */
	public void refuseCall(Channel channel, DuduUser myUser, DuduUser remoteUser, JsonParser parser,
			SendMessageLisenter lisenter) throws UnsupportedEncodingException;

	/**
	 * 同意呼叫
	 * 
	 * @param channel
	 * @param myUser
	 * @param remoteUser
	 * @param parser
	 * @param lisenter
	 * @throws UnsupportedEncodingException
	 */
	public void agreeCall(Channel channel, DuduUser myUser, DuduUser remoteUser, JsonParser parser,
			SendMessageLisenter lisenter) throws UnsupportedEncodingException;

	/**
	 * 挂断 对话
	 * 
	 * @param channel
	 * @param myUser
	 * @param remoteUser
	 * @param parser
	 * @param lisenter
	 * @throws UnsupportedEncodingException
	 */
	public void huangUp(Channel channel, DuduUser myUser, DuduUser remoteUser, JsonParser parser,
			SendMessageLisenter lisenter) throws UnsupportedEncodingException;

	/**
	 * 
	 * @param channel
	 * @param myUser
	 * @param remoteUser
	 * @param parser
	 * @param lisenter
	 * @throws UnsupportedEncodingException
	 */
	public void sendAudoiByte(AppServerType type,Channel channel, byte[] bytes, SendMessageLisenter lisenter) throws UnsupportedEncodingException;

	/**
	 * 自定义消息
	 * 
	 * @param channel
	 * @param jsonObject
	 * @param parser
	 * @param lisenter
	 * @throws UnsupportedEncodingException
	 */
	public void sendFreedomMessage(Channel channel, String jsonObject, DuduUser touser, JsonParser parser,
			SendMessageLisenter lisenter) throws UnsupportedEncodingException;

	/**
	 * 发语音结束
	 * 
	 * @param channel
	 * @param parser
	 * @param lisenter
	 * @throws UnsupportedEncodingException
	 */
	public void sendET(Channel channel, String time,String id, DuduUser myUser, JsonParser parser,
			SendMessageLisenter lisenter) throws UnsupportedEncodingException;

	/**
	 * 发 群语音结束
	 * 
	 * @param channel
	 * @param touser
	 * @param parser
	 * @param lisenter
	 * @throws UnsupportedEncodingException
	 */
	public void sendGT(Channel channel, String time,String id, DuduUser touser, JsonParser parser,
			SendMessageLisenter lisenter) throws UnsupportedEncodingException;

	/**
	 * 個人位置
	 * 
	 * @param channel
	 * @param point
	 * @param parser
	 * @param lisenter
	 * @throws Exception
	 */
	public void onSendXYPosition(Channel channel, DuduPosition point, JsonParser parser, SendMessageLisenter lisenter)
			throws Exception;

	/**
	 * 群位置
	 * 
	 * @param channel
	 * @param point
	 * @param parser
	 * @param lisenter
	 * @throws Exception
	 */
	public void onSendXZPosition(Channel channel, DuduPosition point, JsonParser parser, SendMessageLisenter lisenter)
			throws Exception;

	/**
	 * 
	 * @param channel
	 * @param user
	 * @param frienduser
	 * @param parser
	 * @param lisenter
	 * @throws Exception
	 */
	public void checkUserIsOnLine(Channel channel, DuduUser user, DuduUser frienduser, JsonParser parser,
			SendMessageLisenter lisenter) throws Exception;

	/**
	 * 
	 * @param channel
	 * @param user
	 * @param frienduser
	 * @param parser
	 * @param lisenter
	 * @throws Exception
	 */
	public void cancelRequst(Channel channel, DuduUser user, DuduUser frienduser, JsonParser parser,
			SendMessageLisenter lisenter) throws Exception;

	/**
	 * 
	 * @param channel
	 * @param user
	 * @param parser
	 * @param lisenter
	 * @throws Exception
	 */
	public void loginOutNs(Channel channel, DuduUser user, JsonParser parser, SendMessageLisenter lisenter)
			throws Exception;

	/**
	 * 
	 * @param channel
	 * @param DuduUser
	 * @param parser
	 * @param lisenter
	 * @throws Exception
	 */
	public void getPositon(Channel channel, DuduUser DuduUser, JsonParser parser, SendMessageLisenter lisenter)
			throws Exception;

	/**
	 * 获取活跃的频道
	 * 
	 * @param channel
	 * @param records
	 * @param parser
	 * @param lisenter
	 * @throws Exception
	 */
	public void getActivieChannel(Channel channel, List<String> records, JsonParser parser,
			SendMessageLisenter lisenter) throws Exception;



	public void getPreViewTraffic(Channel channel, JsonParser paser, Trafficebean bean, SendMessageLisenter lisenter)throws Exception;
}

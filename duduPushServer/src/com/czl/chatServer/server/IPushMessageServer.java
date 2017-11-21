package com.czl.chatServer.server;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.bean.BasePushMessage;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public interface IPushMessageServer {
    /**
     * 
      * 功能简述：
      * 功能详细描述： 推送消息
      * @author zhouxue
      * @param ctx
      * @param msg
      * @throws UnsupportedEncodingException [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
	public void pushImMessage(Channel ctx,NettyMessage msg)throws UnsupportedEncodingException;
	/**
	 * 
	  * 功能简述：
	  * 功能详细描述：推送消息失败
	  * @author zhouxue
	  * @param ctx
	  * @param pmsg
	  * @throws UnsupportedEncodingException [参数说明]
	  * @return void [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public void sendIMFailed(Channel ctx, BasePushMessage pmsg)throws UnsupportedEncodingException;
	/**
	 * 
	  * 功能简述：
	  * 功能详细描述：根据连接 推送RS消息
	  * @author zhouxue
	  * @param ctx
	  * @param msg [参数说明]
	  * @return void [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public void pushRSMessage(Channel ctx, NettyMessage msg);
	/**
	 * 
	  * 功能简述：
	  * 功能详细描述：根据 uid 推送RS消息
	  * @author zhouxue
	  * @param uid
	  * @param data [参数说明]
	  * @return void [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public void pushRSMessage(String uid, String[] data) ;
	/**
	 * 
	  * 功能简述：
	  * 功能详细描述：推送消息列表
	  * @author zhouxue
	  * @param ctx
	  * @param myuser [参数说明]
	  * @return void [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public void pushImMessages(Channel ctx, DuduUser myuser);
	/**
	 * 
	  * 功能简述：
	  * 功能详细描述： 推送消息 推送成功
	  * @author zhouxue
	  * @param ctx
	  * @param msg [参数说明]
	  * @return void [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
    public void pushCompelte(ChannelHandlerContext ctx, NettyMessage msg);
}

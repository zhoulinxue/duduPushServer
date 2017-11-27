package com.czl.chatServer.server;

import com.czl.chatClient.bean.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述： 好友对讲 业务 接口
 * @author zhouxue
 * @version 1.0 2017年11月21日
 * Copyright: Copyright (c) zhouxue org.,Ltd. 2017
 * Company:zhouxue org
 */
public interface IFriendChatLifeCycle
{
    /**
     * 
      * 功能简述：
      * 功能详细描述： 邀请好友
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void invitesFriend(ChannelHandlerContext ctx, NettyMessage msg);
    /**
     * 
      * 功能简述：
      * 功能详细描述：挂断 一对一 对讲
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void endFriendChat(ChannelHandlerContext ctx, NettyMessage msg);
    /**
     * 根据挂断者ID挂断 一对一对讲
      * 功能简述：
      * 功能详细描述：
      * @author zhouxue
      * @param userid [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void endFriendChat(String userid);
    /**
     * 
      * 功能简述：
      * 功能详细描述：接受对话
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void agreeCall(ChannelHandlerContext ctx, NettyMessage msg);
    /**
     * 
      * 功能简述：
      * 功能详细描述： 拒绝 对话邀请
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void reJectCall(ChannelHandlerContext ctx, NettyMessage msg);
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：取消对话请求
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void cancelCall(ChannelHandlerContext ctx, NettyMessage msg);
   
    
    
    
}

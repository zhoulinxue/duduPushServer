package com.czl.chatServer.server;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.bean.NettyMessage;

import io.netty.channel.ChannelHandlerContext;

public interface INettyServer
{
    /**
     * 
      * 功能简述：
      * 功能详细描述： 查询用户是否在线
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void userIsOnLine(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException;
    /**
     * 
      * 功能简述：
      * 功能详细描述： 语音流
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void chatbyteEnd(ChannelHandlerContext ctx, NettyMessage msg);
    /**
     * 
      * 功能简述：
      * 功能详细描述：查询频道是否活跃
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void isChannelActive(ChannelHandlerContext ctx, NettyMessage msg);
    /**
     * 
      * 功能简述：
      * 功能详细描述：自定义消息
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void pushFMMsg(ChannelHandlerContext ctx, NettyMessage msg);
    /**
     * 
      * 功能简述：
      * 功能详细描述：频道 信息变化
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void groupChages(ChannelHandlerContext ctx, NettyMessage msg);
    
    
    
}

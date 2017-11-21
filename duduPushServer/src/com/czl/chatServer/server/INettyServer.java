package com.czl.chatServer.server;

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
    public void userIsOnLine(ChannelHandlerContext ctx, NettyMessage msg);
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
    public void chatbyte(ChannelHandlerContext ctx, NettyMessage msg);
    
    
    
}

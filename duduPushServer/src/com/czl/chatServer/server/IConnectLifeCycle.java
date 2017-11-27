package com.czl.chatServer.server;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.bean.NettyMessage;

import io.netty.channel.ChannelHandlerContext;

public interface IConnectLifeCycle
{
   /**
    * 
     * 功能简述：
     * 功能详细描述： app 连接断开
     * @author zhouxue
     * @param ctx [参数说明]
     * @return void [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
    */
    public void appOffline(ChannelHandlerContext ctx);
    /**
     * 
      * 功能简述：
      * 功能详细描述：用户登录
      * @author zhouxue
      * @param ctx
      * @param msg
      * @throws UnsupportedEncodingException [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void  appLogin(ChannelHandlerContext ctx,NettyMessage msg)throws UnsupportedEncodingException;
    /**
     * 
      * 功能简述：
      * 功能详细描述：有 app 连接
      * @author zhouxue
      * @param ctx [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void  appConnect(ChannelHandlerContext ctx);
    /**
     * 
      * 功能简述：
      * 功能详细描述： 退出登录
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void  loginOut(ChannelHandlerContext ctx,NettyMessage msg);
    /**
     * 
      * 功能简述：
      * 功能详细描述： token 失效
      * @author zhouxue
      * @param ctx [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void loginTimeOut(ChannelHandlerContext ctx);
    /**
     * 
      * 功能简述：
      * 功能详细描述：
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void testConnect(ChannelHandlerContext ctx, NettyMessage msg);
    
    
}

package com.czl.chatServer.server;

import com.czl.chatClient.bean.NettyMessage;

import io.netty.channel.ChannelHandlerContext;

public interface IConnectLifeCycle
{
    //用户掉线
    public void appOffline(ChannelHandlerContext ctx);
    //用户上线
    public void  appLogin(ChannelHandlerContext ctx,NettyMessage msg);
    //用户连接成功
    public void  appConnect(ChannelHandlerContext ctx);
    //退出登录
    public void  loginOut(ChannelHandlerContext ctx,NettyMessage msg);
    
    
}

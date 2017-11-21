package com.czl.chatServer.server.Impl.handlerImpl;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.server.IConnectLifeCycle;

import io.netty.channel.ChannelHandlerContext;

public class AppConnectServerImpl implements IConnectLifeCycle
{

    @Override
    public void appOffline(ChannelHandlerContext ctx)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void appLogin(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void appConnect(ChannelHandlerContext ctx)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void loginOut(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
}

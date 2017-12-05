package com.czl.chatServer.server.Impl;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.server.INettyServer;

import io.netty.channel.ChannelHandlerContext;

public class NettyServerImpl implements INettyServer
{

    @Override
    public void userIsOnLine(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
        
    }

    @Override
    public void chatbyteEnd(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void isChannelActive(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushFMMsg(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void groupChages(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    
}

package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.server.INettyServer;
import com.czl.chatServer.utils.RedisManager;

import io.netty.channel.ChannelHandlerContext;

public class NettyServerImpl extends BaseMessageServiceImpl
        implements INettyServer
{
    
    @Override
    public void userIsOnLine(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        
        sendIsOnLine(ctx.channel(), RedisManager.IsOnline(data[2]), data[2]);
        
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

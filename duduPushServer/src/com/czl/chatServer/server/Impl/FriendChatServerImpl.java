package com.czl.chatServer.server.Impl;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.server.IFriendChatLifeCycle;

import io.netty.channel.ChannelHandlerContext;

public class FriendChatServerImpl  implements IFriendChatLifeCycle
{

    @Override
    public void invitesFriend(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endFriendChat(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void agreeCall(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void reJectCall(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void cancelCall(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
}

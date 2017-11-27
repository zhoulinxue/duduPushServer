package com.czl.chatServer.server.Impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.ChatType;
import com.czl.chatServer.server.IChatModelServer;

import io.netty.channel.ChannelHandlerContext;

public class FriendChatModel implements IChatModelServer
{
    private Map<String, IChatModelServer> friendChatModels = new ConcurrentHashMap<>();
    @Override
    public void chatByte(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public IChatModelServer creatChat(ChannelHandlerContext ctx, NettyMessage msg,
            ChatType type)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void newUserIn(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void userQuit(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void finishGroup(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void finishFriendTalk(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void locationChange(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Map<String, IChatModelServer> getModels(ChatType type)
    {
        // TODO Auto-generated method stub
        return friendChatModels;
    }

    @Override
    public void chatbyteEnd(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void userQuit(String userid)
    {
        // TODO Auto-generated method stub
        
    }
    
}

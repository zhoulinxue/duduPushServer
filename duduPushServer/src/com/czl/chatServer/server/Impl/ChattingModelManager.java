package com.czl.chatServer.server.Impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.ChatType;
import com.czl.chatServer.server.IChatModelServer;

import io.netty.channel.ChannelHandlerContext;

public class ChattingModelManager implements IChatModelServer
{
    private IChatModelServer friendServer = new FriendChatModel();
    
    private IChatModelServer groupModelServer = new GroupChatModel();
    
    private Map<String, IChatModelServer> groupChatModels = new ConcurrentHashMap<>();
    
    private Map<String, IChatModelServer> friendChatModels = new ConcurrentHashMap<>();
    
    @Override
    public void chatByte(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        switch (msg.getAppServerType())
        {
            case SM:
                friendServer.chatByte(ctx, msg);
                break;
            case SG:
                groupModelServer.chatByte(ctx, msg);
                break;
            
            default:
                break;
        }
    }
    
    @Override
    public IChatModelServer creatChat(ChannelHandlerContext ctx,
            NettyMessage msg, ChatType type)
    {
        // TODO Auto-generated method stub
        IChatModelServer server = null;
        switch (type)
        {
            case GROUP:
                String groupId = "";
                server = groupModelServer.creatChat(ctx, msg, type);
                groupChatModels.put(groupId, server);
                break;
            case FRIEND:
                String uid = "";
                server = friendServer.creatChat(ctx, msg, type);
                friendChatModels.put(uid, server);
                break;
            default:
                break;
        }
        
        return server;
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
    
}

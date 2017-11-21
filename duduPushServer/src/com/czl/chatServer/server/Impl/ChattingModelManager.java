package com.czl.chatServer.server.Impl;

import java.util.Map;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.ChatType;
import com.czl.chatServer.server.IChatModelServer;

import io.netty.channel.ChannelHandlerContext;

public class ChattingModelManager implements IChatModelServer
{
    public static ChattingModelManager instance;
    
    public static ChattingModelManager getInstance()
    {
        if (instance == null)
        {
            instance = new ChattingModelManager();
        }
        return instance;
    }
    
    private IChatModelServer friendServer = new FriendChatModel();
    
    private IChatModelServer groupModelServer = new GroupChatModel();
    
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
                server = groupModelServer.getModels(ChatType.GROUP)
                        .get(groupId);
                break;
            case FRIEND:
                String uid = "";
                String fuid = "";
                server = friendServer.getModels(ChatType.FRIEND).get(uid);
                
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
    
    @Override
    public Map<String, IChatModelServer> getModels(ChatType type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void chatbyteEnd(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    
}

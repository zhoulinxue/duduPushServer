package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.bean.BasePushMessage;
import com.czl.chatServer.server.IPushMessageServer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class PushMessageImpl implements IPushMessageServer
{
    
    @Override
    public void pushImMessage(Channel ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendIMFailed(Channel ctx, BasePushMessage pmsg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushRSMessage(Channel ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushRSMessage(String uid, String[] data)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushImMessages(Channel ctx, DuduUser myuser)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushCompelte(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
}

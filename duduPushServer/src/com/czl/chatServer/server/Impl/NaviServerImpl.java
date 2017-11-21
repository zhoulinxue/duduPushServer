package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.server.INaviServer;

import io.netty.channel.Channel;

public class NaviServerImpl implements INaviServer
{
    private static INaviServer naviServer;
    
    public static INaviServer getInstance()
    {
        if (naviServer == null)
        {
            naviServer = new NaviServerImpl();
        }
        return naviServer;
    }
    
    @Override
    public void getUserPersion(Channel ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void getTrafficPic(Channel ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void putFriendPersion(Channel ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void putGroupPersion(Channel ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public DuduPosition getUserPersion(String uid)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}

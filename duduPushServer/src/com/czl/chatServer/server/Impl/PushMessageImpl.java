package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;
import com.czl.chatServer.Constants;
import com.czl.chatServer.bean.BasePushMessage;
import com.czl.chatServer.server.IPushMessageServer;
import com.czl.chatServer.utils.RedisManager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class PushMessageImpl extends BaseMessageServiceImpl
        implements IPushMessageServer
{
    private static PushMessageImpl instance;
    
    public static PushMessageImpl getInstance()
    {
        if (instance == null)
        {
            instance = new PushMessageImpl();
        }
        return instance;
    }
    
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
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        String toUserId = data[data.length - 1];
        String fromuid = getUserIdFromChannel(ctx);
        DuduPosition friendUser = RedisManager.IsOnline(toUserId);
        NettyMessage rsmsg = null;
        Log.e(getCurrentIp(ctx)+"IPU"+JSONObject.toJSONString(friendUser));
        if (friendUser != null && getCurrentIp(ctx).equals(friendUser.getIp())
                && friendUser.getPort() == getCurrentPort(ctx))
        {
            Channel channel = RedisManager
                    .getChannelByUid(friendUser.getUserid());
            if (channel != null)
            {
                rsmsg = buildMessage(AppServerType.RS,
                        data[data.length - 2] + Constants.SEPORATE + fromuid);
                sendMessage(rsmsg, channel);
            }
        }
        else if (friendUser != null)
        {
            rsmsg = buildMessage(AppServerType.RS,
                    data[data.length - 2] + Constants.SEPORATE + toUserId
                            + Constants.SEPORATE + fromuid);
            sendtoOtherNsData(friendUser.getIp(), rsmsg);
        }
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
    public void pushMsgCompelte(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void pushMsgsCompelte(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
}

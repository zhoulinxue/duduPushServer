package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
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
    public void isChannelActive(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data=msg.getUserDataFromMsg();
      List<String>  channelIds = JSONObject.parseArray(data[1], String.class);
      List<String> respones = new ArrayList<>();
      for (String channelId : channelIds) {
          boolean isActive = RedisManager.isChannelActive(channelId);
          if (isActive) {
              respones.add(channelId);
          }
      }
      NettyMessage message = buildMessage(AppServerType.CA, JSONObject.toJSONString(respones));
      sendMessage(message, ctx.channel());
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

package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.Constants;
import com.czl.chatServer.server.IFriendChatLifeCycle;
import com.czl.chatServer.utils.DataBaseManager;
import com.czl.chatServer.utils.RedisManager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class FriendChatServerImpl extends BaseMessageServiceImpl
        implements IFriendChatLifeCycle
{
    
    @Override
    public void invitesFriend(ChannelHandlerContext ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        DuduUser user = RedisManager.IsOnline(data[2]);
        if (user != null)
        {
            Channel channel = RedisManager.getChannelByUid(data[2]);
            if (channel != null)
            {
                // 本服务器
                NettyMessage message = buildMessage(AppServerType.FN, data[1]);
                msg.setMessageId(msg.getMessageId());
                sendMessage(message, channel);
            }
            else
            {
                // 不在本服务器
                NettyMessage arg0 = buildMessage(AppServerType.TF,
                        data[1] + "|" + data[2]);// TF
                arg0.setMessageId(msg.getMessageId());
                sendtoOtherNsData(user.getIp(), arg0);
            }
        }
    }
    
    @Override
    public void endFriendChat(ChannelHandlerContext ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        String myuid = getUserIdFromChannel(ctx);
        String friendId = RedisManager.getChatwithFriend(myuid);
        if(StringUtils.isEmpty(friendId)){
            return;
        }
        Channel nbcapp = RedisManager.getChannelByUid(friendId);
        NettyMessage mesg = null;
        if (nbcapp != null)
        {
            mesg = buildMessage(AppServerType.ED, data[1]);
            RedisManager.deleteCalling(myuid, friendId);
            sendMessage(mesg, nbcapp);
        }
//        else
//        {
//            DuduUser frUser = RedisManager.IsOnline(friendId);
//            if (frUser != null)
//            {
//                mesg = buildMessage(AppServerType.ED, data[1]);
//                sendtoOtherNsData(frUser.getIp(), mesg);
//            }
//            
//        }
        
    }
    
    @Override
    public void agreeCall(ChannelHandlerContext ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        Channel channel = RedisManager.getChannelByUid(data[2]);
        DuduUser mydata = DataBaseManager
                .getUserFromDb(getUserIdFromChannel(ctx));
        if (channel != null)
        {
            NettyMessage famsg = buildMessage(AppServerType.FA,
                    JSONObject.toJSONString(mydata) + "|" + data[2]);
            sendMessage(famsg, channel);
        }
        else
        {
            // 查询好友在不在线,以及所在服务器
            DuduUser user = RedisManager.IsOnline(data[2]);
            if (user == null)
            {
                //对方掉线
                return;
            }
            String[] ipAndPort = getCurrentIpAndPort(ctx.channel());
            NettyMessage nsmsg = buildMessage(AppServerType.NA,
                    JSONObject.toJSONString(mydata) + "|" + data[2] + "|"
                            + ipAndPort[0] + "|" + ipAndPort[1]);
            sendtoOtherNsData(user.getIp(), nsmsg);
            
        }
    }
    
    @Override
    public void reJectCall(ChannelHandlerContext ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        // 查询好友在不在线,以及所在服务器
        DuduUser user = RedisManager.IsOnline(data[2]);
        
        RedisManager.deleteCalling(getUserIdFromChannel(ctx), data[2]);
        
        Channel chanel = RedisManager.getChannelByUid(data[2]);
        if (chanel != null)
        {
            // 本服务器
            NettyMessage frmsg = buildMessage(AppServerType.FR);
            frmsg.setContent(getContentByte(data[1] + "|" + data[2]));
            sendMessage(frmsg, chanel);
        }
        else
        {
            // 其他服务器
            NettyMessage arg0 = buildMessage(AppServerType.NR);// NR
            arg0.setContent(getContentByte(data[1] + "|" + data[2]));
            sendtoOtherNsData(user.getIp(), arg0);
        }
    }
    
    @Override
    public void cancelCall(ChannelHandlerContext ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        DuduUser user = RedisManager.IsOnline(data[2]);
        Channel nbcapp = RedisManager.getChannelByUid(data[2]);
        msg.setContent(msg.getCtxUTF8String2());
        msg.setFromUerId(("\n").getBytes(Constants.CONTENT_CHAR_SET));
        if (nbcapp != null)
        {
            nbcapp.writeAndFlush(msg);
            return;
        }
        else
        {
            if (user != null)
            {
                sendtoOtherNsData(user.getIp(), msg);
            }
        }
    }
    
    @Override
    public void endFriendChat(String userid)
    {
        // TODO Auto-generated method stub
        
    }
    
}

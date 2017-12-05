package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.ChatType;
import com.czl.chatServer.Constants;
import com.czl.chatServer.UserStatus;
import com.czl.chatServer.bean.Imbean;
import com.czl.chatServer.server.IChatModelServer;
import com.czl.chatServer.utils.DataBaseManager;
import com.czl.chatServer.utils.RedisManager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class FriendChatModel extends BaseMessageServiceImpl
        implements IChatModelServer
{
    private UserStatus status = UserStatus.SLIENCE;
    
    private DuduUser callingUser;
    
    private DuduUser myUser;
    
    private List<DuduUser> userList = new ArrayList<>();
    
    @Override
    public void chatByte(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean creatChat(ChannelHandlerContext ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        switch (msg.getAppServerType())
        {
            case FS:
                String[] data = getUserDataFromMsg(msg);
                if (data == null || data.length != 3)
                {
                    return false;
                }
                status = UserStatus.CALLING_USER;
                myUser = DataBaseManager
                        .getUserFromDb(getUserIdFromChannel(ctx));
                myUser.setIp(getCurrentIp(ctx.channel()));
                myUser.setPort(getCurrentPort(ctx.channel()));
                // 查询好友在不在线,以及所在服务器
                callingUser = RedisManager.IsOnline(data[2]);
                if (callingUser != null)
                {
                    userList.add(myUser);
                    userList.add(callingUser);
                    RedisManager.userCalling(myUser.getUserid(),
                            callingUser.getUserid());
                }
                else
                {
                    NettyMessage message = buildMessage(AppServerType.FO,
                            data[2]);
                    sendMessage(message, ctx.channel());
                    UserOffline(myUser, data[2]);
                    return false;
                }
                break;
            
            default:
                break;
        }
        return true;
    }
    
    /**
     * 生成 离线推送消息
     * 
     * @param myData
     *            我的基本 信息
     * @param friendId
     *            好友的id
     */
    
    private void UserOffline(DuduUser myData, String friendId)
    {
        // TODO Auto-generated method stub
        Imbean im = new Imbean();
        im.setChannelid("");
        im.setDetail("");
        im.setFromid(myData.getUserid());
        im.setToid(friendId);
        im.setType(Constants.IM_FRIEND_TYPE);
        im.setFromlogourl(myData.getUrl());
        im.setFromname(myData.getUsername());
        im.setDataid(UUID.randomUUID() + "");
        im.setTitle(myData.getUsername());
        im.setAlert(myData.getUsername() + " 邀请您  " + "对讲");
        DataBaseManager.insertOffLinMessage(im);
    }
    
    @Override
    public IChatModelServer newUserIn(ChannelHandlerContext ctx,
            NettyMessage msg)
    {
        // TODO Auto-generated method stub
        return this;
    }
    
    @Override
    public void userQuit(ChannelHandlerContext ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        String myuid = getUserIdFromChannel(ctx);
        String friendId = RedisManager.getChatwithFriend(myuid);
        Channel nbcapp = RedisManager.getChannelByUid(friendId);
        NettyMessage mesg = null;
        if (nbcapp != null)
        {
            mesg = buildMessage(AppServerType.ED, data[1]);
            RedisManager.deleteCalling(myuid, friendId);
            sendMessage(mesg, nbcapp);
        }
        else
        {
            DuduUser frUser = RedisManager.IsOnline(friendId);
            if (frUser != null)
            {
                mesg = buildMessage(AppServerType.ED, data[1]);
                sendtoOtherNsData(frUser.getIp(), mesg);
            }
            
        }
    }
    
    @Override
    public void finishGroup(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        status = UserStatus.SLIENCE;
        String[] data = getUserDataFromMsg(msg);
        switch (msg.getAppServerType())
        {
            case FR: 
            case FE:  
            case ED:
                RedisManager.deleteCalling(data[2], getUserIdFromChannel(ctx));
                break;
            default:
                break;
        }
    }
    
    @Override
    public void locationChange(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data=msg.getUserDataFromMsg();
        DuduPosition position=JSONObject.parseObject(data[1], DuduPosition.class);
        Channel nbcapp =RedisManager.getChannelByUid(RedisManager.getChatwithFriend(position.getUserid()));
        if (nbcapp != null) {
            NettyMessage xymsg = buildMessage(AppServerType.XY, data[1]);
            sendMessage(xymsg, nbcapp);
        }
        
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
    
    @Override
    public UserStatus getUserStatus()
    {
        // TODO Auto-generated method stub
        return status;
    }
    
    @Override
    public void userBusy(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public ChatType getChatType()
    {
        // TODO Auto-generated method stub
        return ChatType.FRIEND;
    }
    
    @Override
    public boolean userIsBusy(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        IChatModelServer server = ChattingModelManager.getInstance()
                .getModels(ChatType.FRIEND)
                .get(data[2]);
        if (server != null && server.getUserStatus() == UserStatus.CALLING_USER)
        {
            return true;
        }
        return false;
    }
    
    public UserStatus getStatus()
    {
        return status;
    }
    
    public void setStatus(UserStatus status)
    {
        this.status = status;
    }
    
    public DuduUser getCallingUser()
    {
        return callingUser;
    }
    
    public void setCallingUser(DuduUser callingUser)
    {
        this.callingUser = callingUser;
    }
    
    @Override
    public List<DuduPosition> getUsers()
    {
        // TODO Auto-generated method stub
        
        return null;
    }
    
    @Override
    public void statusChanged(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        switch (msg.getAppServerType())
        {
            case FA:
                status = UserStatus.ON_LINE_P2P_CHATTING;
                RedisManager.startChattingWithFriend(getUserIdFromChannel(ctx),
                        data[2]);
                break;
            
            default:
                break;
        }
    }
    
    @Override
    public String getServerId()
    {
        // TODO Auto-generated method stub
        return callingUser.getUserid();
    }
    
}

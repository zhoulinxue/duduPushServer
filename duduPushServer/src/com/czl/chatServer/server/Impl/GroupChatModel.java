package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.ChatType;
import com.czl.chatServer.Constants;
import com.czl.chatServer.UserStatus;
import com.czl.chatServer.bean.ChannelMember;
import com.czl.chatServer.bean.Imbean;
import com.czl.chatServer.netty.ServerException;
import com.czl.chatServer.server.IChatModelServer;
import com.czl.chatServer.utils.DataBaseManager;
import com.czl.chatServer.utils.RedisManager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class GroupChatModel extends BaseMessageServiceImpl
        implements IChatModelServer
{
    private List<DuduUser> userList = new ArrayList<>();
    
    @Override
    public void chatByte(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean creatChat(ChannelHandlerContext ctx,
            NettyMessage msg) throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        Groupbean group = JSONObject.parseObject(data[2], Groupbean.class);
        RedisManager.putGroupIp(group.getGroupId(),
                ctx.channel().localAddress().toString().substring(1));
        DuduUser newUsr = getUserFormMsg(ctx, data);
        userList.add(newUsr);
        List<ChannelMember> list = DataBaseManager
                .getMyChannelMember(group.getGroupId(), newUsr.getUserid());
        if (!list.contains(new ChannelMember(newUsr.getUserid()))
                && !Constants.CHANNEL_ID.equals(group.getGroupId()))
        {
            NettyMessage outChannelmsg = buildMessage(AppServerType.EX_TYPE,
                    ServerException.NOT_IN_CHANNEL.toInfo());
            sendMessage(outChannelmsg, ctx.channel());
            return false;
        }
        String userJson=JSONObject.toJSONString(newUsr);
        String groupJson=JSONObject.toJSONString(DataBaseManager.getChannelMsgFromDb(group.getGroupId()));
        for(ChannelMember menber:list){
            if (!newUsr.getUserid().equals(menber.getMemberid())) {
                DuduUser u = RedisManager.IsOnline(menber.getMemberid());
                if(u==null){
                    Imbean im = new Imbean();
                    im.setChannelid(group.getGroupId());
                    im.setDetail(list.size() + "");
                    im.setFromid(newUsr.getUserid());
                    im.setToid(menber.getMemberid());
                    im.setType(Constants.IM_CHANELL_TYPE);
                    im.setFromlogourl(group.getLogourl());
                    im.setFromname(newUsr.getUsername());
                    im.setDataid(UUID.randomUUID() + "");
                    im.setTitle(group.getGroupName());
                    im.setAlert(newUsr.getUsername() + "  邀请你去频道  " + "对讲");
                    DataBaseManager.insertOffLinMessage(im);
                }else {                 
                    Channel channel=RedisManager.getChannelByUid(u.getUserid());
                    if(channel!=null){
                        NettyMessage gmMsg = buildMessage(AppServerType.GM);
                        gmMsg.setContent(
                                getContentByte(userJson + seporate() +groupJson ));
                        sendMessage(gmMsg, channel);
                    }else{
                     // 交由NS服务器转发
                        NettyMessage arg0 = buildMessage(AppServerType.INVITE_USER_ON_OTHER_NS);
                        arg0.setContent(getContentByte(userJson + seporate() + groupJson
                                + seporate() + u.getUserid() + seporate() + getCurrentIp(ctx.channel()) + seporate() + getCurrentPort(ctx.channel())));
                         sendtoOtherNsData(u.getIp(), arg0);
                    }
                }
            }
        }
        
        return true;
    }
    
    private DuduUser getUserFormMsg(ChannelHandlerContext ctx, String[] data)
    {
        // TODO Auto-generated method stub
        DuduUser newUsr = JSONObject.parseObject(data[1], DuduUser.class);
        newUsr.setIp(getCurrentIp(ctx.channel()));
        newUsr.setPort(getCurrentPort(ctx.channel()));
        return newUsr;
    }
    
    @Override
    public IChatModelServer newUserIn(ChannelHandlerContext ctx,
            NettyMessage msg)
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        DuduUser newUsr = getUserFormMsg(ctx, data);
        userList.add(newUsr);
        return this;
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
        return null;
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
        return null;
    }
    
    @Override
    public boolean userIsBusy(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
        return false;
    }

    @Override
    public List<DuduUser> getUsers()
    {
        // TODO Auto-generated method stub
        return userList;
    }
    
}

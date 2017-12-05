package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.ChannalActiveusers;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;
import com.czl.chatClient.utils.StringUtils;
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
    private List<DuduPosition> userList = new ArrayList<>();
    
    private String groupId;
    
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
        String[] data = getUserDataFromMsg(msg);
        Groupbean group = JSONObject.parseObject(data[2], Groupbean.class);
        groupId = group.getGroupId();
        RedisManager.putGroupIp(group.getGroupId(),
                ctx.channel().localAddress().toString().substring(1));
        DuduPosition newUsr = getUserFormMsg(ctx, data);
        
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
        userList.add(newUsr);
        String userJson = JSONObject.toJSONString(newUsr);
        String groupJson = JSONObject.toJSONString(
                DataBaseManager.getChannelMsgFromDb(group.getGroupId()));
       
        for (ChannelMember menber : list)
        {
            Log.e("频道内成员......"+menber.getMemberid());
            if (!getUserIdFromChannel(ctx).equals(menber.getMemberid()))
            {
                DuduUser u = RedisManager.IsOnline(menber.getMemberid());
                Log.e("是否在线......"+(u==null));
                if (u == null)
                {
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
                }
                else
                {
                   
                    Channel channel = RedisManager
                            .getChannelByUid(u.getUserid());
                    if (channel != null)
                    {
                        NettyMessage gmMsg = buildMessage(AppServerType.GM);
                        gmMsg.setContent(getContentByte(
                                userJson + seporate() + groupJson));
                        sendMessage(gmMsg, channel);
                    }
                    else
                    {
                        // 交由NS服务器转发
                        NettyMessage arg0 = buildMessage(
                                AppServerType.INVITE_USER_ON_OTHER_NS);
                        arg0.setContent(getContentByte(userJson + seporate()
                                + groupJson + seporate() + u.getUserid()
                                + seporate() + getCurrentIp(ctx.channel())
                                + seporate() + getCurrentPort(ctx.channel())));
                        sendtoOtherNsData(u.getIp(), arg0);
                    }
                }
            }
        }
        responeAl(group, ctx);
        return true;
    }
    
    /**
     * 回复 AL
     * 
     * @param groupbean
     *            频道对象
     * @param ctx
     *            链接对象
     * @param onLineUser
     *            要回复的内容
     * @throws UnsupportedEncodingException
     */
    
    protected void responeAl(Groupbean groupbean, ChannelHandlerContext ctx)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        RedisManager.startChatInGroup(getUserIdFromChannel(ctx), groupId);
        RedisManager.deleteCalling(getUserIdFromChannel(ctx), null);
        ChannalActiveusers activeusers = new ChannalActiveusers();
        activeusers.setActiveUsers(userList);
        activeusers.setChannelId(groupbean.getGroupId());
        activeusers.setChannelNum(
                DataBaseManager.getChannelNumFromDb(groupbean.getGroupId()));
        activeusers.setChannelName(groupbean.getGroupName());
        NettyMessage almsg = buildMessage(AppServerType.AL);
        almsg.setContent(getContentByte(JSONObject.toJSONString(activeusers)));
        sendMessage(almsg, ctx.channel());
        
    }
    
    private DuduPosition getUserFormMsg(ChannelHandlerContext ctx,
            String[] data)
    {
        // TODO Auto-generated method stub
        DuduPosition newUsr = JSONObject.parseObject(data[1], DuduPosition.class);
        newUsr.setIp(getCurrentIp(ctx.channel()));
        newUsr.setPort(getCurrentPort(ctx.channel()));
        return newUsr;
    }
    
    @Override
    public IChatModelServer newUserIn(ChannelHandlerContext ctx,
            NettyMessage msg)throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        DuduPosition newUsr = getUserFormMsg(ctx, data);       
        Groupbean groupbean = JSONObject.parseObject(data[2], Groupbean.class);
        // 只提示新 进入频道人员
        if (!userList.contains(newUsr)) {
            userList.add(newUsr);
        }
        for (DuduPosition u : userList) {
            System.out.println("需要转发NT:" + u.getUserid());
            if (!newUsr.getUserid().equals(u.getUserid()) && !StringUtils.isEmpty(groupbean.getGroupId())) {
                // 取得APP的连接,发送到APP 如果用户为新加入用户则 发送NT
                Channel nbcapp = RedisManager.getChannelByUid(u.getUserid());
                if (nbcapp != null) {// NT
                    System.out.println("有链接 转发NT:" + u.getUserid());
                    if (StringUtils.isEmpty(RedisManager.getChatInGroup(newUsr.getUserid()))) {                     
                        System.out.println("NT:" + u.getUserid());
                        NettyMessage ntmsg = buildMessage(AppServerType.NT);
                        ntmsg.setContent(getContentByte(ObjectToString(newUsr.clean(),
                                new SimplePropertyPreFilter("url", "username", "userid", "x", "y"))
                                        .append(seporate()).append(ObjectToString(groupbean)).toString()));
                        sendMessage(ntmsg, nbcapp);
                    } else {
                        sendIsOnLine(nbcapp, newUsr, newUsr.getUserid());
                    }
                }
            }
        }
        
        responeAl(groupbean, ctx);
        return this;
    }
    
    @Override
    public void userQuit(ChannelHandlerContext ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        String uid = getUserIdFromChannel(ctx);
        String groupid = RedisManager.getChatInGroup(uid);
        if (StringUtils.isEmpty(groupid))
        {
            return;
        }
        userList.remove(new DuduPosition(uid));
        Log.e("频道对讲"+userList.size());
        if (userList.size() != 0)
        {
            for (int i = userList.size() - 1; i > -1; i--)
            {
                DuduPosition user = userList.get(i);
                // 取得APP的连接,发送到APP
                Channel nbcapp = RedisManager.getChannelByUid(user.getUserid());
                if (!uid.equals(user.getUserid()))
                {
                    if (nbcapp != null)
                    {
                        NettyMessage egmsg = buildMessage(AppServerType.EG,
                                data[1]);
                        sendMessage(egmsg, nbcapp);
                    }
                }
            }
        }
        else
        {
            // 如果频道不存在了 就删除该频道缓存
            List<ChannelMember> list = DataBaseManager
                    .getMyChannelMember(groupid, uid);
            for (ChannelMember member : list)
            {
                DuduUser u = RedisManager.IsOnline(member.getMemberid());
                if (u != null)
                {
                    if (getCurrentIp(ctx.channel()).equals(u.getIp()))
                    {// 在同一服务器
                         // 取得APP的连接,发送到APP
                        Channel nbcapp = RedisManager
                                .getChannelByUid(u.getUserid());
                        if (nbcapp != null)
                        {// DM
                            NettyMessage dmMsg = buildMessage(AppServerType.DM,
                                    groupid);
                            sendMessage(dmMsg, nbcapp);
                        }
                    }
                    else
                    {
                        // 交由NS服务器转发
                        NettyMessage arg0 = buildMessage(AppServerType.DM);
                        arg0.setContent(getContentByte(
                                ObjectToString(u).append(seporate())
                                        .append(groupid)
                                        .toString()));
                        sendtoOtherNsData(u.getIp(), arg0);
                    }
                }
            }
        }
        RedisManager.deletegroupChatMsg(uid);
    }
    
    @Override
    public void finishGroup(ChannelHandlerContext ctx, NettyMessage msg)
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
    public List<DuduPosition> getUsers()
    {
        // TODO Auto-generated method stub
        return userList;
    }
    
    @Override
    public void statusChanged(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public String getServerId()
    {
        // TODO Auto-generated method stub
        if (!StringUtils.isEmpty(groupId))
            return groupId;
        else
        {
            return "";
        }
    }
    
}

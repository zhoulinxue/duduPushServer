package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.ChatType;
import com.czl.chatServer.Constants;
import com.czl.chatServer.UserStatus;
import com.czl.chatServer.server.IChatModelServer;
import com.czl.chatServer.server.IFriendChatLifeCycle;
import com.czl.chatServer.utils.DataBaseManager;
import com.czl.chatServer.utils.RedisManager;

import io.netty.channel.ChannelHandlerContext;

public class ChattingModelManager extends BaseMessageServiceImpl
        implements IChatModelServer
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
    
    //一对一 对讲业务
    private IFriendChatLifeCycle friendChatServer = new FriendChatServerImpl();
    
    private IChatModelServer friendModelServer = new FriendChatModel();
    
    private IChatModelServer groupModelServer = new GroupChatModel();
    
    private Map<String, IChatModelServer> friendChatModels = new ConcurrentHashMap<>();
    
    private Map<String, IChatModelServer> groupChatModels = new ConcurrentHashMap<>();
    
    @Override
    public void chatByte(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        IChatModelServer ser = null;
        switch (msg.getAppServerType())
        {
            case SM:
                ser = friendChatModels.get(getUserIdFromChannel(ctx));
                ser.chatByte(ctx, msg);
                break;
            case SG:
                ser = groupChatModels.get(getUserIdFromChannel(ctx));
                ser.chatByte(ctx, msg);
                break;
            
            default:
                break;
        }
    }
    
    @Override
    public boolean creatChat(ChannelHandlerContext ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        AppServerType type = msg.getAppServerType();
        // TODO Auto-generated method stub
        IChatModelServer server = null;
        String[] gsdata = getUserDataFromMsg(msg);
        boolean isbusy = userIsBusy(ctx, msg);
        switch (type)
        {
            case GS:
                if (!isbusy)
                {
                    
                    Groupbean groupbean = com.alibaba.fastjson.JSONObject
                            .parseObject(gsdata[2], Groupbean.class);
                    server = groupChatModels.get(groupbean.getGroupId());
                    if (server == null)
                    {
                        server = new GroupChatModel();
                        server.creatChat(ctx, msg);
                    }
                    else
                    {
                        server.newUserIn(ctx, msg);
                    }
                    
                    groupChatModels.put(server.getServerId(), server);
                }
                break;
            case FS:
                if (isbusy)
                {
                    NettyMessage frmsg = buildMessage(AppServerType.FR);
                    frmsg.setContent(
                            getContentByte(gsdata[1] + "|" + gsdata[2]));
                    sendMessage(frmsg, ctx.channel());
                }
                else
                {
                    server = new FriendChatModel();
                    boolean isSucess = server.creatChat(ctx, msg);
                    if (isSucess)
                    {
                        friendChatModels.put(getUserIdFromChannel(ctx), server);
                        friendChatServer.invitesFriend(ctx, msg);
                    }
                }
                break;
            default:
                break;
        }
        
        return true;
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
        IChatModelServer ser = null;
        String uid = getUserIdFromChannel(ctx);
        switch (msg.getAppServerType())
        {
            case FR:
                ser = friendChatModels.get(data[2]);
                if (ser != null)
                {
                    ser.finishGroup(ctx, msg);
                    friendChatModels.remove(data[2]);
                }
                break;
            case FE:
                ser = friendChatModels.get(getUserIdFromChannel(ctx));
                if (ser != null)
                {
                    ser.finishGroup(ctx, msg);
                    friendChatModels.remove(data[2]);
                }
                
                break;
            case ED:
                ser = friendChatModels.get(getUserIdFromChannel(ctx));
                if (ser != null)
                {
                    ser.finishGroup(ctx, msg);
                    friendChatModels.remove(data[2]);
                }
                
                break;
            case OU:
                String friendId = RedisManager.getChatwithFriend(uid);
                String groupIp = RedisManager.getChatInGroup(uid);
                if (!StringUtils.isEmpty(friendId))
                {
                    friendChatModels.get(uid).userQuit(uid);
                }
                else if (StringUtils.isEmpty(groupIp))
                {
                    groupChatModels.get(uid).userQuit(uid);
                }
                break;
            case EG:
                String groupId = RedisManager.getChatInGroup(uid);
                Log.e(groupId + "对讲中的频道" + uid);
                if (!StringUtils.isEmpty(groupId))
                {
                    ser = groupChatModels.get(groupId);
                    Log.e(JSONObject.toJSONString(ser));
                    if (ser != null)
                    {
                        ser.userQuit(ctx, msg);
                        List<DuduPosition> dudulist = ser.getUsers();
                        if (dudulist != null && dudulist.size() == 0)
                        {
                            groupChatModels.remove(ser.getServerId());
                        }
                    }
                }
                break;
            
            default:
                break;
        }
    }
    
    @Override
    public void finishGroup(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void locationChange(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        IChatModelServer server=null;
        String[] data = getUserDataFromMsg(msg);
        String uid=getUserIdFromChannel(ctx);
        DuduPosition position=JSONObject.parseObject(data[1], DuduPosition.class);
        DataBaseManager.writePosition(position);
        switch (msg.getAppServerType())
        {
            case XY:
                server=friendChatModels.get(uid);
                if(server!=null){
                    server.locationChange(ctx, msg);
                }else{
                    Log.e("server==null"+uid);
                }          
                break;
            case XZ:
                
                break;
            
            default:
                break;
        }
        
    }
    
    public Map<String, IChatModelServer> getModels(ChatType type)
    {
        // TODO Auto-generated method stub
        switch (type)
        {
            case FRIEND:
                
                return friendChatModels;
            
            case GROUP:
                return groupChatModels;
        }
        return null;
        
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
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] gsdata = getUserDataFromMsg(msg);
        switch (msg.getAppServerType())
        {
            case FS:
                
                return !StringUtils
                        .isEmpty(RedisManager.getCallingMsg(gsdata[2]));
            
            case GS:
                Groupbean groupbean = com.alibaba.fastjson.JSONObject
                        .parseObject(gsdata[2], Groupbean.class);
                String ipAndPort = RedisManager
                        .getGroupIp(groupbean.getGroupId());
                if (StringUtils.isEmpty(ipAndPort))
                {
                    return false;
                }
                else
                {
                    String[] ip = ipAndPort.split(Constants.IP_PORT_SEPORATE);
                    if (!ipAndPort.equals(ctx.channel()
                            .localAddress()
                            .toString()
                            .substring(1)))
                    {
                        NettyMessage gbMeg = buildMessage(AppServerType.GB);
                        gbMeg.setContent(getContentByte(
                                ObjectToString(gsdata[1]).append(seporate())
                                        .append(gsdata[2])
                                        .append(seporate())
                                        .append(ip[0])
                                        .append(seporate())
                                        .append(ip[1])
                                        .toString()));
                        sendMessage(gbMeg, ctx.channel());
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                    
                }
                
            default:
                break;
        }
        return false;
    }
    
    @Override
    public List<DuduPosition> getUsers()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void statusChanged(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        String[] data = getUserDataFromMsg(msg);
        IChatModelServer server = null;
        switch (msg.getAppServerType())
        {
            case FA:
                server = friendChatModels.get(data[2]);
                if (server != null)
                {
                    server.statusChanged(ctx, msg);
                    friendChatModels.put(getUserIdFromChannel(ctx), server);
                }
                break;
            
            default:
                break;
        }
    }
    
    @Override
    public String getServerId()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}

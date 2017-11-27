package com.czl.chatServer.server.Impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.ChatType;
import com.czl.chatServer.UserStatus;
import com.czl.chatServer.server.IChatModelServer;
import com.czl.chatServer.server.IFriendChatLifeCycle;
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
        boolean isbusy = userIsBusy(ctx, msg);
        String[] data = getUserDataFromMsg(msg);
        switch (type)
        {
            case GS:
                if (isbusy)
                {
                    Groupbean groupbean = com.alibaba.fastjson.JSONObject
                            .parseObject(data[2], Groupbean.class);
                    server = groupChatModels.get(groupbean.getGroupId());
                    server.newUserIn(ctx, msg);
                }
                else
                {
                    server = new GroupChatModel();
                    server.creatChat(ctx, msg);
                }               
                groupChatModels.put(getUserIdFromChannel(ctx), server);
                break;
            case FS:
                if (isbusy)
                {
                    friendChatServer.reJectCall(ctx, msg);
                }
                else
                {
                    server = new FriendChatModel();
                   boolean isSucess= server.creatChat(ctx, msg);
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
        String[] data = getUserDataFromMsg(msg);
        IChatModelServer ser = null;
        switch (msg.getAppServerType())
        {
            case FR:
                ser = friendChatModels.get(data[2]);
                List<DuduUser> list = ser.getUsers();
                if (list.contains(new DuduUser(getUserIdFromChannel(ctx))))
                {
                    friendChatModels.remove(data[2]);
                    RedisManager.deleteCalling(getUserIdFromChannel(ctx),
                            data[2]);
                }
                break;
            case FE:
                ser = friendChatModels.remove(getUserIdFromChannel(ctx));
                RedisManager.deleteCalling(getUserIdFromChannel(ctx), data[2]);
                break;
            
            default:
                break;
        }
        
    }
    
    @Override
    public void locationChange(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        
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
    {
        // TODO Auto-generated method stub
        String[] gsdata = getUserDataFromMsg(msg);
        switch (msg.getAppServerType())
        {
            case FS:
                boolean isbusy = friendModelServer.userIsBusy(ctx, msg);
                if (!isbusy)
                {
                    String fuid = RedisManager.getCallingMsg(gsdata[2]);
                    if (!StringUtils.isEmpty(fuid))
                    {
                        return true;
                    }
                }
                else
                {
                    return true;
                }
                break;
            case GS:
                
                Groupbean groupbean = com.alibaba.fastjson.JSONObject
                        .parseObject(gsdata[2], Groupbean.class);
                if (groupChatModels.get(groupbean.getGroupId()) == null)
                {
                    String ipAndPort = RedisManager
                            .getGroupIp(groupbean.getGroupId());
                    if (StringUtils.isEmpty(ipAndPort))
                    {
                        return false;
                    }
                }
                else
                {
                    return true;
                }
                break;
            
            default:
                break;
        }
        return false;
    }
    
    @Override
    public List<DuduUser> getUsers()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}

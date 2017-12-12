package com.czl.chatClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.JsonString;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.bean.Trafficebean;
import com.czl.chatClient.login.onConnetCallBack;
import com.czl.chatClient.receiver.DefaultHanlder;
import com.czl.chatClient.receiver.GroupHandler;
import com.czl.chatClient.receiver.P2PHandler;
import com.czl.chatClient.receiver.ZVAHandler;
import com.czl.chatClient.sender.SendMessageLisenter;
import com.czl.chatClient.sender.UserServer;
import com.czl.chatClient.utils.Log;

import io.netty.channel.Channel;

public class DuduClient implements SendMessageLisenter
{
    private static DuduClient self;
    
    private static UserServer sender;
    
    //	private static DuduDuduSDK.getInstance() DuduSDK.getInstance();
    private onConnetCallBack connectionCallBack;
    
    public static DuduClient getInstance()
    {
        if (self == null)
        {
            self = new DuduClient();
            sender = ServerFacoty.getInstance().getUserserver();
        }
        return self;
    }
    
    /**
     * 登陆
     *
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    public void login(DuduUser user, onConnetCallBack callBack)
    {
        login(user, this, callBack);
    }
    
    /**
     * 登陆
     *
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    public void login(DuduUser user, SendMessageLisenter lisenter,
            onConnetCallBack callBack)
    {
        this.connectionCallBack = callBack;
        this.connectionCallBack.addLisenter(this);
        if (DuduSDK.getInstance() == null)
        {
            throwUnInit();
            return;
        }
        if (user == null)
        {
            throwUnUnKnownUser();
            return;
        }
        DuduSDK.getInstance().setUser(user);
        if (!DuduSDK.getInstance().isConnected())
        {
            DuduSDK.getInstance().connect(callBack);
        }
        else
        {
            registerUser(user, getChannel(), lisenter);
        }
    }
    
    private static void throwUnInit()
    {
        // TODO Auto-generated method stub
        throw new IllegalArgumentException(
                "call DuduDuduSDK.getInstance().init(DuduUser user) first");
    }
    
    private static void throwUnUnKnownUser()
    {
        // TODO Auto-generated method stub
        throw new IllegalArgumentException("User can not be null");
    }
    
    /**
     *
     * @param channel
     * @param lisenter
     *            发送消息 进程 监听
     * @return
     * @throws UnsupportedEncodingException
     */
    protected NettyMessage registerUser(DuduUser user, Channel channel,
            SendMessageLisenter lisenter)
    {
        // TODO Auto-generated method stub
        try
        {
            JsonString stringcontent = DuduSDK.getInstance()
                    .getParser()
                    .ObjectToJsonString(user);
            stringcontent.getBuilder()
                    .append(Constants.SEPORATE)
                    .append(user.getDiviceid());
            return sender.registerUser(stringcontent, channel, lisenter);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 进入频道
     *
     * @param info
     *            频道信息
     * @return 返回进频道 消息
     * @throws UnsupportedEncodingException
     *             编码异常
     */
    public NettyMessage enterChannel(Groupbean info)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        return enterChannel(info, this);
    }
    
    /**
     *
     * @param info
     * @param lisenter
     *            发送消息 进程 监听
     * @return
     * @throws UnsupportedEncodingException
     */
    public NettyMessage enterChannel(Groupbean info,
            SendMessageLisenter lisenter) throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        return sender.enterChannel(getChannel(),
                getCurrentUser(),
                info,
                DuduSDK.getInstance().getParser(),
                this);
    }
    
    /**
     * 呼叫用户
     *
     * @param toUser
     *            被呼叫者
     * @return
     * @throws UnsupportedEncodingException
     */
    public NettyMessage callUser(DuduUser toUser)
    {
        // TODO Auto-generated method stub
        return callUser(toUser, this);
    }
    
    /**
     *
     * @param toUser
     * @param lisenter
     * @return
     * @throws UnsupportedEncodingException
     */
    public NettyMessage callUser(final DuduUser toUser,
            final SendMessageLisenter lisenter)
    {
        // TODO Auto-generated method stub
        try
        {
            NettyMessage msMessage = sender.callUser(getChannel(),
                    getCurrentUser(),
                    toUser,
                    DuduSDK.getInstance().getParser(),
                    new SendMessageLisenter()
                    {
                        
                        @Override
                        public void sendSusccess(NettyMessage message)
                        {
                            // TODO Auto-generated method stub
                            DuduSDK.getInstance().setCallingUser(toUser);
                            DuduSDK.getInstance().setBusy(true);
                            lisenter.sendSusccess(message);
                        }
                        
                        @Override
                        public void sendStart(NettyMessage message)
                        {
                            // TODO Auto-generated method stub
                            lisenter.sendStart(message);
                        }
                        
                        @Override
                        public void sendFailed(NettyMessage message,
                                Throwable cause)
                        {
                            // TODO Auto-generated method stub
                            lisenter.sendFailed(message, cause);
                        }
                        
                        @Override
                        public void oncancellSend(NettyMessage message)
                        {
                            // TODO Auto-generated method stub
                            lisenter.oncancellSend(message);
                            
                        }
                    });
            return msMessage;
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 注册监听 一对一对讲
     *
     * @param callBack
     */
    public void addLisenter(P2PHandler callBack)
    {
        // TODO Auto-generated method stub
        DuduSDK.getInstance().addLisenter(P2PHandler.class, callBack);
    }
    
    /**
     * 注册监听 一对一对讲
     *
     * @param callBack
     */
    public void addLisenter(ZVAHandler callBack)
    {
        // TODO Auto-generated method stub
        DuduSDK.getInstance().addLisenter(ZVAHandler.class, callBack);
    }
    
    /**
     * 注册离线消息监听
     *
     * @param callBack
     */
    public void addLisenter(DefaultHanlder callBack)
    {
        // TODO Auto-generated method stub
        DuduSDK.getInstance().addLisenter(DefaultHanlder.class, callBack);
    }
    
    /**
     * 注册频道消息消息监听
     *
     * @param callBack
     */
    public void addLisenter(GroupHandler callBack)
    {
        // TODO Auto-generated method stub
        DuduSDK.getInstance().addLisenter(GroupHandler.class, callBack);
    }
    
    /**
     * 获取 链接对象
     *
     * @return 链接对象
     */
    public Channel getChannel()
    {
        // TODO Auto-generated method stub
        return DuduSDK.getInstance().getChannel();
    }
    
    @Override
    public void sendSusccess(NettyMessage message)
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void oncancellSend(NettyMessage message)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void sendFailed(NettyMessage message, Throwable cause)
    {
        Log.e("", "cause" + cause.getMessage());
        if ("消息发送失败 请链接后重试".equals(cause.getMessage()))
        {
            DuduSDK.getInstance().setSendMessage(message);
            reconnect();
        }
    }
    
    @Override
    public void sendStart(NettyMessage message)
    {
        // TODO Auto-generated method stub
        
    }
    
    public void connect()
    {
        // TODO Auto-generated method stub
        connect(null);
    }
    
    public void connect(onConnetCallBack callBack)
    {
        // TODO Auto-generated method stub
        if (DuduSDK.getInstance() != null)
            DuduSDK.getInstance().connect(callBack);
    }
    
    /**
     * 按id 获取 频道活跃状态
     *
     * @param ids
     */
    
    public void getActiveChannels(List<String> ids)
    {
        // TODO Auto-generated method stub
        try
        {
            sender.getActivieChannel(DuduSDK.getInstance().getChannel(),
                    ids,
                    DuduSDK.getInstance().getParser(),
                    this);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void getPositions()
    {
        getPositions(this);
    }
    
    /**
     * 获取 好友或者频道内人员的位置
     *
     * @param lisenter
     */
    public void getPositions(SendMessageLisenter lisenter)
    {
        // TODO Auto-generated method stub
        try
        {
            sender.getPositon(getChannel(),
                    getCurrentUser(),
                    DuduSDK.getInstance().getParser(),
                    this);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public DuduUser getCurrentUser()
    {
        // TODO Auto-generated method stub
        return DuduSDK.getInstance().getUser();
    }
    
    /**
     * 拒绝来电
     *
     * @param user
     */
    public void refuseCall(DuduUser user)
    {
        refuseCall(user, this);
    }
    
    /**
     * 拒绝来电
     *
     * @param user
     * @param lisenter
     */
    public void refuseCall(DuduUser user, SendMessageLisenter lisenter)
    {
        DuduSDK.getInstance().changeChattingModel(false, false);
        try
        {
            sender.refuseCall(getChannel(),
                    getCurrentUser(),
                    user,
                    DuduSDK.getInstance().getParser(),
                    lisenter);
            DuduSDK.getInstance().setGroupbean(null);
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void agreeCall(DuduUser user)
    {
        agreeCall(user, this);
    }
    
    /**
     * 接受邀请
     *
     * @param user
     * @param lisenter
     */
    private void agreeCall(DuduUser user, SendMessageLisenter lisenter)
    {
        DuduSDK.getInstance().setFriendUser(user);
        DuduSDK.getInstance().changeChattingModel(true, false);
        try
        {
            sender.agreeCall(getChannel(),
                    getCurrentUser(),
                    user,
                    DuduSDK.getInstance().getParser(),
                    lisenter);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    
    public void haungUpCall()
    {
        haungUpCall(this);
    }
    
    private void haungUpCall(SendMessageLisenter lisenter)
    {
        DuduSDK.getInstance().changeChattingModel(false, false);
        try
        {
            sender.huangUp(getChannel(),
                    getCurrentUser(),
                    getCurrentFriend(),
                    DuduSDK.getInstance().getParser(),
                    lisenter);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    
    public void existChannel()
    {
        existChannel(this);
    }
    
    /**
     * 退出频道
     *
     * @param lisenter
     */
    private void existChannel(SendMessageLisenter lisenter)
    {
        DuduSDK.getInstance().changeChattingModel(false, false);
        try
        {
            sender.existChannel(getChannel(),
                    getCurrentUser(),
                    DuduSDK.getInstance().getParser(),
                    lisenter);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    
    @Deprecated
    public void sendAudoiByte(byte[] bytes)
    {
        sendAudoiByte(bytes, this);
    }
    
    /**
     * 发送语音数据
     *
     * @param bytes
     * @param lisenter
     */
    public void sendAudoiByte(byte[] bytes, SendMessageLisenter lisenter)
    {
        AppServerType type = AppServerType.SM;
        if (DuduSDK.getInstance().isGroupChatting())
        {
            type = AppServerType.SG;
        }
        try
        {
            sender.sendAudoiByte(type, getChannel(), bytes, lisenter);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    
    public void sendET(String time, String id)
    {
        sendET(time, id, this);
    }
    
    /**
     *
     * @param time
     * @param id
     * @param lisenter
     */
    public void sendET(String time, String id, SendMessageLisenter lisenter)
    {
        try
        {
            sender.sendET(getChannel(),
                    time,
                    id,
                    getCurrentUser(),
                    DuduSDK.getInstance().getParser(),
                    lisenter);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * 
      * 功能简述：
      * 功能详细描述： 发送语音消息给某一个用户
      * @author zhouxue
      * @param toUser
      * @param bytes
      * @param content [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void sendAudoiByte(DuduUser toUser, byte[] bytes, String content)
    {
        List<DuduUser> users = new ArrayList<>();
        users.add(toUser);
        sendAudoiByte( users,  bytes,  content);
    }
     /**
      * 
       * 功能简述：
       * 功能详细描述：发送消息  给某一群用户
       * @author zhouxue
       * @param toUsers
       * @param bytes
       * @param content [参数说明]
       * @return void [返回类型说明]
       * @exception throws [异常类型] [异常说明]
       * @see [类、类#方法、类#成员]
      */
    public void sendAudoiByte(List<DuduUser> toUsers, byte[] bytes, String content)
    {
      
        try
        {
            sender.sendAudoiByte(toUsers,
                    AppServerType.AU,
                    getChannel(),
                    bytes,
                    content,
                    this);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean isConnected()
    {
        if (DuduSDK.getInstance() == null)
        {
            return false;
        }
        return DuduSDK.getInstance().isConnected();
    }
    
    public boolean isGroupChatting()
    {
        return DuduSDK.getInstance().isGroupChatting();
    }
    
    public void sendtime(String time, String stringMessageId)
    {
        if (!isGroupChatting())
        {
            sendET(time, stringMessageId);
        }
        else
        {
            sendGT(time, stringMessageId);
        }
    }
    
    public void sendGT(String time, String id)
    {
        sendGT(time, id, this);
    }
    
    /**
     *
     * @param time
     * @param id
     * @param lisenter
     */
    public void sendGT(String time, String id, SendMessageLisenter lisenter)
    {
        try
        {
            sender.sendGT(getChannel(),
                    time,
                    id,
                    getCurrentUser(),
                    DuduSDK.getInstance().getParser(),
                    lisenter);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    
    public void sendPosiontion(DuduPosition position)
    {
        sendPosiontion(position, this);
    }
    
    public void sendPosiontion(DuduPosition position,
            SendMessageLisenter lisenter)
    {
        DuduUser user = getCurrentUser();
        if (user == null)
        {
            return;
        }
        position.setUrl(user.getUrl());
        position.setUserid(user.getUserid());
        position.setUsername(user.getUsername());
        try
        {
            if (isGroupChatting())
            {
                sender.onSendXZPosition(getChannel(),
                        position,
                        DuduSDK.getInstance().getParser(),
                        lisenter);
            }
            else
            {
                sender.onSendXYPosition(getChannel(),
                        position,
                        DuduSDK.getInstance().getParser(),
                        lisenter);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void userIsOnLine(DuduUser duduUser)
    {
        userIsOnLine(duduUser, this);
    }
    
    /**
     * 查询用户是否在线
     *
     * @param duduUser
     * @param lisenter
     */
    public void userIsOnLine(DuduUser duduUser, SendMessageLisenter lisenter)
    {
        try
        {
            sender.checkUserIsOnLine(getChannel(),
                    getCurrentUser(),
                    duduUser,
                    DuduSDK.getInstance().getParser(),
                    lisenter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void cancelRequst(DuduUser duduUser)
    {
        cancelRequst(duduUser, this);
    }
    
    /**
     * 取消邀请
     *
     * @param duduUser
     * @param lisenter
     */
    public void cancelRequst(DuduUser duduUser, SendMessageLisenter lisenter)
    {
        if (duduUser == null)
        {
            duduUser = DuduSDK.getInstance().getCallingUser();
        }
        if (duduUser == null)
        {
            return;
        }
        try
        {
            sender.cancelRequst(getChannel(),
                    getCurrentUser(),
                    duduUser,
                    DuduSDK.getInstance().getParser(),
                    lisenter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("",
                "繁忙状态改变" + (duduUser != null) + "!!" + (duduUser.getUserid()
                        + "!!!!" + DuduSDK.getInstance().getCallingUser()));
        if (duduUser != null && duduUser.getUserid()
                .equals(DuduSDK.getInstance().getCallingUser().getUserid()))
        {
            DuduSDK.getInstance().setCallingUser(null);
        }
    }
    
    public void existLogin()
    {
        existLogin(this);
    }
    
    public void existLogin(SendMessageLisenter lisenter)
    {
        try
        {
            sender.loginOutNs(getChannel(),
                    getCurrentUser(),
                    DuduSDK.getInstance().getParser(),
                    lisenter);
            DuduSDK.getInstance().setUser(null);
            closeConnect(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void getPreViewTraffic(Trafficebean bean)
    {
        getPreViewTraffic(bean, this);
    }
    
    /**
     * 获取 交通路况图
     *
     * @param bean
     * @param lisenter
     */
    public void getPreViewTraffic(Trafficebean bean,
            SendMessageLisenter lisenter)
    {
        try
        {
            sender.getPreViewTraffic(getChannel(),
                    DuduSDK.getInstance().getParser(),
                    bean,
                    lisenter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean isChatting()
    {
        return DuduSDK.getInstance().isChatting();
    }
    
    public void reconnect()
    {
        if (DuduSDK.getInstance() != null
                && !DuduSDK.getInstance().isConnected())
        {
            if (connectionCallBack != null)
            {
                login(getCurrentUser(), connectionCallBack);
            }
        }
    }
    
    public void closeConnect(boolean isreconnect)
    {
        if (DuduSDK.getInstance() != null)
        {
            DuduSDK.getInstance().close(isreconnect);
        }
    }
    
    /**
     *
     * @param user
     * @param s supose to jsonString
     */
    public void sendDuduMessage(DuduUser user, String s)
    {
        sendDuduMessage(user, s, this);
    }

    /**
     *
     * @param user
     * @param s
     * @param saveToCache 是否 保存离线消息  true ：当 {user} 不在线时  保存消息到离线数据库
     */
    public void sendDuduMessage(DuduUser user, String s,boolean saveToCache)
    {
        sendDuduMessage(user, s, this,saveToCache);
    }

    /**
     *
     * @param users 接收者 集合
     * @param s  发送的内存  （不要含有\n及“|”）
     * @param lisenter
     * @param savetoCache 是否 保存离线消息
     */
    public void sendDuduMessage(List<DuduUser> users, String s,
            SendMessageLisenter lisenter,boolean savetoCache)
    {
        try
        {
            sender.sendFreedomMessage(getChannel(),
                    s,
                    users,
                    DuduSDK.getInstance().getParser(),
                    lisenter,savetoCache);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    public void sendDuduMessage(DuduUser user, String s,
                                SendMessageLisenter lisenter)
    {
        List<DuduUser> users = new ArrayList<>();
        users.add(user);
        sendDuduMessage(users, s, lisenter,false);
    }
    /**
     *
     * @param user
     * @param s
     * @param lisenter
     */
    public void sendDuduMessage(DuduUser user, String s,
            SendMessageLisenter lisenter,boolean savetoCache)
    {
        List<DuduUser> users = new ArrayList<>();
        users.add(user);
        sendDuduMessage(users, s, lisenter,savetoCache);
    }
    
    public Groupbean getCurrentGroup()
    {
        return DuduSDK.getInstance().getGroupbean();
    }
    
    public DuduUser getCurrentFriend()
    {
        return DuduSDK.getInstance().getFriendUser();
    }
    
    public boolean isBusy()
    {
        return DuduSDK.getInstance().isBusy();
    }
    
    public void closeConnectWithReconnect()
    {
        closeConnect(true);
    }
    
    public DuduUser getCallingUser()
    {
        return DuduSDK.getInstance().getCallingUser();
    }
}

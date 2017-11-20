package com.czl.chatClient;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.czl.chatClient.bean.ChannalActiveusers;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.JsonString;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.bean.NettyServer;
import com.czl.chatClient.bean.Responbean;
import com.czl.chatClient.handler.NSChannelInitializer;
import com.czl.chatClient.handler.MSChannelInitializer;
import com.czl.chatClient.login.onConnetCallBack;
import com.czl.chatClient.receiver.RecivMessageCallBack;
import com.czl.chatClient.sender.BaseMessageServiceImpl;
import com.czl.chatClient.sender.JsonParser;
import com.czl.chatClient.sender.SendMessageLisenter;
import com.czl.chatClient.utils.Log;
import com.czl.chatClient.utils.StringUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public final class DuduSDK extends BaseMessageServiceImpl
        implements Runnable, onConnetCallBack
{
    // 链接对象
    private Channel mChannel;
    
    // 监听 集合
    private Map<String, RecivMessageCallBack> callBackMap = new ConcurrentHashMap<>();
    
    // 是否是链接状态
    private boolean isConnected = false;
    
    // 当前 用户
    private DuduUser user;
    
    private JsonParser paser;
    
    // 服务器
    private static NettyServer server = null;
    
    private static DuduClient client;
    
    private static DuduSDK sdk;
    
    protected static boolean isDebug = true;
    
    // 跨服务器消息
    private NettyMessage currentMsg;
    
    private Groupbean groupbean;
    
    private DuduUser friendUser;
    
    private boolean isReconnect = true;
    
    private boolean isChatting = false;
    
    private boolean isGroupChatting = false;
    
    private onConnetCallBack connectCallBack;
    
    private boolean isLooping = false;
    
    private int DEFAULT_SLEEP_TIME = 3 * 1000;
    
    private int sleepTime = DEFAULT_SLEEP_TIME;
    
    private NettyMessage sendMessage;
    
    private boolean isBusy = false;
    
    private DuduUser callingUser;
    
    public static void init(DuduUser user)
    {
        init(user, new FastJsonPaser());
    }
    
    public static void init(DuduUser user, JsonParser paser)
    {
        init(user, paser, false);
    }
    
    public static void init(DuduUser user, JsonParser paser, boolean isLog)
    {
        if (user == null)
        {
            throw new IllegalArgumentException("DuduUser  can  not  be  null");
        }
        if (sdk == null)
        {
            sdk = new DuduSDK(user, paser, isLog);
        }
        server = NettyServer.creatDefualt();
        ServerFacoty.getInstance().init();
        client = ServerFacoty.getInstance().getClient();
    }
    
    public DuduSDK(DuduUser user, JsonParser paser, boolean isLog)
    {
        super();
        this.user = user;
        this.paser = paser;
    }
    
    protected static DuduSDK getInstance()
    {
        if (sdk == null)
        {
            throwUnInit();
        }
        return sdk;
    }
    
    private static void throwUnInit()
    {
        // TODO Auto-generated method stub
        throw new IllegalArgumentException(
                "call DuduSDK.init(DuduUser user) first");
    }
    
    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        close(false);
        // String ipandport = "";
        try
        {
            // ipandport = getter.getMsServer(msUrl);
            // if (!StringUtils.isEmpty(ipandport)) {
            // server = getServer(ipandport);
            // }
            if (!isConnected())
                connectMS(server.getIp(), server.getPort());
        }
        catch (Exception e)
        {
            Log.e("Dudu_SDK",
                    "getServer ...failed ...user  default server... ");
        }
    }
    
    protected void close(boolean isreconnect)
    {
        // TODO Auto-generated method stub
        Log.e("Dudu_SDK", "关闭链接");
        if (mChannel != null)
        {
            this.isReconnect = isreconnect;
            mChannel.close();
            mChannel = null;
        }
        isConnected = false;
    }
    
    /**
     * 连接MS 服务器
     * 
     * @param ip
     * @param port
     */
    private void connectMS(String ip, int port)
    {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new MSChannelInitializer(selfcallback, DuduSDK.this,
                        paser));
        // 发起异步连接操作
        try
        {
            ChannelFuture future = b.connect(ip, port)
                    .channel()
                    .closeFuture()
                    .await();
            if (!future.isSuccess())
            {
                sleepTime = 20 * 1000;
                reconnect();
            }
            Log.e("Dudu_SDK", "  MS: " + ip + ":" + port);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            group.shutdownGracefully();
            isLooping = false;
        }
    }
    
    protected void connect(onConnetCallBack callBack)
    {
        // TODO Auto-generated method stub
        if (isLooping)
        {
            return;
        }
        if (callBack != null)
        {
            this.connectCallBack = callBack;
        }
        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                isLooping = true;
                DuduSDK.this.run();
            }
        });
    }
    
    protected void connect(final String ip, final int port)
    {
        // TODO Auto-generated method stub
        executor.execute(new Runnable()
        {
            
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                connectNS(ip, port);
            }
        });
    }
    
    /**
     * 链接业务服务器
     * 
     * @param ip
     * @param port
     */
    
    private void connectNS(String ip, int port)
    {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new NSChannelInitializer(selfcallback, DuduSDK.this,
                        paser));
        try
        {
            Log.e("", "NS=:" + ip + ":" + port);
            ChannelFuture future = b.connect(new InetSocketAddress(ip, port))
                    .await();
            if (future.isSuccess())
            {
                sleepTime = DEFAULT_SLEEP_TIME;
                mChannel = future.channel();
                onConnectSucess(future.channel());
            }
            else
            {
                onConnectFailed();
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            reconnect();
        }
        finally
        {
            isReconnect = true;
        }
    }
    
    protected void reconnect()
    {
        // TODO Auto-generated method stub
        if (isReconnect())
        {
            if (!isLooping)
            {
                isLooping = true;
                executor.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Log.e("Dudu_SDK", sleepTime + "");
                        try
                        {
                            Thread.sleep(sleepTime);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        connectMS(server.getIp(), server.getPort());
                    }
                });
            }
        }
        else
        {
            Log.e("Dudu_SDK", "不自动重连");
        }
    }
    
    protected void changeAccout(DuduUser table, SendMessageLisenter lisenter)
    {
        // TODO Auto-generated method stub
        if (mChannel == null)
        {
            throwUnInit();
            return;
        }
        this.user = table;
        onConnectSucess(mChannel);
    }
    
    protected void changeServer(String ip, int port, onConnetCallBack callBack)
    {
        // TODO Auto-generated method stub
        connect(ip, port);
    }
    
    protected Channel getChannel()
    {
        // TODO Auto-generated method stub
        return mChannel;
    }
    
    protected void sendMessage(Channel channel, final NettyMessage message,
            final SendMessageLisenter listener)
    {
        // TODO Auto-generated method stub
        if (channel != null)
        {
            ChannelFuture future = channel.writeAndFlush(message);
            listener.sendStart(message);
            future.addListener(new ChannelFutureListener()
            {
                
                @Override
                public void operationComplete(ChannelFuture future)
                        throws Exception
                {
                    // TODO Auto-generated method stub
                    if (future.isSuccess())
                    {
                        listener.sendSusccess(message);
                    }
                    else if (future.isCancelled())
                    {
                        listener.oncancellSend(message);
                    }
                    else if (!future.isSuccess())
                    {
                        listener.sendFailed(message, future.cause());
                    }
                }
            });
        }
    }
    
    protected void addLisenter(Class<?> mClass, RecivMessageCallBack callBack)
    {
        // TODO Auto-generated method stub
        callBackMap.put(mClass.getSimpleName(), callBack);
    }
    
    @Override
    public void onConnectSucess(Channel channel)
    {
        isConnected = true;
        isReconnect = true;
        if (client == null)
        {
            throwUnInit();
            return;
        }
        if (user == null)
        {
            throw new IllegalStateException(
                    "did not  login.... call DuduClient.login(DuduUser user)....first");
        }
        client.registerUser(user, mChannel, null);
        if (currentMsg != null)
        {
            String header = currentMsg.getHeader();
            String content = currentMsg.getCtxUTF8String();
            String[] obg = content.split("\\|");
            if (AppServerType.GB.toString().equals(header) && obg.length == 4)
            {
                try
                {
                    Groupbean info = (Groupbean) paser
                            .parseObject(obg[obg.length - 3], Groupbean.class);
                    client.enterChannel(info);
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //			else if (AppServerType.FA.toString().equals(header) && obg.length == 4) {
            //				isReconnect = false;
            //				close();
            //				connect(obg[obg.length - 2], Integer.valueOf(obg[obg.length - 1]));
            //			} 
            else
            {
                dosomething(channel, currentMsg, paser);
            }
            currentMsg = null;
        }
        if (sendMessage != null)
        {
            channel.writeAndFlush(sendMessage);
            sendMessage = null;
        }
        if (connectCallBack != null)
            connectCallBack.onConnectSucess(channel);
    }
    
    @Override
    public void onConnectFailed()
    {
        // TODO Auto-generated method stub
        Log.e("Dudu_SDK",
                "获取NS 业务服务器失败...onConnectFailed ... reconnect()...5 seconds later ...");
        reconnect();
        if (connectCallBack != null)
            connectCallBack.onConnectFailed();
    }
    
    @Override
    public boolean isReconnect()
    {
        if (connectCallBack != null && this.isReconnect)
        {
            isReconnect = connectCallBack.isReconnect();
        }
        Log.e("Dudu_SDK", isReconnect ? "自动链接" : "不自动链接");
        return isReconnect;
    }
    
    protected NettyServer getServer(String result)
    {
        // TODO Auto-generated method stub
        String[] str = result.split("\\|");
        NettyServer server = new NettyServer();
        if (str.length == 2)
        {
            server.setIp(str[0]);
            server.setPort(Integer.parseInt(str[1]));
            return server;
        }
        else
        {
            return null;
        }
    }
    
    protected boolean isConnected()
    {
        Log.e("Dudu_SDK", isConnected ? "已链接" : "未链接");
        return isConnected;
    }
    
    public DuduUser getUser()
    {
        return user;
    }
    
    public void setUser(DuduUser user)
    {
        isReconnect = true;
        this.user = user;
    }
    
    public NettyServer getServer()
    {
        return server;
    }
    
    public static void setServer(NettyServer server)
    {
        DuduSDK.server = server;
    }
    
    @Override
    public void disconnect(Channel channel)
    {
        // TODO Auto-generated method stub
        Log.e("Dudu_SDK",
                "disconnect   " + ((user == null) ? "true" : user.getUserid()));
        close(false);
        isConnected = false;
        reconnect();
        Log.e("Dudu_SDK",
                "connectCallBack==null   " + (connectCallBack == null));
        if (connectCallBack != null)
            connectCallBack.disconnect(channel);
    }
    
    private RecivMessageCallBack selfcallback = new RecivMessageCallBack()
    {
        
        @Override
        public List<AppServerType> getServerType()
        {
            // TODO Auto-generated method stub
            return AppServerType.allvalues();
        }
        
        @Override
        public void onRecivMessage(Channel channel, NettyMessage message,
                String tag, JsonParser parser)
        {
            // TODO Auto-generated method stub
            if (client == null)
            {
                throwUnInit();
                return;
            }
            if (!messageFilter(message))
            {
                Log.e("", "SDK 已拦截掉指令   " + message.getHeader());
                return;
            }
            dosomething(channel, message, parser);
            
        }
        
        private boolean messageFilter(NettyMessage message)
        {
            // TODO Auto-generated method stub
            String data;
            try
            {
                data = new String(message.getContent(), "UTF-8");
                String[] splits = data.split("\\|");
                if (splits.length == 4)
                {
                    isReconnect = false;
                    close(false);
                    currentMsg = message;
                    Log.e("",
                            "切换服务器" + splits[splits.length - 2] + "" + Integer
                                    .valueOf(splits[splits.length - 1]));
                    Thread.sleep(500);
                    connect(splits[splits.length - 2],
                            Integer.valueOf(splits[splits.length - 1]));
                    return false;
                }
                else if (AppServerType.EX_TYPE.toString()
                        .equals(message.getHeader()) && "100".equals(splits[0]))
                {
                    user = null;
                    setCallingUser(null);
                    isConnected = false;
                    isReconnect = false;
                    close(false);
                }
                else if (AppServerType.EX_TYPE.toString()
                        .equals(message.getHeader()) && "0".equals(splits[0]))
                {
                    sleepTime = 10 * 1000;
                    isLooping = false;
                    reconnect();
                }
                else if (AppServerType.AB.toString()
                        .equals(message.getHeader()))
                {
                    NettyServer server = getServer(message.getCtxUTF8String());
                    Log.e("Dudu_SDK", "  NS:" + (server == null));
                    if (server != null)
                    {
                        // String ip="192.168.13.32";
                        // int port=1668;
                        // String ip="192.168.13.31";
                        // int port=16688;
                        // connect(ip, port);
                        connect(server.getIp(), server.getPort());
                    }
                    return false;
                }
                else if (AppServerType.RS.toString()
                        .equals(message.getHeader()))
                {
                    Responbean bean = (Responbean) paser.parseObject(splits[0],
                            Responbean.class);
                    if ("FS".equals(bean.getHeader()))
                    {
                        if (!splits[1].equals(callingUser.getUserid()))
                        {
                            return false;
                        }
                    }
                }
                commadFilter(message);
                // 处理特殊消息
                ServerFacoty.getInstance()
                        .getUserserver()
                        .CommadFilter(mChannel, message, paser);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
    };
    
    /**
     * 对讲状态
     * 
     * @param message
     */
    @SuppressWarnings("incomplete-switch")
    private void commadFilter(NettyMessage message)
    {
        try
        {
            String data = new String(message.getContent());
            String[] splits = data.split("\\|");
            switch (AppServerType.ofCommand(message.getHeader()))
            {
                case FN:
                    if (!isBusy())
                    {
                        JsonString fnStr = new JsonString(splits[0]);
                        DuduUser user = (DuduUser) paser
                                .jsonStringToObJect(fnStr, DuduUser.class);
                        Responbean responbean = new Responbean();
                        responbean
                                .setHeader(AppServerType.FS.getHeaderString());
                        responbean.setResponeId(message.getStringMessageId());
                        JsonString jsonString = paser
                                .ObjectToJsonString(responbean);
                        jsonString.getBuilder()
                                .append(Constants.SEPORATE)
                                .append(user.getUserid());
                        responeRs(mChannel, message.getHeader(), jsonString);
                    }
                    break;
                case FA:
                    setCallingUser(null);
                    friendUser = (DuduUser) paser.parseObject(splits[0],
                            DuduUser.class);
                    changeChattingModel(true, false);
                    break;
                case AL:
                    ChannalActiveusers activeusers = (ChannalActiveusers) paser
                            .parseObject(splits[0], ChannalActiveusers.class);
                    if (activeusers != null)
                    {
                        groupbean = new Groupbean();
                        groupbean.setGroupId(activeusers.getChannelId());
                        groupbean.setActiveUsers(activeusers.getActiveUsers());
                        if (!StringUtils.isEmpty(activeusers.getChannelNum()))
                        {
                            groupbean.setChannelNum(Integer
                                    .valueOf(activeusers.getChannelNum()));
                        }
                        groupbean.setGroupName(activeusers.getChannelName());
                    }
                    changeChattingModel(true, true);
                    break;
                case ED:
                    friendUser = null;
                    changeChattingModel(false, false);
                    break;
                case CG:
                    groupbean = (Groupbean) paser.parseObject(splits[0],
                            Groupbean.class);
                    List<DuduPosition> positions = new ArrayList<>();
                    DuduPosition position = new DuduPosition();
                    position.setUrl(user.getUrl());
                    position.setUserid(user.getUserid());
                    position.setUsername(user.getUsername());
                    positions.add(position);
                    groupbean.setActiveUsers(positions);
                    changeChattingModel(true, true);
                    break;
                case GC:
                    Groupbean cggroupbean = (Groupbean) paser
                            .parseObject(splits[0], Groupbean.class);
                    this.groupbean.setChannelNum(cggroupbean.getChannelNum());
                    this.groupbean.setGroupName(cggroupbean.getGroupName());
                    this.groupbean.setLogourl(cggroupbean.getLogourl());
                    break;
                case CF:
                    friendUser = (DuduUser) paser.parseObject(splits[0],
                            DuduUser.class);
                    changeChattingModel(true, false);
                    break;
                case CA:
                    @SuppressWarnings("unchecked")
                    List<String> cdchannemlIds = (List<String>) paser
                            .parseArray(splits[0], String.class);
                    if (cdchannemlIds == null || cdchannemlIds.size() == 0
                            || (groupbean != null && !cdchannemlIds
                                    .contains(groupbean.getGroupId())))
                    {
                        if (isGroupChatting())
                        {
                            changeChattingModel(false, false);
                        }
                    }
                    break;
                case NT:
                    if (groupbean != null && paser != null)
                    {
                        DuduPosition feuser = (DuduPosition) paser
                                .parseObject(splits[0], DuduPosition.class);
                        if (!groupbean.getActiveUsers().contains(feuser))
                        {
                            groupbean.getActiveUsers().add(feuser);
                        }
                    }
                    break;
                case EG:
                    if (groupbean != null && paser != null)
                    {
                        DuduPosition eguser = (DuduPosition) paser
                                .parseObject(splits[0], DuduPosition.class);
                        groupbean.getActiveUsers().remove(eguser);
                    }
                case FR:
                case FE:
                    setCallingUser(null);
                    break;
            }
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    protected boolean isChatting()
    {
        return isChatting;
    }
    
    protected boolean isFriendChatting()
    {
        return isChatting && !isGroupChatting();
    }
    
    protected void setChatting(boolean chatting)
    {
        isChatting = chatting;
    }
    
    protected boolean isGroupChatting()
    {
        Log.e("", "是否是群聊：——" + (isChatting && isGroupChatting));
        return isChatting && isGroupChatting;
    }
    
    protected void changeChattingModel(boolean isChatting,
            boolean isGroupChatting)
    {
        Log.e("", "是否在对讲：——" + isChatting + "是否是群聊：——" + isGroupChatting);
        this.isChatting = isChatting;
        this.isGroupChatting = isGroupChatting;
    }
    
    protected void dosomething(Channel channel, NettyMessage message,
            JsonParser parser)
    {
        // TODO Auto-generated method stub
        Log.e("", "界面登陆监听==null:" + (connectCallBack == null));
        if ((callBackMap == null ? 0 : callBackMap.entrySet().size()) == 0
                && connectCallBack != null)
        {
            connectCallBack.addLisenter(client);
        }
        Log.e("",
                "监听器的数量=" + (callBackMap == null ? 0
                        : callBackMap.entrySet().size()));
        for (Map.Entry<String, RecivMessageCallBack> entry : callBackMap
                .entrySet())
        {
            RecivMessageCallBack callBack = entry.getValue();
            if (callBack != null
                    && AppServerType.contains(message.getHeader(), callBack))
            {
                callBack.onRecivMessage(channel,
                        message,
                        entry.getKey(),
                        parser);
            }
        }
        
    }
    
    protected JsonParser getPaser()
    {
        return paser;
    }
    
    protected void setPaser(JsonParser paser)
    {
        this.paser = paser;
    }
    
    public static boolean isDebug()
    {
        return isDebug;
    }
    
    public static void setDebug(boolean isDebug)
    {
        DuduSDK.isDebug = isDebug;
    }
    
    protected Groupbean getGroupbean()
    {
        return groupbean;
    }
    
    protected void setGroupbean(Groupbean groupbean)
    {
        this.groupbean = groupbean;
    }
    
    protected DuduUser getFriendUser()
    {
        return friendUser;
    }
    
    protected void setFriendUser(DuduUser friendUser)
    {
        this.friendUser = friendUser;
    }
    
    @Override
    public void addLisenter(DuduClient client)
    {
        // TODO Auto-generated method stub
        
    }
    
    protected onConnetCallBack getConnectCallBack()
    {
        return connectCallBack;
    }
    
    protected void setConnectCallBack(onConnetCallBack connectCallBack)
    {
        this.connectCallBack = connectCallBack;
    }
    
    protected void setSendMessage(NettyMessage message)
    {
        sendMessage = message;
    }
    
    protected boolean isBusy()
    {
        return isBusy;
    }
    
    protected void setBusy(boolean isBusy)
    {
        this.isBusy = isBusy;
    }
    
    protected DuduUser getCallingUser()
    {
        return callingUser;
    }
    
    protected void setCallingUser(DuduUser callingUser)
    {
        setBusy(callingUser != null);
        this.callingUser = callingUser;
    }
    
}

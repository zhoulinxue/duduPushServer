package com.czl.chatServer.server.Impl.handlerImpl;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.server.IConnectLifeCycle;
import com.czl.chatServer.server.IFriendChatLifeCycle;
import com.czl.chatServer.server.IHandlerServer;
import com.czl.chatServer.server.INettyServer;
import com.czl.chatServer.server.IPushMessageServer;
import com.czl.chatServer.server.Impl.FriendChatServerImpl;
import com.czl.chatServer.server.Impl.NettyServerImpl;
import com.czl.chatServer.server.Impl.PushMessageImpl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述：前端 逻辑 分发 处理类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class AppHandlerServer implements IHandlerServer
{
    private IConnectLifeCycle connectServer = new AppConnectServerImpl();
    
    private IFriendChatLifeCycle friendChatServer = new FriendChatServerImpl();
    
    private INettyServer nettyServer = new NettyServerImpl();
    
    private IPushMessageServer pushMsg = new PushMessageImpl();
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        //有app 连接....
        connectServer.appConnect(ctx);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, NettyMessage msg)
            throws Exception
    {
        // 接收到APP消息
        switch (msg.getAppServerType())
        {
            case APP_LOGIN:
                connectServer.appLogin(ctx, msg);
                break;
            case EXIT_APP:
                connectServer.loginOut(ctx, msg);
                break;
            case FS:
                friendChatServer.invitesFriend(ctx, msg);
                break;
            case FR:
                friendChatServer.reJectCall(ctx, msg);
                break;
            case ED:
                friendChatServer.endFriendChat(ctx, msg);
                break;
            case ON_LINE:
                nettyServer.userIsOnLine(ctx, msg);
                break;
            case P2P_CHAT_BYTE:
            case SG:
                nettyServer.chatbyte(ctx, msg);
                break;
            case IM:
                pushMsg.pushImMessage(ctx.channel(), msg);
                break;
            case IS:
                pushMsg.pushCompelte(ctx,msg);
                break;
            
            case GS:
                
                break;
            case EG:
                
                break;
            case GT:
                
                break;
            case ET:
                
                break;
            case TA:
                
                break;
            case PP:
                
                break;
            case XY:
                
                break;
            case XZ:
                
                break;
            case FE:
                
                break;
            case LT:
                
                break;
            case FA:
                break;
            case RS:
                
                break;
            case IC:
                
                break;
            case PR:
                
                break;
            case AU:
                
                break;
            case FM:
                
                break;
            case GC:
                
                break;
            default:
                break;
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception
    {
        // 有异常
        
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception
    {
        // 连接 事件...触发
        try
        {
            if (IdleStateEvent.class.isAssignableFrom(evt.getClass()))
            {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.READER_IDLE)
                {
                    //读数据超时  即关闭连接  掉线处理
                    ctx.close();
                }
            }
        }
        finally
        {
            ctx.fireUserEventTriggered(evt);
            ReferenceCountUtil.release(evt);
        }
        
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        // app断链
        connectServer.appOffline(ctx);
    }
    
}

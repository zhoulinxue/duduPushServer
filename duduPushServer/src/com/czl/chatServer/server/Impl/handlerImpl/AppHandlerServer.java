package com.czl.chatServer.server.Impl.handlerImpl;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;
import com.czl.chatServer.server.IConnectLifeCycle;
import com.czl.chatServer.server.IFriendChatLifeCycle;
import com.czl.chatServer.server.IHandlerServer;
import com.czl.chatServer.server.INettyServer;
import com.czl.chatServer.server.IPushMessageServer;
import com.czl.chatServer.server.Impl.BaseMessageServiceImpl;
import com.czl.chatServer.server.Impl.ChattingModelManager;
import com.czl.chatServer.server.Impl.FriendChatServerImpl;
import com.czl.chatServer.server.Impl.NaviServerImpl;
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
public class AppHandlerServer extends BaseMessageServiceImpl
        implements IHandlerServer
{
    //连接业务
    private IConnectLifeCycle connectServer = new AppConnectServerImpl(this);
    
    //一对一 对讲业务
    private IFriendChatLifeCycle friendChatServer = new FriendChatServerImpl();
    
    // 常规 业务
    private INettyServer nettyServer = new NettyServerImpl();
    
    //推送业务
    private IPushMessageServer pushMsg = new PushMessageImpl();
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        Log.e("channelActive");
        //有app 连接....
        connectServer.appConnect(ctx);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, NettyMessage msg)
            throws Exception
    {
     
        AppServerType type=msg.getAppServerType();
        Log.e("channelRead"+type);
        // 接收到APP消息
        switch (msg.getAppServerType())
        {
            case LD:
                connectServer.appLogin(ctx, msg);
                break;
            case OU:
                ChattingModelManager.getInstance().userQuit(ctx, msg);
                //redis 注销 用户信息
                connectServer.loginOut(ctx, msg);
                break;
            case FS:
                ChattingModelManager.getInstance().creatChat(ctx, msg);
                break;
            case FA:
                ChattingModelManager.getInstance().statusChanged(ctx, msg);
                friendChatServer.agreeCall(ctx, msg);
                break;
            case FR:
                ChattingModelManager.getInstance().userQuit(ctx, msg);
                friendChatServer.reJectCall(ctx, msg);
                break;
            case FE:
                ChattingModelManager.getInstance().userQuit(ctx, msg);
                friendChatServer.cancelCall(ctx, msg);
                break;
            case ED:
                friendChatServer.endFriendChat(ctx, msg);
                ChattingModelManager.getInstance().userQuit(ctx, msg);               
                break;
            case ON:
                nettyServer.userIsOnLine(ctx, msg);
                break;
            case SM:
            case SG:
                ChattingModelManager.getInstance().chatByte(ctx, msg);
                break;
            case IM:
                pushMsg.pushImMessage(ctx.channel(), msg);
                break;
            case IS:
                pushMsg.pushMsgCompelte(ctx, msg);
                break;
            case GS:
                ChattingModelManager.getInstance().creatChat(ctx, msg);
                break;
            case EG:
                ChattingModelManager.getInstance().userQuit(ctx, msg);
                break;
            case GT:
            case ET:
                ChattingModelManager.getInstance().chatbyteEnd(ctx, msg);
                break;
            case TA:
                NaviServerImpl.getInstance().getTrafficPic(ctx.channel(), msg);
                break;
            case PP:
                NaviServerImpl.getInstance().getUserPersion(ctx.channel(), msg);
                break;
            case XY:
            case XZ:
                ChattingModelManager.getInstance().locationChange(ctx, msg);
                break;
            case LT:
                connectServer.testConnect(ctx, msg);
                break;
            case RS:
                pushMsg.pushRSMessage(ctx.channel(), msg);
                break;
            case IC:
                nettyServer.isChannelActive(ctx, msg);
                break;
            case PR:
                pushMsg.pushMsgsCompelte(ctx, msg);
                break;
            case AU:
                ChattingModelManager.getInstance().chatByte(ctx, msg);
                break;
            case FM:
                nettyServer.pushFMMsg(ctx, msg);
                break;
            case GC:
                nettyServer.groupChages(ctx, msg);
                break;
            case OF:
                String[] data=msg.getUserDataFromMsg();
                DuduPosition position=JSONObject.parseObject(data[1], DuduPosition.class);
                ChattingModelManager.getInstance().userOffline(position);
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
        Log.e("exceptionCaught");
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception
    {
        Log.e("userEventTriggered");
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
        Log.e("channelInactive");
        // app断链
        connectServer.appOffline(ctx);
    }
    
}

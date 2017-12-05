package com.czl.chatServer.server.Impl.handlerImpl;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.Constants;
import com.czl.chatServer.server.IHandlerServer;
import com.czl.chatServer.server.Impl.BaseMessageServiceImpl;
import com.czl.chatServer.utils.RedisManager;

import io.netty.channel.ChannelHandlerContext;

public class MSNodeHandler extends BaseMessageServiceImpl implements IHandlerServer
{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        // TODO Auto-generated method stub
        String nsName=ctx.channel().attr(Constants.NS_USER_NAME).get();
        if(!StringUtils.isEmpty(nsName)){
            String[] ipAndPort = nsName.split(":");
            // 删除 Node 监听端口
            RedisManager.nodeportExit(ipAndPort[0]);
            // NS服务器掉线 重置 该服务器人数
            RedisManager.nsShutDown(nsName);
        }
        
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, NettyMessage msg)
            throws Exception
    {
        // TODO Auto-generated method stub
        String[] data=getUserDataFromMsg(msg);
        switch (msg.getAppServerType())
        {
            case LC:               
                String[] lcnodeport = data[1].split(":");
                ctx.channel().attr(Constants.NS_USER_NAME).set(data[1]);
                RedisManager.nsNodePortRegister(lcnodeport[0], lcnodeport[2]);
                RedisManager.addNs(data[1]);
                System.out.println("ns注册成功！"+data[1]);
                break;
            case LQ:
                String[] lqnodeport = data[1].split(":");
                System.out.println("ns注销成功！已经关闭连接"+lqnodeport[1]);
                ctx.channel().close();
                break;
            
            default:
                break;
        }
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
}

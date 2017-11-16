package com.czl.chatServer.server.Impl.handlerImpl;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.server.HandlerServer;

import io.netty.channel.ChannelHandlerContext;
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
public class AppHandlerServer implements HandlerServer{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
	// TODO Auto-generated method stub
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	// TODO Auto-generated method stub
	
    }

}

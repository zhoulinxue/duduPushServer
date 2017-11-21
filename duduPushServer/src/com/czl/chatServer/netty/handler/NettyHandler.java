package com.czl.chatServer.netty.handler;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.server.IHandlerServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述：Netty 指令接收  类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class NettyHandler extends ChannelInboundHandlerAdapter {
    private IHandlerServer server;

    public NettyHandler(IHandlerServer server) {
	super();
	this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	// TODO Auto-generated method stub
	super.channelActive(ctx);
	server.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	// TODO Auto-generated method stub
	super.channelRead(ctx, msg);
	NettyMessage message = (NettyMessage) msg;
	server.channelRead(ctx, message);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	// TODO Auto-generated method stub
	super.userEventTriggered(ctx, evt);
	server.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	// TODO Auto-generated method stub
	super.channelInactive(ctx);
	server.channelInactive(ctx);
    }

}

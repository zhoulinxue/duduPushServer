package com.czl.chatServer.server;

import com.czl.chatClient.bean.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
/**
 * 
 * 项目名称：duduPushServer
 * 功能模块名称：消息接受 处理接口
 * 功能描述：接受消息 接口定义类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public interface HandlerServer {

    public void channelActive(ChannelHandlerContext ctx) throws Exception;

    public void channelRead(ChannelHandlerContext ctx, NettyMessage msg) throws Exception;

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception;

    public void channelInactive(ChannelHandlerContext ctx) throws Exception;

}

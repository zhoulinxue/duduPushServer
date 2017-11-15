package com.czl.chatServer.netty;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.Constants;
import com.czl.chatServer.NSConfig;
import com.czl.chatServer.netty.core.NodeServerType;
import com.czl.chatServer.netty.decoder.NsAppMessageDecoder;
import com.czl.chatServer.netty.encode.NettyMessageServerEncoder;
import com.czl.chatServer.netty.handler.NettyHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述：前断服务创建类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class AppServer extends Thread {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private volatile boolean closed = false;
    private NSConfig config;
    private NSClient client;

    // 配置服务端的NIO线程组
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public AppServer(NSConfig config, NSClient client) {
	this.client = client;
	this.config = config;
    }

    public void bind() {
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 512)
		    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws IOException {
			    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024 * 1024, false,
				    Unpooled.copiedBuffer(Constants.MESSAFE_END_TAG.getBytes())));
			    ch.pipeline().addLast(new NsAppMessageDecoder(1024 * 1024, 4, 4));
			    ch.pipeline().addLast(new NettyMessageServerEncoder());
			    ch.pipeline().addLast("IdleStateHandler",
				    new IdleStateHandler(120, 120, 120, TimeUnit.SECONDS));
			    ch.pipeline().addLast(new NettyHandler(config.getAppHandler()));
			}
		    });

	    // 绑定端口，同步等待成功
	    ChannelFuture f = b.bind(Integer.parseInt(config.getNsport())).sync();

	    if (f.isSuccess()) {
		client.send(buildLoginReq());
	    } else {
		client.send(buildLogOutReq());
	    }
	    f.channel().closeFuture().sync();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    workerGroup.shutdownGracefully();
	    bossGroup.shutdownGracefully();
	    // 所有资源释放完成之后，清空资源，再次发起重连操作
	    if (closed) {
		return;
	    }
	    executor.execute(new Runnable() {
		@Override
		public void run() {
		    try {
			TimeUnit.SECONDS.sleep(5);
			try {
			    bind();// 发起重连操作
			} catch (Exception e) {
			    e.printStackTrace();
			}
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    });
	}
    }

    public void run() {
	bind();
    }

    public void close() {
	closed = true;
	bossGroup.shutdownGracefully();
	workerGroup.shutdownGracefully();
    }

    /**
     * 
     * @return
     */
    private NettyMessage buildLoginReq() {
	NettyMessage message = new NettyMessage();
	message.setHeader(NodeServerType.LC.toString());
	try {
	    message.setContent((client.userId + Constants.SEPORATE + Constants.MESSAFE_END_TAG).getBytes("UTF-8"));
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return message;
    }

    private NettyMessage buildLogOutReq() {// LQ
	NettyMessage message = new NettyMessage();

	message.setHeader(NodeServerType.LQ.toString());
	try {
	    message.setContent((client.userId + Constants.SEPORATE + Constants.MESSAFE_END_TAG).getBytes("UTF-8"));
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return message;
    }

}

package com.czl.chatServer.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;
import com.czl.chatServer.Constants;
import com.czl.chatServer.ServerType;
import com.czl.chatServer.netty.core.NodeServerType;
import com.czl.chatServer.netty.decoder.NsClientMessageDecoder;
import com.czl.chatServer.netty.encode.NettyMessageServerEncoder;
import com.czl.chatServer.netty.handler.NsClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述： 向MS 注册NS 发送 NS 基本信息 到MS 主要 发送 LC{@link NodeServerType.LC} 指令
 * @author zhouxue
 * @version 1.0 2017年11月16日
 * Copyright: Copyright (c) zhouxue org.,Ltd. 2017
 * Company:zhouxue org
 */
public class NSClient extends Thread
{
    
    private ScheduledExecutorService executor = Executors
            .newScheduledThreadPool(1);
    
    EventLoopGroup group = new NioEventLoopGroup();
    
    private String msIp;
    
    private int msPort;
    
    private volatile boolean isConnected = false;
    
    public Channel channel;
    
    private NettyMessage message;
    private ServerType type;
    private NSClient client;
    /**
     * 
     * @param msIp
     *            MS 的ip 地址
     * @param msPort
     *            MS 的端口号
     * @param NsName
     *            本机的 基本信息 ip:port:nodePort
     */
    public NSClient(String msIp, int msPort, NettyMessage message, ServerType type)
    {
        this.msIp = msIp;
        this.msPort = msPort;
        this.message = message;
        this.type=type;
        client=this;
    }
    
    public void connect()
    {
        // 配置客户端NIO线程组
        try
        {
            Bootstrap b = new Bootstrap();
            // final BuzinessHandler businessHandler=new BuzinessHandler(this);
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception
                        {
                            ch.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(
                                            1024 * 1024, false,
                                            Unpooled.copiedBuffer(
                                                    Constants.MESSAFE_END_TAG
                                                            .getBytes())));
                            ch.pipeline().addLast(new NsClientMessageDecoder(
                                    1024 * 1024, 4, 4));
                            ch.pipeline().addLast("MessageEncoder",
                                    new NettyMessageServerEncoder());
                            ch.pipeline().addLast("readTimeoutHandler",
                                    new IdleStateHandler(60, 60, 60,
                                            TimeUnit.SECONDS));
                            ch.pipeline().addLast("NsClientHandler",
                                    new NsClientHandler(NSClient.this,type));                           
                        }
                    });
            // 发起异步连接操作
            ChannelFuture future = b
                    .connect(new InetSocketAddress(msIp, msPort)).sync();
            if(future.isSuccess()){
                isConnected = true;
                channel = future.channel();
                channel.closeFuture().sync();
            }
           
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            // 所有资源释放完成之后，清空资源，再次发起重连操作
            reconnect();
        }
    }
    
    public void reconnect()
    {
        // TODO Auto-generated method stub
        if (isConnected)
        {
            return;
        }
        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    TimeUnit.SECONDS.sleep(1);
                    try
                    {
                        System.out.println("发起重连NS操作");
                        connect();// 发起重连操作
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    
    @Override
    public void run()
    {
        connect();
    }
    
    public void send(NettyMessage message)
    {
        if (channel != null)
        {
            channel.writeAndFlush(message);
        }
        else
        {
            Log.e("channel ==null");
        }
    }

    public NettyMessage getMessage()
    {
        return message;
    }

    public void setMessage(NettyMessage message)
    {
        this.message = message;
    }

    public boolean isConnected()
    {
        return isConnected;
    }

    public void setConnected(boolean isConnected)
    {
        this.isConnected = isConnected;
    }
    
}

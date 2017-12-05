package com.czl.chatServer.netty;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;
import com.czl.chatServer.Constants;
import com.czl.chatServer.NSConfig;
import com.czl.chatServer.ServerType;
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
 * @author zhouxue
 * @version 1.0 2017年11月16日
 * Copyright: Copyright (c) zhouxue org.,Ltd. 2017
 * Company:zhouxue org
 */
public class NettyServer extends Thread
{
    private ScheduledExecutorService executor = Executors
            .newScheduledThreadPool(1);
    
    private volatile boolean closed = false;
    
    private NSConfig config;
    
    // 配置服务端的NIO线程组
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    
    private ServerType type;
    
    private int serverport;
    
    public NettyServer(NSConfig config, ServerType type)
    {
        this.config = config;
        this.type = type;
        serverport = getServerport(config, type);
    }
    
    private int getServerport(NSConfig config, ServerType serverType)
    {
        // TODO Auto-generated method stub
        int port = 0;
        switch (serverType)
        {
            case AppServer:
                Log.e("app 业务服务（NS 开启）");
                port = Integer.parseInt(config.getNsport());
                break;
            case NodeServer:
                Log.e("分布式 业务开启(NODE开启)");
                port = Integer.parseInt(config.getListeningport());
                break;
            case ShortClient:
                port = Integer.parseInt(config.getNsport());
                break;
            
            default:
                break;
        }
        return port;
    }
    
    public void bind()
    {
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 512)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws IOException
                        {
                            ch.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(
                                            1024 * 1024, false,
                                            Unpooled.copiedBuffer(
                                                    Constants.MESSAFE_END_TAG
                                                            .getBytes())));
                            ch.pipeline().addLast(
                                    new NsAppMessageDecoder(1024 * 1024, 4, 4));
                            ch.pipeline()
                                    .addLast(new NettyMessageServerEncoder());
                            ch.pipeline().addLast("IdleStateHandler",
                                    new IdleStateHandler(120, 120, 120,
                                            TimeUnit.SECONDS));
                            ch.pipeline().addLast(new NettyHandler(
                                    config.getServerHandler(type)));
                        }
                    });
            Log.e(serverport + "");
            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(serverport).sync();
            
            if (f.isSuccess())
            {
                dosomthingonSuccess(type);
            }
            else
            {
                dosomthingFiled(type);
            }
            f.channel().closeFuture().sync();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            // 所有资源释放完成之后，清空资源，再次发起重连操作
            if (closed)
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
                        TimeUnit.SECONDS.sleep(5);
                        try
                        {
                            bind();// 发起重连操作
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
    }
    
    private void dosomthingFiled(ServerType type)
    {
        // TODO Auto-generated method stub
        switch (type)
        {
            case AppServer:
                NSClient client = new NSClient(config.getServerip(),
                        Integer.parseInt(config.getNodeport()),
                        buildLogOutReq());
                client.start();
                break;
            
            default:
                break;
        }
    }
    
    /**
     * 
     * 功能简述： 功能详细描述：
     * 
     * @author zhouxue
     * @param type
     *            [参数说明]
     * @return void [返回类型说明]
     * @exception throws
     *                [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    private void dosomthingonSuccess(ServerType type)
    {
        // TODO Auto-generated method stub
        switch (type)
        {
            case AppServer:
                NSClient client = new NSClient(config.getServerip(),
                        Integer.parseInt(config.getNodeport()),
                        buildLoginReq());
                client.start();
                break;
            
            default:
                break;
        }
    }
    
    public void run()
    {
        bind();
    }
    
    public void close()
    {
        closed = true;
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
    
    /**
     * 
     * @return
     */
    private NettyMessage buildLoginReq()
    {
        NettyMessage message = new NettyMessage();
        message.setHeader(NodeServerType.LC.toString());
        try
        {
            message.setContent((Constants.SEPORATE+getContent() + Constants.MESSAFE_END_TAG)
                    .getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }
    
    private NettyMessage buildLogOutReq()
    {// LQ
        
        NettyMessage message = new NettyMessage();
        
        message.setHeader(NodeServerType.LQ.toString());
        try
        {
            message.setContent((Constants.SEPORATE+getContent() + Constants.MESSAFE_END_TAG)
                    .getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }
    
    private String getContent()
    {
        // TODO Auto-generated method stub
        StringBuffer buffer = new StringBuffer();
        buffer.append(config.getNsip());
        buffer.append(Constants.IP_PORT_SEPORATE);
        buffer.append(config.getNsport());
        buffer.append(Constants.IP_PORT_SEPORATE);
        buffer.append(config.getListeningport());
        return buffer.toString();
    }
    
}

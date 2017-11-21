package com.czl.chatServer.server;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.Constants;
import com.czl.chatServer.netty.decoder.NsClientMessageDecoder;
import com.czl.chatServer.netty.encode.NettyMessageServerEncoder;
import com.czl.chatServer.netty.handler.NsClientHandler;
import com.czl.chatServer.utils.RedisManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class BaseMessageServiceImpl implements BaseMessageServer
{
    private String currentIp;
    
    private int currentPort;
    
    @Override
    public String buildHead(NettyMessage message)
    {
        // TODO Auto-generated method stub
        StringBuffer buffer = new StringBuffer();
        buffer.append((char) message.getHeader0());
        buffer.append((char) message.getHeader1());
        return buffer.toString();
    }
    
    @Override
    public NettyMessage buildMessage(AppServerType header)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = new NettyMessage();
        message.setMessageId(StringUtils.getRandomMsgId());
        message.setHeader0(
                header.getHeaderString().getBytes(Constants.HEAD_CHAR_SET)[0]);
        message.setHeader1(
                header.getHeaderString().getBytes(Constants.HEAD_CHAR_SET)[1]);
        return message;
    }
    
    @Override
    public String endTag()
    {
        // TODO Auto-generated method stub
        return Constants.MESSAFE_END_TAG;
    }
    
    @Override
    public StringBuilder ObjectToString(Object object)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(JSONObject.toJSONString(object));
        return builder;
    }
    
    public StringBuilder ObjectToString(Object object,
            SimplePropertyPreFilter filter)
    {
        if (filter == null)
        {
            return ObjectToString(object);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(JSONObject.toJSONString(object, filter));
        return builder;
    }
    
    @Override
    public String seporate()
    {
        // TODO Auto-generated method stub
        return Constants.SEPORATE;
    }
    
    @Override
    public String[] getCurrentIpAndPort(Channel ctx)
    {
        // TODO Auto-generated method stub
        String[] str = new String[] { "0", "0" };
        try
        {
            
            if (ctx != null)
            {
                String ip = ctx.attr(Constants.KEY_USER_ID).get();
                if (!StringUtils.isEmpty(ip))
                {
                    str = initIpAndPort(ip);
                }
            }
            
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        return str;
        
    }
    
    protected String[] initIpAndPort(String ip)
    {
        // TODO Auto-generated method stub
        String[] str = ip.split(Constants.IP_PORT_SEPORATE);
        currentIp = str[0];
        currentPort = Integer.parseInt(str[1]);
        return str;
    }
    
    public String getCurrentIp(Channel ctx)
    {
        if (StringUtils.isEmpty(currentIp))
        {
            getCurrentIpAndPort(ctx);
        }
        return currentIp;
    }
    
    public int getCurrentPort(Channel ctx)
    {
        if (currentPort == 0)
        {
            getCurrentIpAndPort(ctx);
        }
        return currentPort;
    }
    
    @Override
    public byte[] getContentByte(String content)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String newContent = seporate() + content + endTag();
        return newContent.getBytes(Constants.CONTENT_CHAR_SET);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object jsonToObJect(String json, Class calss)
    {
        // TODO Auto-generated method stub
        return JSONObject.parseObject(json, calss);
    }
    
    @Override
    public NettyMessage sendEX(String content)
            throws IllegalArgumentException, UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage arg0 = buildMessage(AppServerType.EX_TYPE);
        arg0.setContent(getContentByte(content));
        return arg0;
    }
    
    @Override
    public void sendMessage(NettyMessage message, Channel ctx)
            throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        if (ctx != null && message != null)
        {
            ctx.writeAndFlush(message);
        }
        else
        {
            throw new IllegalArgumentException("data not alowed...");
        }
    }
    
    @Override
    public NettyMessage send00Back(String str, Channel ctx)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.RETURN_TAG);
        message.setContent(getContentByte(str));
        sendMessage(message, ctx);
        return message;
    }
    
    @Override
    public NettyMessage sendtoOtherNsData(String ip, final NettyMessage mymsg)
    {
        // TODO Auto-generated method stub
        
        System.out.println("目标服务器ip" + ip);
        final NettyMessage message = new NettyMessage();
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap b = new Bootstrap();
            
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
                                    new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("readTimeoutHandler",
                                    new IdleStateHandler(120, 30, 120,
                                            TimeUnit.MINUTES));
                            ch.pipeline().addLast("ClientShortHandler",
                                    new NsClientHandler(mymsg));
                            
                        }
                    });
            
            b.connect(ip, Integer.valueOf(RedisManager.getNodeport(ip)))
                    .channel()
                    .closeFuture()
                    .await();
            
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            group.shutdownGracefully();
        }
        return message;
        
    }
    
    @Override
    public String[] paserNettyMsg(NettyMessage message)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = (new String(message.getContent(),
                Constants.CONTENT_CHAR_SET)).split("\\|");
        return data;
    }
    
    @Override
    public void closeSocket(Channel ctx, String errorMsg)
    {
        // TODO Auto-generated method stub
        if (ctx != null)
        {
            ctx.close();
        }
    }
    
    @Override
    public void responeClient(Channel ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        if (ctx != null && msg != null)
        {
            String header = buildHead(msg);
            send00Back(header, ctx);
        }
    }
    
    @Override
    public boolean formatMessage(String[] data)
    {
        // TODO Auto-generated method stub
        if (data.length == 0)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public NettyMessage buildMessage(AppServerType header, String content)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(header);
        message.setContent(getContentByte(content));
        return message;
    }
    
}

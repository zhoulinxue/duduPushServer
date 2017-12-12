package com.czl.chatServer.netty.handler;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.Constants;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述：MS 链接类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class ShortClientHandler extends ChannelInboundHandlerAdapter
{
  
    
    private NettyMessage msg;
    
    public ShortClientHandler(NettyMessage msg)
    {
        super();
        this.msg = msg;
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward to the
     * next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * 
     * Sub-classes may override this method to change behavior.
     */
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);
     

        // Demo.ctx=ctx;
        if (msg != null)
        {
            ctx.writeAndFlush(msg);
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        // TODO Auto-generated method stub
        super.channelInactive(ctx);
    }
    
    /**
     * Calls {@link ChannelHandlerContext#fireChannelRead(Object)} to forward to
     * the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * 
     * Sub-classes may override this method to change behavior.
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception
    {
        try
        {
            NettyMessage message = (NettyMessage) msg;
            byte head0 = message.getHeader0();
            byte head1 = message.getHeader1();
            if (head0 == 48 && head1 == 48)
            {
                // System.out.println("NS向MS注册成功2");
            }
            else
            {
                
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            ctx.fireChannelRead(msg);
            ReferenceCountUtil.release(msg);
        }
        /////////////////////////////////////////////////////
        
    }
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception
    {
        // System.out.println("WWWWWWWWWWWWWWWWW:"+ctx.pipeline().channel().localAddress().toString())
        // ;
        cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }
    ///////////////////////////////////////////////
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception
    {
        try
        {
            if (IdleStateEvent.class.isAssignableFrom(evt.getClass()))
            {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.READER_IDLE)
                {
                    System.out.println("ns客户端read idle");
                    ctx.close();
                }
                else if (event.state() == IdleState.WRITER_IDLE)
                {
                    // System.out.println("ns客户端write idle,给MS发送心跳");
                    NettyMessage heatBeat = buildHeatBeat();
                    ctx.writeAndFlush(heatBeat);
                }
                // else if (event.state() == IdleState.ALL_IDLE)
                // System.out.println("ns客户端all idle");
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            ctx.fireUserEventTriggered(evt);
            ReferenceCountUtil.release(evt);
        }
    }
    
    private NettyMessage buildHeatBeat()
    {
        NettyMessage message = new NettyMessage();
        
        message.setHeader0((byte) 67);
        message.setHeader1((byte) 75);
        try
        {
            message.setContent((Constants.MESSAFE_END_TAG).getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }
}

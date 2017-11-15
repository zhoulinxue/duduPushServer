package com.czl.chatClient.handler;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.DuduClient;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Administrator on 2017/10/20.
 */

public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // task to run goes here
                if(!DuduClient.getInstance().isConnected()){
                    ctx.writeAndFlush(buildHeatBeat());
                }
            }
        };
        Timer timer = new Timer();
        long delay = 10;
        long intevalPeriod = 120*1000;
        // schedules the task to be run in an interval
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
    private NettyMessage buildHeatBeat() {
        NettyMessage message = new NettyMessage();
        message.setHeader(AppServerType.HEART_BEAT);
        message.setMessageId(StringUtils.getRandomMsgId());
        message.setContent("".getBytes());
        return message;
    }
}

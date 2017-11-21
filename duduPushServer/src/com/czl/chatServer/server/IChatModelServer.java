package com.czl.chatServer.server;

import java.util.Map;

import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.ChatType;

import io.netty.channel.ChannelHandlerContext;

public interface IChatModelServer
{
    /**
     * 
      * 功能简述：
      * 功能详细描述： 语音流
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void chatByte(ChannelHandlerContext ctx, NettyMessage msg);
    
    /**
     * 
      * 功能简述：
      * 功能详细描述： 创建对讲
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    
    public IChatModelServer creatChat(ChannelHandlerContext ctx, NettyMessage msg,
            ChatType type);
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：加入对讲
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void newUserIn(ChannelHandlerContext ctx, NettyMessage msg);
    
    /**
     * 
      * 功能简述：
      * 功能详细描述： 用户退出对讲
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void userQuit(ChannelHandlerContext ctx, NettyMessage msg);
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：结束频道对讲
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    
    public void finishGroup(ChannelHandlerContext ctx, NettyMessage msg);
    
    /**
     * 
      * 功能简述：
      * 功能详细描述： 结束 好友对讲
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    
    public void finishFriendTalk(ChannelHandlerContext ctx, NettyMessage msg);
    /**
     * 
      * 功能简述：
      * 功能详细描述：
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */    
    public void locationChange(ChannelHandlerContext ctx, NettyMessage msg);
    
    /**
     * 
      * 功能简述：
      * 功能详细描述： 获取 已 再对讲中的 集合
      * @author zhouxue
      * @param type
      * @return [参数说明]
      * @return Map<String,IChatModelServer> [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public Map<String, IChatModelServer> getModels(ChatType type);
    /**
     * 
      * 功能简述：
      * 功能详细描述： 数据发送结束
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void chatbyteEnd(ChannelHandlerContext ctx, NettyMessage msg);
    
}

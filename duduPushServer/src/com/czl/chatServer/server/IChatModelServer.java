package com.czl.chatServer.server;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.ChatType;
import com.czl.chatServer.UserStatus;

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
    public void chatByte(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException;
    
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
    
    public boolean creatChat(ChannelHandlerContext ctx, NettyMessage msg) throws UnsupportedEncodingException;
    
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
    public IChatModelServer newUserIn(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException;
    
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
    public void userQuit(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException;
    /**
     * 
      * 功能简述：
      * 功能详细描述： 根据 uid 退出频道
      * @author zhouxue
      * @param userid [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void userQuit(String userid)throws UnsupportedEncodingException;
    
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
      * 功能详细描述：
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */    
    public void locationChange(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException;
    
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
    public void chatbyteEnd(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException;
    /**
     * 
      * 功能简述：
      * 功能详细描述：
      * @author zhouxue
      * @return [参数说明]
      * @return UserStatus [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public UserStatus getUserStatus();
    /**
     * 
      * 功能简述：
      * 功能详细描述： 好友正忙
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void userBusy(ChannelHandlerContext ctx, NettyMessage msg);
    /**
     * 
      * 功能简述：
      * 功能详细描述： 获取对讲的type
      * @author zhouxue
      * @return [参数说明]
      * @return ChatType [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public ChatType getChatType();
    /**
     * 
      * 功能简述：
      * 功能详细描述： 用户是否繁忙
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public  boolean userIsBusy(ChannelHandlerContext ctx, NettyMessage msg)throws UnsupportedEncodingException;
    /**
     * 
      * 功能简述：
      * 功能详细描述：获取正在对讲中的人
      * @author zhouxue
      * @return [参数说明]
      * @return List<DuduUser> [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public List<DuduPosition> getUsers();
    /**
     * 
      * 功能简述：
      * 功能详细描述： 用户状态 变化
      * @author zhouxue
      * @param ctx
      * @param msg [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void statusChanged(ChannelHandlerContext ctx, NettyMessage msg);
    
    public String  getServerId();

    public void userOffline(DuduPosition position)throws UnsupportedEncodingException;
    
}

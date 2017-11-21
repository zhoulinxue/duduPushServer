package com.czl.chatServer.server;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.NettyMessage;

import io.netty.channel.Channel;

public interface INaviServer
{
    /**
     * 
      * 功能简述：
      * 功能详细描述： 获取用户坐标
      * @author zhouxue
      * @param ctx
      * @param msg
      * @throws UnsupportedEncodingException [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void getUserPersion(Channel ctx, NettyMessage msg)
            throws UnsupportedEncodingException;
    
    /**
     * 
      * 功能简述：
      * 功能详细描述： 获取交通路况图片
      * @author zhouxue
      * @param ctx
      * @param msg
      * @throws UnsupportedEncodingException [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void getTrafficPic(Channel ctx, NettyMessage msg)
            throws UnsupportedEncodingException;
    
    /**
     * 
      * 功能简述：
      * 功能详细描述： 获取好友 坐标
      * @author zhouxue
      * @param ctx
      * @param msg
      * @throws UnsupportedEncodingException [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void putFriendPersion(Channel ctx, NettyMessage msg)
            throws UnsupportedEncodingException;
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：获取频道 用户 坐标
      * @author zhouxue
      * @param ctx
      * @param msg
      * @throws UnsupportedEncodingException [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void putGroupPersion(Channel ctx, NettyMessage msg)
            throws UnsupportedEncodingException;
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：通过uid 获取用户坐标
      * @author zhouxue
      * @param uid
      * @return
      * @throws UnsupportedEncodingException [参数说明]
      * @return DuduPosition [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public DuduPosition getUserPersion(String uid)
            throws UnsupportedEncodingException;
}

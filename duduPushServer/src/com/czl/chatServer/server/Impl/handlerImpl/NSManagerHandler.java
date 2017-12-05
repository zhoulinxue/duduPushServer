package com.czl.chatServer.server.Impl.handlerImpl;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.Constants;
import com.czl.chatServer.netty.ServerException;
import com.czl.chatServer.server.IHandlerServer;
import com.czl.chatServer.server.Impl.BaseMessageServiceImpl;
import com.czl.chatServer.utils.RedisManager;

import io.netty.channel.ChannelHandlerContext;

public class NSManagerHandler extends BaseMessageServiceImpl
        implements IHandlerServer
{
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, NettyMessage msg)
            throws Exception
    {
        // TODO Auto-generated method stub
        NettyMessage message = (NettyMessage) msg;
        switch (message.getAppServerType())
        {
            case AC:
                String ipport = "";
                String acStr = message.getCtxUTF8String();
                String uid = getUserId(acStr);
                if (!StringUtils.isEmpty(uid))
                {
                    ipport = getIpAndPortFromCacheMsg(uid);
                }
                else
                {
                    ipport = getRandom();
                }
                String[] nsIpandPort = null;
                if (!StringUtils.isEmpty(ipport))
                {
                    nsIpandPort = ipport.split(":");
                    if (!RedisManager.keyIsExist(
                            Constants.ND_SERVER_IP + nsIpandPort[0]))
                    {
                        ipport = nsIpandPort[0] + Constants.IP_PORT_SEPORATE
                                + nsIpandPort[1];
                    }
                    NettyMessage abmsg = buildMessage(AppServerType.AB, ipport);
                    sendMessage(abmsg, ctx.channel());                   
                }
                else
                {
                    sendMessage(buildEx(ServerException.NS_NOEXIST.toInfo()),
                            ctx.channel());
                }              
                break; 
            default:
                break;
        }
    }
   
    
    /**
     * 
      * 功能简述：
      * 功能详细描述： 获取 用户掉线前的 IP及地址
      * @author zhouxue
      * @param uid
      * @return [参数说明]
      * @return String [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    private String getIpAndPortFromCacheMsg(String uid)
    {
        // TODO Auto-generated method stub
        String ipandPort = "";
        // 对讲关系的 uid;
        String friendId = RedisManager.getChatwithFriend(uid);
        // 对讲关系的 groupid;
        String groupId = RedisManager.getChatInGroup(uid);
        
        System.out.println("NS:" + uid + "!!" + groupId);
        if (!StringUtils.isEmpty(friendId))
        {
            DuduPosition user = RedisManager.IsOnline(friendId);
            if (user != null)
            {
                System.out.println("NS好友:" + ipandPort);
                ipandPort = user.getIp() + Constants.IP_PORT_SEPORATE
                        + user.getPort();
            }
        }
        else if (!StringUtils.isEmpty(groupId))
        {
            ipandPort = RedisManager.getGroupIp(groupId);
            System.out.println("NS频道:" + ipandPort);
        }
        else
        {
            ipandPort = getRandom();
            System.out.println("NS随机:" + ipandPort);
        }
        
        return ipandPort;
    }
    
    private String getRandom()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：
      * @author zhouxue
      * @param acStr
      * @return [参数说明]
      * @return String [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    private String getUserId(String acStr)
    {
        // TODO Auto-generated method stub
        String uid = "";
        System.err.println(acStr);
        if (!StringUtils.isEmpty(acStr))
        {
            String[] strs = acStr.split("\\|");
            for (String s : strs)
            {
                System.err.println(s);
            }
            if (strs != null && strs.length >= 2)
            {
                uid = strs[1];
            }
        }
        return uid;
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
}

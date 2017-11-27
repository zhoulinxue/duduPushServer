package com.czl.chatServer.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.DateUtils;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.Constants;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class RedisManager
{
    static Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();
    
    public static Channel getChannelByUid(String uid)
    {
        return channelMap.get(uid);
    }
    
    public static String getUserToken(String userid)
    {
        // TODO Auto-generated method stub
        String token = JedisUtils.get(Constants.USER_TOKEN + userid);
        System.out
                .println(Constants.USER_TOKEN + userid + "获取token   " + token);
        return token;
    }
    
    public static DuduUser getUserFromRedis(String userid)
    {
        // TODO Auto-generated method stub
        String json = JedisUtils.get(Constants.USER + userid);
        if (!StringUtils.isEmpty(json))
            return JSONObject.parseObject(json, DuduUser.class);
        else
        {
            return null;
        }
    }
    
    public static String getNodeport(String currIp)
    {
        String key = Constants.NODE_PORT + currIp;
        
        return JedisUtils.get(key);
    }
    
    public static void putUserInfo(ChannelHandlerContext ctx, DuduUser user)
    {
        // TODO Auto-generated method stub
        ctx.channel().attr(Constants.KEY_USER_ID).set(user.getUserid());
        APP2NSRegister(user.getUserid(),
                ctx.channel()
                        .pipeline()
                        .channel()
                        .localAddress()
                        .toString()
                        .substring(1));
        
    }
    
    /**
     * 缓存用户信息
     * 
     * @param lduser
     */
    public static void addUserInfoToRedis(DuduUser lduser)
    {
        // TODO Auto-generated method stub
        lduser.setLoginTime(
                DateUtils.timeFormatFull(System.currentTimeMillis()));
        String userjson = com.alibaba.fastjson.JSONObject.toJSONString(lduser);
        JedisUtils.set("user." + lduser.getUserid(), userjson, 0);
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述： 在 redis 注册用户信息
      * @author zhouxue
      * @param userId
      * @param currNsIpPort
      * @return [参数说明]
      * @return boolean [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static boolean APP2NSRegister(String userId, String currNsIpPort)
    {
        // System.out.println("登陆成功" + userId);
        try
        {
            String key = Constants.NS_IP + currNsIpPort;
            String sum = JedisUtils.get(key);
            if (sum == null || "".equals(sum))
            {
                {
                    JedisUtils.set(key, "1", 0);
                }
            }
            else
            {
                JedisUtils.set(key,
                        String.valueOf(Integer.valueOf(sum) + 1),
                        0);
            }
            JedisUtils.setSetAdd(Constants.THIS_NS_ONLIN + currNsIpPort,
                    userId);
            JedisUtils.setSetAdd(Constants.ON_LIN_USER, userId);
            JedisUtils.set(Constants.USER_ISONLINE + userId, currNsIpPort, 0);
            removeOfflineChatting(userId);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return true;
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：
      * @author zhouxue
      * @param userId [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void removeOfflineChatting(String userId)
    {
        // TODO Auto-generated method stub
        JedisUtils.del(Constants.OFFLINE_CHATTING_USER + userId);
        JedisUtils.setDel(Constants.OFFLINE_CHATTING_GROUP, userId);
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：缓存连接
      * @author zhouxue
      * @param userid
      * @param channel [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void putChannel(String userid, Channel channel)
    {
        // TODO Auto-generated method stub
        channelMap.put(userid, channel);
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：移除连接
      * @author zhouxue
      * @param userid [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void removeChannelByUid(String userid)
    {
        // TODO Auto-generated method stub
        channelMap.remove(userid);
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：
      * @author zhouxue
      * @param userId
      * @param currNsIpPort
      * @return [参数说明]
      * @return boolean [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static boolean app2NSLoginout(String userId, String currNsIpPort)
    {
        String key = Constants.NS_IP_PORT + currNsIpPort;
        try
        {
            String sum = JedisUtils.get(key);
            if (sum == null || "".equals(sum))
            {
                sum = "1";
            }
            else
            {
                if (Integer.valueOf(sum) < 0)
                {
                    sum = "1";
                }
            }
            JedisUtils.set(key, String.valueOf(Integer.valueOf(sum) - 1), 0);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        deleteCalling(userId);
        JedisUtils.setDel(Constants.ON_LIN_USER, userId);
        JedisUtils.setDel(Constants.THIS_NS_ONLIN + currNsIpPort, userId);
        JedisUtils.del(Constants.USER_ISONLINE + userId);
        delteUserInfo(userId);
        System.out.println("userId_redis_下线" + userId);
        return true;
    }
    
    public static void deleteCalling(String userid)
    {
        // TODO Auto-generated method stub
        String callerid = getCallingMsg(userid);
        JedisUtils.del(Constants.CALL_USER + userid);
        JedisUtils.del(Constants.CALLER + callerid);
    }
    
    public static String getCallingMsg(String userid)
    {
        // TODO Auto-generated method stub
        return JedisUtils.get(Constants.CALL_USER + userid);
    }
    
    /**
     * 删除用户信息
     * 
     * @param userid
     * @return
     */
    public static void delteUserInfo(String userid)
    {
        // TODO Auto-generated method stub
        JedisUtils.del(Constants.USER + userid);
    }
    
}

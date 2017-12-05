package com.czl.chatServer.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.utils.DateUtils;
import com.czl.chatClient.utils.Log;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.Constants;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RedisManager
{
    static Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();
    
    public static Channel getChannelByUid(String uid)
    {
        Log.e("获取连接" + uid);
        return channelMap.get(uid);
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
        Log.e("登录成功" + userid);
        channelMap.put(userid, channel);
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述： 缓存groupIp
      * @author zhouxue
      * @param groupId
      * @param ipandPort
      * @return [参数说明]
      * @return String [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    
    public static String putGroupIp(String groupId, String ipandPort)
    {
        // TODO Auto-generated method stub
        return JedisUtils.set(Constants.GROUP_IP + groupId, ipandPort, 0);
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：获取 频道IP
      * @author zhouxue
      * @param groupId
      * @return [参数说明]
      * @return String [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static String getGroupIp(String groupId)
    {
        // TODO Auto-generated method stub
        return JedisUtils.get(Constants.GROUP_IP + groupId);
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：获取用户token
      * @author zhouxue 
      * @param userid
      * @return [参数说明]
      * @return String [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static String getUserToken(String userid)
    {
        // TODO Auto-generated method stub
        String token = JedisUtils.get(Constants.USER_TOKEN + userid);
        System.out
                .println(Constants.USER_TOKEN + userid + "获取token   " + token);
        return token;
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：获取用户信息缓存
      * @author zhouxue
      * @param userid
      * @return [参数说明]
      * @return DuduUser [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
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
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：获取NODE 端口
      * @author zhouxue
      * @param currIp
      * @return [参数说明]
      * @return String [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static String getNodeport(String currIp)
    {
        String key = Constants.NODE_PORT + currIp;
        
        return JedisUtils.get(key);
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述： 缓存用户信息
      * @author zhouxue
      * @param ctx
      * @param user [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
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
     JedisUtils.set(Constants.USER+user.getUserid(), JSONObject.toJSONString(user),0);
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
        deleteCalling(userId, null);
        JedisUtils.setDel(Constants.ON_LIN_USER, userId);
        JedisUtils.setDel(Constants.THIS_NS_ONLIN + currNsIpPort, userId);
        JedisUtils.del(Constants.USER_ISONLINE + userId);
        delteUserInfo(userId);
        System.out.println("userId_redis_下线" + userId);
        return true;
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：以主叫ID 删除 呼叫信息
      * @author zhouxue
      * @param userid 主叫ID
      * @param myId [参数说明] 被叫ID
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void deleteCalling(String userid, String myId)
    {
        // TODO Auto-generated method stub
        String callerid = getCallingMsg(userid);
        JedisUtils.del(Constants.CALL_USER + userid);
        if (myId == null || myId.equals(callerid))
        {
            JedisUtils.del(Constants.CALLED + callerid);
        }
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：获取被叫ID
      * @author zhouxue
      * @param userid
      * @return [参数说明]
      * @return String [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
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
    
    /**
     * 按uid 查询用户是否在线 及其所在的服务器 情况
     * 
     * @param userid
     * @return
     */
    public static DuduUser IsOnline(String userid)
    {
        DuduUser user = null;
        try
        {
            user = getUserFromRedis(userid);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：
      * @author zhouxue
      * @param uid
      * @param fuid [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static boolean userCalling(String uid, String fuid)
    {
        // TODO Auto-generated method stub
        Jedis jedis = JedisUtils.getResource();
        try
        {
            
            Transaction t = jedis.multi();// 开始事务
            t.set(Constants.CALL_USER + fuid, uid);
            t.set(Constants.CALLED + uid, fuid);
            List<Object> list = t.exec();
            return list.size() == 2;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            JedisUtils.returnResource(jedis);
        }
    }
    
    public static String getChatwithFriend(String userId)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：开始对讲
      * @author zhouxue
      * @param userIdFromChannel
      * @param string [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static boolean startChattingWithFriend(String selfid,
            String friendid)
    {
        try
        {
            JedisUtils.set(Constants.CHAT_WITH_FRIEND + selfid, friendid, 0);
            JedisUtils.set(Constants.CHAT_WITH_FRIEND + friendid, selfid, 0);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：正在对讲的频道
      * @author zhouxue
      * @param uid
      * @return [参数说明]
      * @return String [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static String getChatInGroup(String uid)
    {
        // TODO Auto-generated method stub
        String key = Constants.GROUP_CHATTING + uid;
        Log.e("获取的键值对：" + key);
        return JedisUtils.get(key);
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：开始在频道内对讲
      * @author zhouxue
      * @param uid
      * @param groupID
      * @return [参数说明]
      * @return String [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static String startChatInGroup(String uid, String groupID)
    {
        // TODO Auto-generated method stub
        Log.e(uid+"!对讲中"+groupID);
        JedisUtils.setSetAdd(Constants.GROUP_CHATTING + groupID, uid);
        return JedisUtils.set(Constants.GROUP_CHATTING + uid, groupID, 0);
    }
    
    public static void deleteGroupIp(String groupid)
    {
        // TODO Auto-generated method stub
        JedisUtils.del(Constants.GROUP_IP + groupid);
        JedisUtils.del(Constants.GROUP_CHATTING + groupid);
    }
    
    public static void deletegroupChatMsg(String uid)
    {
        // TODO Auto-generated method stub
        String groupid = getChatInGroup(uid);
        JedisUtils.del(Constants.GROUP_CHATTING + uid);
        JedisUtils.setDel(Constants.GROUP_CHATTING + groupid, uid);
        deleteGroupIp(groupid);
    }
    
}

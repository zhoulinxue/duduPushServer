package com.czl.chatServer.server.Impl.handlerImpl;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.Constants;
import com.czl.chatServer.ServerType;
import com.czl.chatServer.netty.ServerException;
import com.czl.chatServer.server.IConnectLifeCycle;
import com.czl.chatServer.server.Impl.BaseMessageServiceImpl;
import com.czl.chatServer.server.Impl.PushMessageImpl;
import com.czl.chatServer.utils.DBUtils;
import com.czl.chatServer.utils.RedisManager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class AppConnectServerImpl extends BaseMessageServiceImpl
        implements IConnectLifeCycle
{
    @Override
    public void appOffline(ChannelHandlerContext ctx)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void appLogin(ChannelHandlerContext ctx, NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // 获取当前服务器IP和port
        String[] ipAndPort = getCurrentIpAndPort(ctx.channel());
        //从登录信息 来获取用户信息
        DuduUser user = parseUser(msg);
        if (user != null)
        {
            user.setIp(ipAndPort[0]);
            user.setPort(Integer.parseInt(ipAndPort[1]));
            // 从数据库判断 是否 是注册用户
            boolean isRegisteredUser = DBUtils
                    .isUserRegistered(user.getUserid());
            if (isRegisteredUser)
            {
                // 查看缓存的token
                String token = RedisManager.getUserToken(user.getUserid());
                if (!StringUtils.isEmpty(user.getToken())
                        && !user.getToken().equals(token))
                {
                    // token 失效
                    loginTimeOut(ctx);
                    return;
                }
            }
            else
            {
                Log.e("未注册用户 登陆  ——" + user.getUserid());
            }
            // 从 redis 查看当前登录信息
            DuduUser olduser = RedisManager.getUserFromRedis(user.getUserid());
            if (olduser != null
                    && !user.getDiviceid().equals(olduser.getDiviceid()))
            {
                //不在同一终端 登录 相同账号
                reLogin(olduser, user);
            }
            else
            {
                Channel channel = RedisManager
                        .getChannelByUid(user.getUserid());
                if (channel != null && olduser != null)
                {
                    //在同一终端 登录两次  关闭 老的 连接
                    channel.attr(Constants.KEY_USER_ID)
                            .set(olduser.getDiviceid());
                    channel.close();
                }
            }
            //缓存新连接
            RedisManager.putChannel(user.getUserid(), ctx.channel());
            RedisManager.putUserInfo(ctx, user);
            PushMessageImpl.getInstance().pushImMessages(ctx.channel(), user);
        }
        
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：  重复登录 的处理逻辑
      * @author zhouxue
      * @param alreadyUser
      * @param currentUser
      * @throws IllegalArgumentException
      * @throws UnsupportedEncodingException [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public void reLogin(DuduUser alreadyUser, DuduUser currentUser)
            throws IllegalArgumentException, UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        
        if (currentUser.getIp().equals(alreadyUser.getIp())
                && currentUser.getPort() == alreadyUser.getPort())
        {
            System.out.println(currentUser.getUserid() + "用户已经在线了_同服务器");
            // 本服务器
            Channel nbcapp = RedisManager
                    .getChannelByUid(alreadyUser.getUserid());
            if (!currentUser.getDiviceid().equals(alreadyUser.getDiviceid())
                    && nbcapp != null)
            {
                // 生成 提醒用户下线 的消息
                NettyMessage offlineMsg = sendEX(
                        ServerException.OFF_LINE.toInfo());
                sendMessage(offlineMsg, nbcapp);
            }
            // 不在维护老 通道
            RedisManager.removeChannelByUid(alreadyUser.getUserid());
        }
        else
        {
            // 不在本服务器
            NettyMessage message = buildMessage(
                    AppServerType.RELOGIN_OTHERNS_TYPE);
            String content = alreadyUser.getUserid() + seporate()
                    + currentUser.getDiviceid();
            message.setContent(getContentByte(content));
            // 向指定的 NS服务器发送消息
            sendtoOtherNsData(alreadyUser.getIp(), message);
        }
    }
    
    /**
     * 
      * 功能简述：
      * 功能详细描述：获取登录用户的 资料
      * @author zhouxue
      * @param msg
      * @return [参数说明]
      * @return DuduUser [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    private DuduUser parseUser(NettyMessage msg)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        String[] data = paserNettyMsg(msg);
        if (!StringUtils.isEmpty(data[1]))
        {
            // 从数据库获取用户消息
            DuduUser lduser = (DuduUser) jsonToObJect(data[1], DuduUser.class);
            lduser.setDiviceid(data[2]);
            return lduser;
        }
        return null;
    }
    
    @Override
    public void appConnect(ChannelHandlerContext ctx)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void loginOut(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        String fromUserId = getUserIdFromChannel(ctx);
        RedisManager.app2NSLoginout(fromUserId,
                ctx.channel().localAddress().toString().substring(1));
    }
    
    @Override
    public void loginTimeOut(ChannelHandlerContext ctx)
    {
        // TODO Auto-generated method stub
        // 生成 提醒用户下线 的消息
        NettyMessage offlineMsg;
        try
        {
            offlineMsg = buildMessage(AppServerType.EX_TYPE,
                    ServerException.OFF_LINE.toInfo());
            sendMessage(offlineMsg, ctx.channel());
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void testConnect(ChannelHandlerContext ctx, NettyMessage msg)
    {
        // TODO Auto-generated method stub
        NettyMessage arg0;
        try
        {
            arg0 = buildMessage(AppServerType.OK);
            arg0.setContent(("|\n").getBytes(Constants.CONTENT_CHAR_SET));
            ctx.writeAndFlush(arg0);
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}

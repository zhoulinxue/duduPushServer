package com.czl.chatServer;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatServer.server.HandlerServer;
import com.czl.chatServer.server.Impl.handlerImpl.AppHandlerServer;
import com.czl.chatServer.server.Impl.handlerImpl.NodeHandlerServer;
import com.czl.chatServer.server.Impl.handlerImpl.ShorthandlerServer;
import com.czl.chatServer.utils.FileUtil;

/**
 * 
 * 项目名称：duduPushServer
 * 功能模块名称：入口类
 * 功能描述：启动程序的配置信息
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class NSConfig
{
    private static NSConfig config = null;
    
    private String redisip;
    
    private String redispwd;
    
    private String jdbcurl;
    
    private String jdbcuser;
    
    private String jdbcpwd;
    
    private String serverip;
    
    private String nodeport;
    
    private String appport;
    
    private String nsip;
    
    private String nsport;
    
    private String listeningport;
    
    private String userid;
    
    private String heartbeat;
    
    private String redisport;
    
    private Builder builder;
    
    public NSConfig(Builder builder)
    {
        super();
        this.builder = builder;
        String cfg;
        try
        {
            cfg = FileUtil.BufferedReader("config.json");
            config = JSONObject.parseObject(cfg.trim(), NSConfig.class);
            config.setBuilder(builder);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public NSConfig()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static NSConfig creatDefault()
    {
        if (config == null)
        {
            String cfg;
            try
            {
                cfg = FileUtil.BufferedReader("config.json");
                config = JSONObject.parseObject(cfg.trim(), NSConfig.class);
                config.setBuilder(new NSConfig().creatDefaultBuilder());
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        return config;
    }
    
    private Builder creatDefaultBuilder()
    {
        // TODO Auto-generated method stub
        NSConfig.Builder builder = new Builder();
        builder.setAppHandler(new AppHandlerServer())
                .setNodeHandler(new NodeHandlerServer())
                .setShorHandler(new ShorthandlerServer());
        return builder;
    }
    
    public String getRedisip()
    {
        return redisip;
    }
    
    public void setRedisip(String redisip)
    {
        this.redisip = redisip;
    }
    
    public String getRedispwd()
    {
        return redispwd;
    }
    
    public void setRedispwd(String redispwd)
    {
        this.redispwd = redispwd;
    }
    
    public String getJdbcurl()
    {
        return jdbcurl;
    }
    
    public void setJdbcurl(String jdbcurl)
    {
        this.jdbcurl = jdbcurl;
    }
    
    public String getJdbcuser()
    {
        return jdbcuser;
    }
    
    public void setJdbcuser(String jdbcuser)
    {
        this.jdbcuser = jdbcuser;
    }
    
    public String getJdbcpwd()
    {
        return jdbcpwd;
    }
    
    public void setJdbcpwd(String jdbcpwd)
    {
        this.jdbcpwd = jdbcpwd;
    }
    
    public String getServerip()
    {
        return serverip;
    }
    
    public void setServerip(String serverip)
    {
        this.serverip = serverip;
    }
    
    public String getNodeport()
    {
        return nodeport;
    }
    
    public void setNodeport(String nodeport)
    {
        this.nodeport = nodeport;
    }
    
    public String getAppport()
    {
        return appport;
    }
    
    public void setAppport(String appport)
    {
        this.appport = appport;
    }
    
    public String getNsip()
    {
        return nsip;
    }
    
    public void setNsip(String nsip)
    {
        this.nsip = nsip;
    }
    
    public String getNsport()
    {
        return nsport;
    }
    
    public void setNsport(String nsport)
    {
        this.nsport = nsport;
    }
    
    public String getListeningport()
    {
        return listeningport;
    }
    
    public void setListeningport(String listeningport)
    {
        this.listeningport = listeningport;
    }
    
    public String getUserid()
    {
        return userid;
    }
    
    public void setUserid(String userid)
    {
        this.userid = userid;
    }
    
    public String getHeartbeat()
    {
        return heartbeat;
    }
    
    public void setHeartbeat(String heartbeat)
    {
        this.heartbeat = heartbeat;
    }
    
    public String getRedisport()
    {
        return redisport;
    }
    
    public void setRedisport(String redisport)
    {
        this.redisport = redisport;
    }
    
    @Override
    public String toString()
    {
        return "NSConfig [redisip=" + redisip + ", redispwd=" + redispwd
                + ", jdbcurl=" + jdbcurl + ", jdbcuser=" + jdbcuser
                + ", jdbcpwd=" + jdbcpwd + ", serverip=" + serverip
                + ", nodeport=" + nodeport + ", appport=" + appport + ", nsip="
                + nsip + ", nsport=" + nsport + ", listeningport="
                + listeningport + ", userid=" + userid + ", heartbeat="
                + heartbeat + ", redisport=" + redisport + "]";
    }
    
    public Builder getBuilder()
    {
        return builder;
    }
    
    public void setBuilder(Builder builder)
    {
        this.builder = builder;
    }
    
    private HandlerServer getAppHandler()
    {
        // TODO Auto-generated method stub
        return builder.getAppHandler();
    }
    
    class Builder
    {
        private HandlerServer appHandler;
        
        private HandlerServer nodeHandler;
        
        private HandlerServer shorHandler;
        
        public Builder()
        {
            super();
        }
        
        public HandlerServer getAppHandler()
        {
            return appHandler;
        }
        
        public Builder setAppHandler(HandlerServer appHandler)
        {
            this.appHandler = appHandler;
            return this;
        }
        
        public HandlerServer getNodeHandler()
        {
            return nodeHandler;
        }
        
        public Builder setNodeHandler(HandlerServer nodeHandler)
        {
            this.nodeHandler = nodeHandler;
            return this;
        }
        
        public HandlerServer getShorHandler()
        {
            return shorHandler;
        }
        
        public Builder setShorHandler(HandlerServer shorHandler)
        {
            this.shorHandler = shorHandler;
            return this;
        }
        
    }

    public HandlerServer getServerHandler(ServerType type)
    {
        // TODO Auto-generated method stub
        HandlerServer  server=null;
        switch (type)
        {
            case AppServer:
                server=getAppHandler();
                break;
            
            case NodeServer:
                server=getNodeHandler();
                break;
            case ShortClient:
                server=getShorHandler();
                break;
            
            default:
                break;
        }
        return server;
    }

    private HandlerServer getShorHandler()
    {
        // TODO Auto-generated method stub
        return builder.getShorHandler();
    }

    private HandlerServer getNodeHandler()
    {
        // TODO Auto-generated method stub
        return builder.getNodeHandler();
    }

   
}

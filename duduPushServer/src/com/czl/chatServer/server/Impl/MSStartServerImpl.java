package com.czl.chatServer.server.Impl;

import com.czl.chatServer.ChatType;
import com.czl.chatServer.NSConfig;
import com.czl.chatServer.ServerType;
import com.czl.chatServer.netty.NettyServer;
import com.czl.chatServer.server.IMSStartServer;

public class MSStartServerImpl implements IMSStartServer
{

    @Override
    public void start()
    {
        // TODO Auto-generated method stub
        NSConfig nsConfig=initConfig();
        startNodeServer(nsConfig);
        startNSManagerServer(nsConfig);
    }

    @Override
    public NSConfig initConfig()
    {
        // TODO Auto-generated method stub
        return NSConfig.creatDefault();
    }

    @Override
    public void startNodeServer(NSConfig config)
    {
        // TODO Auto-generated method stub
        NettyServer server=new NettyServer(config, ServerType.MS_NODE_SERVER);
        server.start();
    }

    @Override
    public void startNSManagerServer(NSConfig config)
    {
        // TODO Auto-generated method stub
        NettyServer server=new NettyServer(config, ServerType.MS_MANAGER_SERVER);
        server.start();
    }
    
}

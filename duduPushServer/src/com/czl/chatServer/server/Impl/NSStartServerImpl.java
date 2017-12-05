package com.czl.chatServer.server.Impl;

import com.czl.chatServer.Constants;
import com.czl.chatServer.NSConfig;
import com.czl.chatServer.ServerType;
import com.czl.chatServer.netty.NettyServer;
import com.czl.chatServer.netty.NSClient;
import com.czl.chatServer.server.INSStartServer;
/**
 * 
 * 项目名称：duduPushServer
 * 功能模块名称： 程序启动 业务逻辑 实现类
 * 功能描述：实现程序 参数初始化、程序启动逻辑实现
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class NSStartServerImpl implements INSStartServer {

    @Override
    public  NSConfig initConfig() {
	// TODO 初始化默认服务器配置
	return NSConfig.creatDefault();
    }

    @Override
    public void registeNS(NSConfig config) {
	// TODO Auto-generated method stub
        
    }

    @Override
    public void startNodeServer(NSConfig config) {
	// TODO Auto-generated method stub
           NettyServer  server=new NettyServer(config,  ServerType.NodeServer);
           server.start();
    }

    @Override
    public void startAppServer(NSConfig config) {
	// TODO Auto-generated method stub
	NettyServer server = new NettyServer(config,ServerType.AppServer);
	server.start();
    }

    @Override
    public final void start() {
	// 初始化 服务器启动参数
	NSConfig config = initConfig();
	//开启跨服务器 监听
	startNodeServer(config);
	//开启APP监听
	startAppServer(config);

    }

}

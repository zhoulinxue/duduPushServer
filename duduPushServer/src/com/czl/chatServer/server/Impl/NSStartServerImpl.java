package com.czl.chatServer.server.Impl;

import com.czl.chatServer.Constants;
import com.czl.chatServer.NSConfig;
import com.czl.chatServer.netty.AppServer;
import com.czl.chatServer.netty.NSClient;
import com.czl.chatServer.server.NSStartServer;
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
public class NSStartServerImpl implements NSStartServer {

    @Override
    public final NSConfig initConfig() {
	// TODO Auto-generated method stub
	return NSConfig.creatDefault();
    }

    @Override
    public void registeNS(NSConfig config) {
	// TODO Auto-generated method stub

    }

    @Override
    public void startNodeServer(NSConfig config) {
	// TODO Auto-generated method stub

    }

    @Override
    public void startAppServer(NSConfig config) {
	// TODO Auto-generated method stub
	StringBuffer buffer = new StringBuffer();
	buffer.append(config.getNsip());
	buffer.append(Constants.IP_PORT_SEPORATE);
	buffer.append(config.getNsport());
	buffer.append(Constants.IP_PORT_SEPORATE);
	buffer.append(config.getListeningport());
	NSClient client = new NSClient(config.getServerip(), Integer.parseInt(config.getNodeport()), buffer.toString());
	client.start();
	AppServer server = new AppServer(config, client);
	server.start();
    }

    @Override
    public final void start() {
	// TODO Auto-generated method stub
	NSConfig config = initConfig();

	startNodeServer(config);

	startAppServer(config);

    }

}

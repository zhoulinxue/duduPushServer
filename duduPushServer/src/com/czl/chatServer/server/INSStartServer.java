package com.czl.chatServer.server;

import com.czl.chatServer.NSConfig;

/**
 * 
 * 项目名称：duduPushServer
 * 功能模块名称： 服务启动业务类
 * 功能描述： 启动程序 业务逻辑 接口
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */

public interface INSStartServer {
	/**
	 * 开启NS 服务
	 */
	public void start();
	
	/**
	 * 读取 服务器配置
	 * @return 配置对象
	 */
	public NSConfig initConfig();
	
	/**
	 * 向MS注册NS服务
	 * @param msIp
	 * @param msPort
	 */
	public void registeNS(NSConfig config);
	
	/**
	 * 启动node管理 服务
	 * @param nodeIp
	 * @param bindPort
	 */
	public  void  startNodeServer(NSConfig config);
	
	/**
	 * 启动app 业务 服务器
	 * @param appIp
	 * @param bindPort
	 */
	public void  startAppServer(NSConfig config);

}

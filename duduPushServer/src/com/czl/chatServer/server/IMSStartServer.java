package com.czl.chatServer.server;

import com.czl.chatServer.NSConfig;

public interface IMSStartServer
{
    /**
     * 开启MS 服务
     */
    public void start();
    
    /**
     * 读取 服务器配置
     * @return 配置对象
     */
    public NSConfig initConfig();
    
    /**
     * 启动node管理 服务
     * @param nodeIp
     * @param bindPort
     */
    public  void  startNodeServer(NSConfig config);
    
    /**
     * 启动NS 业务 服务器
     * @param appIp
     * @param bindPort
     */
    public void  startNSManagerServer(NSConfig config);
}

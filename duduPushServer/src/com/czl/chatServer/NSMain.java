package com.czl.chatServer;

import com.czl.chatServer.server.NSStartServer;
import com.czl.chatServer.server.Impl.NSStartServerImpl;

/**
 * 
 * 项目名称：duduPushServer
 * 功能模块名称：主程序入口
 * 功能描述：启动程序的入口类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class NSMain
{
    public static void main(String[] args)
    {
        NSStartServer server = new NSStartServerImpl();
        server.start();
    }
}

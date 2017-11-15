package com.czl.chatClient.bean;

import com.czl.chatClient.Constants;

/**
 * Created by Administrator on 2017/8/4.
 */

public class NettyServer {
	// 默认服务器 ip
	private String ip;
	// 默认 服务器 端口
	private int port;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public static NettyServer creatDefualt() {
		// TODO Auto-generated method stub
		NettyServer server = new NettyServer();
		server.setIp(Constants.MS_IP);
		server.setPort(10001);
		return server;
	}
}

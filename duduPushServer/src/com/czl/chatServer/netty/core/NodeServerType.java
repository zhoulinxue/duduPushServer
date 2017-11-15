package com.czl.chatServer.netty.core;

import com.czl.chatClient.utils.StringUtils;
/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述：Node 服务器间 交互指令类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public enum NodeServerType {

	FE("FE"),

	TF("TF"),

	NA("NA"),

	TM("TM"),

	CL("CL"),

	DM("DM"),

	RS("RS"),
	// 自定义 消息
	FM("FM"), 

	ED("ED"),
	// 用户 不在线
	GC("GC", true),

	NR("NR"), 
	//关闭NS
	LQ("LQ"),
	//注册NS 到MS
	LC("LC");

	private NodeServerType(String headerString) {
		this.headerString = headerString;
	}

	private String headerString;
	private boolean isSystemType = false;

	public String getHeaderString() {
		return headerString;
	}

	public void setHeaderString(String headerString) {
		this.headerString = headerString;
	}

	private NodeServerType(String headerString, boolean isSystemType) {
		this.headerString = headerString;
		this.isSystemType = isSystemType;
	}

	public boolean isSystemType() {
		return isSystemType;
	}

	public void setSystemType(boolean isSystemType) {
		this.isSystemType = isSystemType;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return headerString;
	}

	public static NodeServerType ofCommand(String header) {
		// TODO Auto-generated method stub
		if (!StringUtils.isEmpty(header)) {
			for (NodeServerType s : values()) {
				if (header.equals((s.toString()))) {
					return s;
				}
			}
		}
		return null;
	}

	public static boolean isCommand(String header) {
		// TODO Auto-generated method stub
		if (!StringUtils.isEmpty(header)) {
			for (NodeServerType s : values()) {
				if (header.equals((s.toString()))) {
					return true;
				}
			}
		}
		return false;
	}

}

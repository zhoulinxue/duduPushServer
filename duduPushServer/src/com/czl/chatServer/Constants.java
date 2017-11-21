package com.czl.chatServer;

import io.netty.util.AttributeKey;
/**
 * 
 * 项目名称：
 * 功能模块名称：
 * 功能描述：常量类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class Constants {
	public static final String IM_CHANELL_TYPE = "channelcall";
	public static final String IM_FRIEND_TYPE = "friendcall";
	public static String USER_HEAD="user";
	public static AttributeKey<String> KEY_USER_ID = AttributeKey.valueOf(USER_HEAD);
	public static String CONTENT_CHAR_SET ="utf-8";
	public static String POSITION_KEY="position.";
	public static String CALL_USER="caling.";
	public static String CALLER="caller.";
	public static String CHANNEL_ID="9999";
	public static String CHATTING_ID="chatting_id";
	public static String CHATTING_IN_OR_WITH="ct.";
	//程序编码格式
    public static final String HEAD_CHAR_SET = "utf-8";
	public static final String ID = "ID";
	public static final String ON_LIN_USER = "onLine";
	public static final String USER_ISONLINE = "gd.";
	public static final String GROUP_IP = "group_ip";
	public static final String USER_TOKEN="app.token.";
   
    public static final String ZK_DATA_PATH ="/tmp_root_path";
    public static final String ZK_REGISTRY_PATH = ZK_DATA_PATH+"/data";
    public static final int ZK_SESSION_TIMEOUT = 2000;
    public static final String USER = "user.";
    public static final String NODE_PORT = "nd.";
    public static final String NS_IP =  "ns.";
    // 分隔符
    public static String SEPORATE="|";
    //消息结束符
    public static String MESSAFE_END_TAG="\n";
    // ip和port 间隔符号
	public static String IP_PORT_SEPORATE=":";
	public static double EARTH_RADIUS = 6378.137;
	public static String OFFLINE_CHATTING_GROUP="offlineChattingGroup";
	public static String OFFLINE_CHATTING_USER="offlineTime.";
	public static long OFFLING_DURING=80*1000;
	public static long OFFLING_USER_DURING=10*1000;
	public static String OUT_OF_CHATTING="NoChatting.";
	public static String CHATTING_CHANNEL="chatting_channel";
	public static String THIS_NS_ONLIN="on_line_in";
	public static String OFF_USER="off_user"; 
	    // 
	public static String ZKADDRESS="192.168.13.31:2181";
}

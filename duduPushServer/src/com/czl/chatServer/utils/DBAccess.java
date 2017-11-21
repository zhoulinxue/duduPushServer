package com.czl.chatServer.utils;

import java.util.HashMap;
import java.util.Map;

import com.czl.chatServer.NSConfig;
import com.czl.chatServer.bean.Database;

public class DBAccess {
	private static Map<DBName, Database> map = new HashMap<DBName, Database>();
	private static String serverip =NSConfig.creatDefault().getJdbcurl();
	private static String password =NSConfig.creatDefault().getJdbcpwd();
	public static Database getDB(DBName db){
		if(map.size() < 1){
			map.put(DBName.userdb, new Database(serverip + "/userdb?useUnicode=true&characterEncoding=utf8",NSConfig.creatDefault().getJdbcuser(),password));
			map.put(DBName.filedb, new Database(serverip + "/filedb?useUnicode=true&characterEncoding=utf8",NSConfig.creatDefault().getJdbcuser(),password));
			map.put(DBName.logdb, new Database(serverip + "/filedb?useUnicode=true&characterEncoding=utf8",NSConfig.creatDefault().getJdbcuser(),password));
			map.put(DBName.channelinfo, new Database(serverip + "/filedb?useUnicode=true&characterEncoding=utf8",NSConfig.creatDefault().getJdbcuser(),password));
		}		
		return map.get(db);

	}
	public static enum DBName {
		userdb, filedb,logdb,channelinfo;
    }
}

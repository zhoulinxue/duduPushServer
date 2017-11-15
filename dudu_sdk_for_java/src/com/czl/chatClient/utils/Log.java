package com.czl.chatClient.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.czl.chatClient.Constants;
import com.czl.chatClient.DuduSDK;
import com.czl.chatClient.bean.NettyMessage;

public class Log {
	public static void error(String error) {
		System.err.println(new Date().toString() + "|" + error);
	}

	public static void printeNettymsg(NettyMessage msg, String tag) {
		try {
			String uid = "";
			String content = new String(msg.getContent(), Constants.CONTENT_CHAR_SET);
			if (msg.getFromUerId() != null) {
				uid = new String(msg.getFromUerId(), Constants.CONTENT_CHAR_SET);
			}
			StringBuffer buffer = new StringBuffer();
			buffer.append((char) msg.getHeader0());
			buffer.append((char) msg.getHeader1());
			if (!(msg.getHeader0() == 83 && msg.getHeader1() == 77)
					&& !(msg.getHeader0() == 83 && msg.getHeader1() == 71)) {
				e("Dudu_SDK",DateUtils.timeFormatFull(System.currentTimeMillis()) + tag + uid + "    header:="
						+ buffer.toString() + "    content=" + content + "换行");
			} else {
				e("Dudu_SDK", DateUtils.timeFormatFull(System.currentTimeMillis()) + tag + uid + "header:="
						+ buffer.toString() + "语音消息");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void e(String tag,String msg){
		if(DuduSDK.isDebug())
		System.out.println("Dudu_SDK___"+msg);
	}
	
	
}

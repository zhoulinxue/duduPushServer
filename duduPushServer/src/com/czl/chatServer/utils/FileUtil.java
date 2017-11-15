package com.czl.chatServer.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
/**
 * 
 * 项目名称：duduPushServer
 * 功能模块名称：文件类
 * 功能描述：文件辅助类
 * @author "zhouxue"
 * @version 1.0 2017年11月15日
 * Copyright: Copyright (c) zhouxue Co.,Ltd. 2017
 * Company:"zhouxue" org
 */
public class FileUtil {
	public static String BufferedReader(String path) throws Exception {
		File file = new File(path);
		if (!file.exists() || file.isDirectory())
			return "";

		byte[] buf = new byte[1024];
		StringBuffer sb = new StringBuffer();
		FileInputStream fis = new FileInputStream(file);
		while ((fis.read(buf)) != -1) {
			sb.append(new String(buf));
			buf = new byte[1024];
		}
		fis.close();
		return sb.toString();
	}


	public static void BufferedWrite(String path, String b) throws Exception {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
			out.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void BufferedWrite(String path, byte[] cbuf, int off, int len) throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path, true);
			out.write(cbuf, off, len);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

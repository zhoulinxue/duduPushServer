package com.czl.chatClient.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/16.
 */

public class Responbean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6287357038000051431L;
	private String responeId;
	private String header;

	public String getResponeId() {
		return responeId;
	}

	public void setResponeId(String responeId) {
		this.responeId = responeId;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

}

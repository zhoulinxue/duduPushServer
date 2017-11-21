package com.czl.chatServer.bean;

import java.io.Serializable;

public class BasePushMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String dataid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDataid() {
		return dataid;
	}
	public void setDataid(String dataid) {
		this.dataid = dataid;
	}
	public BasePushMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}

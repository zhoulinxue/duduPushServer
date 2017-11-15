package com.czl.chatClient.bean;

import java.util.List;

public class Groupbean {
	private String groupId;
	private String groupName;
	private int channelNum;
	private String logourl;
	private List<DuduPosition> activeUsers;

	public Groupbean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getChannelNum() {
		return channelNum;
	}

	public void setChannelNum(int channelNum) {
		this.channelNum = channelNum;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public List<DuduPosition> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<DuduPosition> activeUsers) {
		this.activeUsers = activeUsers;
	}

	

}

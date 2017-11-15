package com.czl.chatClient.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouxue on 2016/7/27.
 * Company czl_zva
 */
public class ChannalActiveusers implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<DuduPosition> activeUsers;
    private String channelNum;
    private String channelId;
    private String channelName;

    public ChannalActiveusers() {
    }

    public String getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(String channelNum) {
        this.channelNum = channelNum;
    }

    public List<DuduPosition> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(List<DuduPosition> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
    
}

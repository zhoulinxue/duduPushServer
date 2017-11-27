package com.czl.chatServer.bean;

import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.utils.StringUtils;

public class ChannelMember {
	private String channelid;
	private String memberid;
	private String logourl = "";
	private String nameinchannel = "";
	private int rank = 0;
	private String createtime = "";
	
	public ChannelMember() {
		super();
	}
	public ChannelMember(String memberid) {
		super();
		this.memberid = memberid;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	public String getNameinchannel() {
		return nameinchannel;
	}
	public void setNameinchannel(String nameinchannel) {
		this.nameinchannel = nameinchannel;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getLogourl() {
		return logourl;
	}
	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}
	
	public DuduUser toUser(){
	    DuduUser user=new DuduUser();
		user.setUserid(getMemberid());
		return user;
	}
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		if(!(arg0 instanceof ChannelMember)){
			return false;
		}
		if(arg0==null||StringUtils.isEmpty(((ChannelMember)arg0).getMemberid())){
			return false;
		}
		if(((ChannelMember)arg0).getMemberid().equals(getMemberid())){
			return true;
		}
		return super.equals(arg0);
	}
}


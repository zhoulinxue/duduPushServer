package com.czl.chatClient.bean;

import java.io.Serializable;

public class Trafficebean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double longitude;
	private double latitude;
	private String city;
	private String nextRoad;
	private String currentRoad;
	private int direction;
	private String tag;
	private String distance = "0";
	private String url;
	private long createtime;


	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getNextRoad() {
		return nextRoad;
	}
	public void setNextRoad(String nextRoad) {
		this.nextRoad = nextRoad;
	}
	public String getCurrentRoad() {
		return currentRoad;
	}
	public void setCurrentRoad(String currentRoad) {
		this.currentRoad = currentRoad;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
}

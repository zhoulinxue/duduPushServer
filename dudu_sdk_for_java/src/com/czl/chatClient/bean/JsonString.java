package com.czl.chatClient.bean;

public class JsonString {
	private StringBuilder builder = new StringBuilder();

	public JsonString() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JsonString(String content) {
		super();
		builder.append(content);
	}

	public StringBuilder getBuilder() {
		return builder;
	}

	public String getContent() {
		return builder.toString();
	}

}

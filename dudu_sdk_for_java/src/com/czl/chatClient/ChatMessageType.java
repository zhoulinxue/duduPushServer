package com.czl.chatClient;

import com.czl.chatClient.utils.StringUtils;

public enum ChatMessageType {

	NOMAL("Text"), FILE("file"), VOICE("voice"), PICTRUE("pictrue");
	private String typeString;

	private ChatMessageType(String typeString) {
		this.typeString = typeString;
	}

	public final String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public static ChatMessageType ofCommand(String header) {
		// TODO Auto-generated method stub
		if (!StringUtils.isEmpty(header)) {
			for (ChatMessageType s : values()) {
				if (header.equals((s.getTypeString()))) {
					return s;
				}
			}
		}
		return null;
	}

}

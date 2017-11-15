package com.czl.chatClient.bean;

import com.czl.chatClient.ChatMessageType;

public class ChatMessage {
	private String text;
	private byte[] bytes;
	private ChatMessageType type;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public ChatMessageType getType() {
		return type;
	}

	public void setType(ChatMessageType type) {
		this.type = type;
	}

}

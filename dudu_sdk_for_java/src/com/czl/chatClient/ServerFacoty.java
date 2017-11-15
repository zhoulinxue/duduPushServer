package com.czl.chatClient;

import com.czl.chatClient.sender.UserServer;
import com.czl.chatClient.sender.UserServerImpl;

public class ServerFacoty {
	private static ServerFacoty self;
	private UserServer userserver;
	private DuduClient client;

	public void init() {
		getUserserver();
		getClient();
	}

	public static ServerFacoty getInstance() {
		if (self == null) {
			self = new ServerFacoty();
		}
		return self;
	}

	public UserServer getUserserver() {
		if (userserver == null) {
			userserver = new UserServerImpl();
		}
		return userserver;
	}

	public DuduClient getClient() {
		// TODO Auto-generated method stub
		if (client == null) {
			client = DuduClient.getInstance();
		}
		return client;
	}

}

package com.czl.chatServer;

public enum UserStatus {	
		ON_LINE_P2P_CHATTING(11,"ON_LINE_P2P_CHATTING"),// 正在一对一对讲
		OFF_LINE_P2P_CHATTING(10,"OFF_LINE_P2P_CHATTING"),// 一对一对讲中 ,但是掉线了
		KINCK_P2P_CHATTING(12,"离线时间太长,对讲已挂断"),// 被移除一对一对讲
		ON_LINE_GROUP_CHATTING(13,"ON_LINE_GROUP_CHATTING"),//正在群对讲
	    OFF_LINE_GROUP_CHATTING(14,"OFF_LINE_GROUP_CHATTING"),// 正在群对讲,但是掉线了
	    KINCK_GROUP_CHATTING(15,"离线时间太长,您已离开频道"),// 被移除 群对讲
	    OFF_LINE(1,"OFF_LINE"),// 离线
	    SLIENCE(2,"ON_LINE"),// 空闲状态
	    CALLING_USER(3,"calling_user"),//正在呼叫别人
	    ;
		private String message;
		private int code;
		private boolean isDebug = false;
		// private boolean isDebug = true;

		private UserStatus(int code, String message) {
			this.message = message;
			this.code = code;
		}

		public String getMessage() {
			return this.message;
		}

		public int getCode() {
			return this.code;
		}

		public String toInfo(String type) {
			if (isDebug) {
				return "|" + this.code + "|" + this.message + "," + type + "\n";
			} else {
				return "|" + this.code + "|" + this.message + "\n";
			}
		}

		public void setMessage(String message) {
			this.message = message;
		}

}

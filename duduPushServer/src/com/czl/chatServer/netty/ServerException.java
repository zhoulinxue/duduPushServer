package com.czl.chatServer.netty;

public enum ServerException {
	MS_NO_FREEPORT(10, "MS没有空闲port"),
	CONFIG_ERROR(20, "配置文件config.json异常"), 
	LISTENING_FAILURE(30,"服务端建立监听失败"), 
	REDIS_ERROR(40, "Redis 异常"), 
	NS_NOEXIST(50, "没有可使用语音服务器"), 
	NSPORT_NOEXIST(60,"没有可使用SS服务器PORT"), 
	PROTOCOL_NOEXIST(70, "协议错误"), 
	SERVER_READER_IDLE(71,"服务器读取数据超时,将断开连接"), 
	SERVER_EXCEPTION(72, "服务器异常"), 
	SERVER_TRANS_EXCEPTION(73, "服务器转发消息超时"),

	FRIEND_ON_LINE(0, "在线"),
	FRIEND_OUTLINE(1, "用户不在线"),
	FRIEND_OFFLINE(2, "对方网络不稳定或意外终止了"),

	NO_LOGIN(81, "没有登录"), 
	NOT_ON_SAME_NS(82, "好友不在同一个ns服务器"), 
	CHAT_FAILED(83, "对讲失败,对方未收到此消息"),
	FRIEND_CANCEL(90,"对方已经取消与您的对讲或者你没在对讲中"),

//	CHANNEL_NOPERSON(91, "频道内无其他人"), 
	CHANNEL_OUT(92, "您已经退出频道对讲"), 
	NO_ONE_ONLINE(93, "频道内的人员都不在线"), 
	ERROR_OBJECT(94, "解析json消息错误"),
	BYTE_DECODE_ERROR(95, "字节转换错误"), 
	NOT_IN_CHANNEL(96, "你不在改频道内"), 
	OFF_LINE(100,"您的账号在其他手机登录,请重新登录?"),
	REFRESH_DATA(110,"有信息需要同步"),
	IM_EXCEPTION(77, "发送失败"), ERROR_LONG(78,"未登陆用户不能进频道"),
	ERROR_CHANNEL(79,"频道信息错误");
	private String message;
	private int code;
	private boolean isDebug = false;
	// private boolean isDebug = true;

	private ServerException(int code, String message) {
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
			return  this.code + "|" + this.message + "," + type + "\n";
		} else {
			return   this.code + "|" + this.message + "\n";
		}
	}
	
	public String toInfo() {
			return   this.code + "|" + this.message ;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

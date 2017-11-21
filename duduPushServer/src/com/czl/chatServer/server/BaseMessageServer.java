package com.czl.chatServer.server;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.NettyMessage;

import io.netty.channel.Channel;

public interface BaseMessageServer {
	//获取 消息头
	 public String buildHead(NettyMessage msg);
	 //组件 消息对象
	 public NettyMessage buildMessage(AppServerType header,String content) throws Exception;
	 public NettyMessage buildMessage(AppServerType header) throws Exception;
	 // 信息结束BUG
	 public String endTag();
	 // 对象转str
	 public StringBuilder ObjectToString(Object object) throws Exception;
	 //分隔符
	 public String seporate() throws Exception;
	 //获取用户名字和端口
	 public String[] getCurrentIpAndPort(Channel ctx);
	 //获取内容的byte[]
	 public byte[] getContentByte(String content)throws Exception;
	 //json 转对象
	 public Object jsonToObJect(String json,Class calss) throws Exception;
	 // 发送异常消息
	 public NettyMessage sendEX(String content)throws Exception;
	// 发送消息
	 public void sendMessage(NettyMessage message,Channel ctx) throws Exception;
	 //收条
	 public NettyMessage send00Back(String str,Channel ctx)throws Exception;
	 // 服务器转发
	 public NettyMessage sendtoOtherNsData(String ip,  final NettyMessage mymsg) throws Exception;
	 // 获取 消息 长度
	 public String[] paserNettyMsg(NettyMessage msg)throws Exception;
	 //关闭 通道
	 public void  closeSocket(Channel ctx, String errorMsg);
	 //回复 客户端
     public void  responeClient(Channel ctx,NettyMessage msg)throws UnsupportedEncodingException;
     //回复 客户端
     public boolean  formatMessage(String[] data);

     
}

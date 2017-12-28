package com.czl.chatClient.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.Constants;
import com.czl.chatClient.utils.Log;
import com.czl.chatClient.utils.StringUtils;

public final class NettyMessage implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private byte header0;
	private byte header1;
	private int ctxLength;
	private byte[] content;
	private byte[] fromUerId;
	private byte[] GtOrEtmsg;
	private byte[] messageId;
	private String[] data;
	private NettyContent conobj;

	public NettyMessage() {
	}

	public NettyMessage(AppServerType header) {
		String head = header.toString();
		try {
			setHeader0(head.getBytes(Constants.HEAD_CHAR_SET)[0]);
			setHeader1(head.getBytes(Constants.HEAD_CHAR_SET)[1]);
			setMessageId(StringUtils.getRandomMsgId());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public byte[] getFromUerId() {
		return fromUerId;
	}

	public void setFromUerId(byte[] fromUerId) {
		this.fromUerId = fromUerId;
	}

	public byte getHeader0() {
		return header0;
	}

	public void setHeader0(byte header0) {
		this.header0 = header0;
	}

	public byte getHeader1() {
		return header1;
	}

	public void setHeader1(byte header1) {
		this.header1 = header1;
	}

	public int getCtxLength() {
		return ctxLength;
	}

	public void setCtxLength(int ctxLength) {
		this.ctxLength = ctxLength;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public byte[] getGtOrEtmsg() {
		return GtOrEtmsg;
	}

	public void setGtOrEtmsg(byte[] gtOrEtmsg) {
		GtOrEtmsg = gtOrEtmsg;
	}

	public String getCtxUTF8String() {

		try {
			String contentStr= new String(getContent(), Constants.CONTENT_CHAR_SET);
			return contentStr ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public byte[] getMessageId() {
		return messageId;
	}

	public void setMessageId(byte[] messageId) {
		this.messageId = messageId;
	}

	public byte[] getCtxUTF8String2() {
		byte[] dest = new byte[content.length - 2];
		System.arraycopy(content, 2, dest, 0, content.length - 2);
		return dest;
	}

	public String getHeader() {
		return buildHead(header0, header1);
	}

	public String buildHead(byte head0, byte head1) {
		StringBuffer buffer = new StringBuffer();
		buffer.append((char) head0);
		buffer.append((char) head1);
		return buffer.toString();
	}

	public String getStringMessageId() {
		if (messageId == null) {
			return "";
		}
		try {
			return new String(messageId, Constants.CONTENT_CHAR_SET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public AppServerType getAppServerType() {
		return AppServerType.ofCommand(getHeader());
	}

	public String getStringFromUerId() {
		// TODO Auto-generated method stub
		if (fromUerId == null) {
			return "";
		}
		try {
			return new String(fromUerId, Constants.CONTENT_CHAR_SET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	public void setHeader(AppServerType p2pChatByte) {
		// TODO Auto-generated method stub
		setHeader(p2pChatByte.toString());
	}

	public void setHeader(String header) {
		// TODO Auto-generated method stub
		try {
			setHeader0(header.getBytes(Constants.HEAD_CHAR_SET)[0]);
			setHeader1(header.getBytes(Constants.HEAD_CHAR_SET)[1]);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getUserDataFromMsg()
	{
		// TODO Auto-generated method stub
		if(data!=null){
			return data;
		}
		try
		{
			data=(new String(getContent(), Constants.CONTENT_CHAR_SET))
					.split("\\|");
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	public NettyContent getConobj()
	{
		if (conobj == null)
		{
			try
			{
				String mesgStr= new String(getContent(), Constants.CONTENT_CHAR_SET);
				Log.e(mesgStr);
				conobj = JSONObject.parseObject(mesgStr, NettyContent.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		return conobj;
	}

	public void setConobj(NettyContent conobj) {
		this.conobj = conobj;
	}
	public byte[] getbyteConobj()
	{
		// TODO Auto-generated method stub
		NettyContent content = getConobj();
		JsonString jsonStr=new JsonString( JSONObject.toJSONString(content));
		return StringUtils.tobyte(jsonStr.getFinalMessage());
	}
	public void setConobj(byte[] nettyContent)
	{
		// TODO Auto-generated method stub
		try
		{
			if (nettyContent == null)
			{
				return;
			}
			String nettyCon = StringUtils.toString(nettyContent);
			if (!StringUtils.isEmpty(nettyCon))
			{
				conobj = JSONObject.parseObject(nettyCon, NettyContent.class);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String[] getFormatStrings() {
		String[] data=getCtxUTF8String().split("\\|");
		String[] newdata=new String[data.length];
		for(int i=0;i<data.length;i++){
			newdata[i]=returnFormat(data[i]);
		}
		return newdata;
	}

	public String getFormatString() {
		return StringUtils.formatString(getCtxUTF8String());
	}

	private String returnFormat(String datum) {
		return StringUtils.returnFormat(datum);
	}
}

package com.czl.chatClient.bean;

import com.czl.chatClient.Constants;
import com.czl.chatClient.utils.StringUtils;

public class JsonString {
	private StringBuilder builder = new StringBuilder();

	public JsonString() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JsonString(String content) {
		super();
		append(content);
	}

	public JsonString getBuilder() {
		return this;
	}

	public String getContent() {
		return builder.toString();
	}

	public JsonString append(String sting){
		if(!StringUtils.isEmpty(sting)&&!Constants.SEPORATE.equals(sting)){
			String formatString=StringUtils.getFormatString(sting, Constants.SEPORATE,Constants.REPLACE_SEPORATE_TAG);
			builder.append(StringUtils.getFormatString(formatString,Constants.MESSAFE_END_TAG,Constants.REPLACE_END_TAG));
		}else if(Constants.SEPORATE.equals(sting)){
			builder.append(sting);
		}
		return  this;
	}

	public String getMessage() {
		return builder.toString();
	}
}

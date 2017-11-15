package com.czl.chatClient.sender;

import java.io.UnsupportedEncodingException;

import com.czl.chatClient.bean.JsonString;

public interface JsonParser {
	// 对象转str
	public String toJSONString(Object object) throws UnsupportedEncodingException;

	//
	public JsonString ObjectToJsonString(Object object) throws UnsupportedEncodingException;

	//
	public Object parseObject(String json, Class<?> clazz) throws UnsupportedEncodingException;

	// 解析集合
	public Object parseArray(String json, Class<?> clazz) throws UnsupportedEncodingException;

	// 解析集合
	public Object jsonStringParseArray(JsonString json, Class<?> clazz) throws UnsupportedEncodingException;

	// json 转对象
	public Object jsonStringToObJect(JsonString json, Class<?> clazz) throws UnsupportedEncodingException;
}

package com.czl.chatClient;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.bean.JsonString;
import com.czl.chatClient.sender.JsonParser;

public class FastJsonPaser implements JsonParser {

	@Override
	public String toJSONString(Object object) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return JSONObject.toJSONString(object);
	}

	@Override
	public JsonString ObjectToJsonString(Object object) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return new JsonString(toJSONString(object));
	}

	@Override
	public Object parseObject(String json, Class<?> clazz) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return JSONObject.parseObject(json, clazz);
	}

	@Override
	public Object jsonStringToObJect(JsonString json, Class<?> clazz) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return parseObject(json.getContent(), clazz);
	}

	@Override
	public Object parseArray(String json, Class<?> clazz) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return JSONObject.parseArray(json, clazz);
	}

	@Override
	public Object jsonStringParseArray(JsonString json, Class<?> clazz) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return jsonStringToObJect(json, clazz);
	}

}

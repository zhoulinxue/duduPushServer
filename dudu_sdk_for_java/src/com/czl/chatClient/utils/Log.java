package com.czl.chatClient.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.Constants;
import com.czl.chatClient.DuduSDK;
import com.czl.chatClient.bean.NettyContent;
import com.czl.chatClient.bean.NettyMessage;

public class Log
{
    public static void error(String error)
    {
        System.err.println(new Date().toString() + "|" + error);
    }
    
    public static void printeNettymsg(NettyMessage msg, String tag)
    {
        try
        {
            String uid = "";
            String content = msg.getFormatString();
            if (msg.getFromUerId() != null)
            {
                uid = new String(msg.getFromUerId(),
                        Constants.CONTENT_CHAR_SET);
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append((char) msg.getHeader0());
            buffer.append((char) msg.getHeader1());
            if (!(msg.getHeader0() == 83 && msg.getHeader1() == 77)
                    && !(msg.getHeader0() == 83 && msg.getHeader1() == 71)&&!AppServerType.AU.toString().equals(msg.getHeader()))
            {
                e(DateUtils.timeFormatFull(System.currentTimeMillis()) + tag
                        + uid + "    header:=" + msg.getHeader() + "    ID:"
                        + msg.getStringMessageId() + "    content=" + content);
            }
            else
            {
                e(DateUtils.timeFormatFull(System.currentTimeMillis()) + tag
                        + uid + "header:=" + msg.getHeader() + "    ID:"
                        + msg.getStringMessageId() + "语音消息");
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    private static String getConObj(NettyMessage msg) {
        NettyContent content=msg.getConobj();
        if(content!=null)
        return JSONObject.toJSONString(msg.getConobj());
        else
            return "";
    }

    public static void e(String tag, String msg)
    {
        if(StringUtils.isEmpty(tag)){
            tag="Dudu_SDK";
        }
        if (DuduSDK.isDebug)
            android.util.Log.e(tag, msg);
    }
    
    public static void e(String msg)
    {
        if (DuduSDK.isDebug)
            e("Dudu_SDK", msg);
    }
    
}

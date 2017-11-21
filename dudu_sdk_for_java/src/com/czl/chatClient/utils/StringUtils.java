package com.czl.chatClient.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import com.czl.chatClient.Constants;

public class StringUtils
{
    
    public static boolean isEmpty(CharSequence str)
    {
        // TODO Auto-generated method stub
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
    
    public static byte[] getRandomMsgId()
    {
        // TODO Auto-generated method stub
        String msgId = getRandomString(6);
        try
        {
            return msgId.getBytes(Constants.CONTENT_CHAR_SET);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getRandomString(int length)
    { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    
    public static byte[] tobyte(String str)
    {
        // TODO Auto-generated method stub
        if (str != null)
        {
            try
            {
                return str.getBytes(Constants.CONTENT_CHAR_SET);
            }
            catch (UnsupportedEncodingException e)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    /**
    * 转换为字节数组
    * @param str
    * @return
    */
    public static String toString(byte[] bytes)
    {
        try
        {
            return new String(bytes, Constants.CONTENT_CHAR_SET);
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }
    
}

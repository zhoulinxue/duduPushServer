package com.czl.chatClient.receiver;


import java.io.UnsupportedEncodingException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.AppServerType;
import com.czl.chatClient.Constants;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyContent;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.bean.Pushmessage;
import com.czl.chatClient.bean.Responbean;
import com.czl.chatClient.sender.JsonParser;
import com.czl.chatClient.utils.Log;

import io.netty.channel.Channel;


public abstract class ZVAHandler implements ZVALisenter{


    @Override
    public List<AppServerType> getServerType()
    {
        // TODO Auto-generated method stub
        return AppServerType.zvaValues();
    }

    @Override
    public void onRecivMessage(Channel ctx, NettyMessage message, String tag,
                               JsonParser parser) throws  UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        switch (message.getAppServerType())
        {
            case FM:
                NettyContent content;
                try
                {
                    content = (NettyContent) parser.parseObject(message.getCtxUTF8String(), NettyContent.class);
                    DuduUser fromUser = content.getFrom();
                    String fmMsg = content.getContent();
                    Log.e("Dudu_SDK", "onReceiveFMMessage______FM__");
                    onReciveMessage(fromUser, fmMsg);
                }
                catch (UnsupportedEncodingException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case AU:
                DuduUser from = message.getConobj().getFrom();
                byte[] bytes = message.getContent();
                String discritionMsg = message.getConobj().getContent();
                onReciveAudioMessage(from, bytes, discritionMsg);
                break;
            case RS:
                String data;
                try
                {
                    data = new String(message.getContent(), "UTF-8");
                    String[] splits = data.split("\\|");
                    Responbean responbean = (Responbean) parser
                            .parseObject(splits[0], Responbean.class);
                    onReceiveRespons(responbean);
                }
                catch (UnsupportedEncodingException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;
            case RETURN_TAG:
                String datar;
                    datar = new String(message.getContent(), "UTF-8");
                    String[] splits = datar.split("\\|");
                    onServerRecive(splits[0]);

                break;
            case PU:
                String pudata = new String(message.getContent(), Constants.CONTENT_CHAR_SET);
                String[] pusplits = pudata.split("\\|");
                List<Pushmessage> lists = JSONObject.parseArray(pusplits[0], Pushmessage.class);
                Log.e("Dudu_SDK","DefaultHanlder_______onReceiveOfflineMessage__");
                onReceiveOfflineMessage(lists);
                break;

            case IM:
                String imdata = new String(message.getContent(), Constants.CONTENT_CHAR_SET);
                String[] imsplits = imdata.split("\\|");
                Pushmessage Impmsg = (Pushmessage) parser.parseObject(imsplits[2],
                        Pushmessage.class);
                Log.e("Dudu_SDK","DefaultHanlder_______onReceiveIMMsg__");
                onReceivePushMessage(Impmsg);
                break;
            default:
                break;
        }
    }
}

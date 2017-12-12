package com.czl.chatClient.receiver;

import com.czl.chatClient.bean.DuduUser;

import com.czl.chatClient.bean.Pushmessage;
import com.czl.chatClient.bean.Responbean;

import java.util.List;


public interface ZVALisenter extends RecivMessageCallBack
{
    
    public void onServerRecive(String string);
    
    public void onReceiveRespons(Responbean responbean);
    
    public void onReciveAudioMessage(DuduUser from, byte[] bytes,
            String discritionMsg);
    
    public void onReciveMessage(DuduUser fromUser, String fmMsg);

    public void onReceiveOfflineMessage(List<Pushmessage> lists);

    public void onReceivePushMessage(Pushmessage Impmsg);
    
}

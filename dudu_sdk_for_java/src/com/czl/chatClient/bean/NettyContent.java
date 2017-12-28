package com.czl.chatClient.bean;

import com.czl.chatClient.utils.StringUtils;

import java.util.List;

public class NettyContent
{
    private DuduUser from;
    private List<DuduUser> toUser;
    private String content;
    private long msgTime;
    private boolean saveToCache=false;

    public NettyContent() {
    }

    public DuduUser getFrom() {
        return from;
    }

    public void setFrom(DuduUser from) {
        this.from = from;
    }

    public List<DuduUser> getToUser() {
        return toUser;
    }

    public void setToUser(List<DuduUser> toUser) {
        this.toUser = toUser;
    }

    public String getContent() {
        return StringUtils.returnFormat(content);
    }

    public void setContent(String content) {

        this.content = StringUtils.formatString(content);
    }

    public long getMsgTime()
    {
        return msgTime;
    }

    public void setMsgTime(long msgTime)
    {
        this.msgTime = msgTime;
    }

    public boolean isSaveToCache()
    {
        return saveToCache;
    }

    public void setSaveToCache(boolean saveToCache)
    {
        this.saveToCache = saveToCache;
    }
    
}

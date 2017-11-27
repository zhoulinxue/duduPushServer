package com.czl.chatServer.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import com.alibaba.fastjson.JSONObject;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatServer.Constants;

/**
 * Created by zhouxue on 2016/10/14.
 * Company czl_zva
 */

public class Imbean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2890094227217060295L;
	private String alert;
   private String channelid;
    private String dataid;
    private String detail;
    private String fromid;
    private String fromlogourl;
    private String fromname;
    private String sendtime;
    private String toid;
    private String type;
    private String title;
    private int status;
    private String markid;
    private Timestamp updatetime;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getFromlogourl() {
        return fromlogourl;
    }

    public void setFromlogourl(String fromlogourl) {
        this.fromlogourl = fromlogourl;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }


 

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }
   

    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	

	public String getMarkid() {
		return markid;
	}

	public void setMarkid(String markid) {
		this.markid = markid;
	}
	



	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	@Override
    public String toString() {
        return "Imbean{" +
                "alert='" + alert + '\'' +
                ", channelid='" + channelid + '\'' +
                ", dataid='" + dataid + '\'' +
                ", detail='" + detail + '\'' +
                ", fromid='" + fromid + '\'' +
                ", fromlogourl='" + fromlogourl + '\'' +
                ", fromname='" + fromname + '\'' +
                ", sendtime='" + sendtime + '\'' +
                ", toid='" + toid + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
//	public NettyMessage toNettyMessage(){
//		NettyMessage netty=new NettyMessage((byte)73, (byte)77);
//			try {
//				netty.setContent(("|"+getType()+"|"+fromid+"|"+JSONObject.toJSONString(this)+"\n").getBytes(Constants.CONTENT_CHAR_SET));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		return netty;
//	}
//	
	public String buildContent(){
		return ("IM|"+getType()+"|"+fromid+"|"+JSONObject.toJSONString(this));
	}

}

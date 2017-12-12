package com.czl.chatClient.bean;

import com.czl.chatClient.utils.StringUtils;

public class DuduUser {
	private String userid;
	private String username;
	private String token;
	private String url;
	private String diviceid;
	private String extra;
	private String ip;
	private int port;
	private String loginTime;
    private String logourl;
	

	public DuduUser()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public DuduUser(String userid)
    {
        super();
        this.userid = userid;
    }

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {
	    this.logourl=url;
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		this.logourl=url;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DuduUser) {
			DuduUser uobj = (DuduUser) obj;
			return getUserid().equals(uobj.getUserid());
		}
		return super.equals(obj);
	}

	public String getDiviceid() {
		return diviceid;
	}

	public void setDiviceid(String diviceid) {
		this.diviceid = diviceid;
	}
	@Override
    public String toString()
    {
        return "DuduUser [userid=" + userid + ", username=" + username
                + ", token=" + token + ", url=" + url + ", diviceid=" + diviceid
                + ", extra=" + extra + ", ip=" + ip + ", port=" + port + "]";
    }

    public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(String loginTime)
    {
        this.loginTime = loginTime;
    }

    public String getLogourl()
    {
        if(StringUtils.isEmpty(logourl)){
            return getUrl();
        }
        return logourl;
    }

    public void setLogourl(String logourl)
    {
        this.logourl = logourl;
        this.url=logourl;
    }
	
}

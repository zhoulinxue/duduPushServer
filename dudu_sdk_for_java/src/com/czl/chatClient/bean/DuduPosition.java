package com.czl.chatClient.bean;

/**
 * Created by zhouxue on 2016/8/4. Company czl_zva
 */
public class DuduPosition extends DuduUser{

	private double x;
	private double y;

	public DuduPosition()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public DuduPosition(String uid)
    {
        // TODO Auto-generated constructor stub
	    setUserid(uid);
    }

    public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	};

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return super.equals(o);
		}
		return getUserid().equals(((DuduPosition) o).getUserid());
	}

    public DuduPosition clean()
    {
        // TODO Auto-generated method stub
        DuduPosition user=new DuduPosition();
        user.setUrl(getUrl());
        user.setUserid(getUserid());
        user.setUsername(getUsername());
        user.setX(getX());
        user.setY(getY());
        return user;
    }
   
}

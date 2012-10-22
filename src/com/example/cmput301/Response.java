package com.example.cmput301;

import java.util.Date;

abstract class Response
{
	int rid;
	Date timestamp;
	
	public int getRid()
	{
		return this.rid;
	}
	
	public void setRid(int rid)
	{
		this.rid = rid;
	}
	
	public Date getTimestamp()
	{
		return this.timestamp;
	}
	
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}
	
}


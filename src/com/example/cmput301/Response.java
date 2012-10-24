package com.example.cmput301;

import java.util.Date;

abstract class Response
{
	String rid;
	Date timestamp;
	
	public String getRid()
	{
		return this.rid;
	}
	
	public void setRid(String rid)
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


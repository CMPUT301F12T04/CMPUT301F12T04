package com.example.cmput301;

import java.util.Date;

public abstract class Response
{
	Date timestamp;
	
	public Date getTimestamp()
	{
		return this.timestamp;
	}
	
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}
	
}


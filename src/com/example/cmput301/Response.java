package com.example.cmput301;

import java.util.Date;

public abstract class Response
{
	Date timestamp;
	String annotation;
	
	public String getAnnotation()
	{
		return this.annotation;
	}
	
	public void setAnnotation(String annotation)
	{
		this.annotation = annotation;
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


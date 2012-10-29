package com.example.cmput301;

import java.util.Date;

public class TextResponse extends Response
{
	String content;

	public TextResponse(String content, Date timestamp)
	{
		super(null,timestamp);
		this.content = content;
	}
	
	public String getContent()
	{
		return this.content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
}

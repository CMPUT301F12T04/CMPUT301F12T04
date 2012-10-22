package com.example.cmput301;

import java.util.Date;

public class TextResponse extends Response
{
	String text;

	public TextResponse(String text, Date timestamp)
	{
		super.setTimestamp(timestamp);
		this.text = text;
	}
	
	public String getText()
	{
		return this.text;
	}
}

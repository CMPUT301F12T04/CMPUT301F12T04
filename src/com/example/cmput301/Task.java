package com.example.cmput301;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task
{
	private List<Response> responses;
	private String name;
	private String description;
	private String tid;
	private Date timestamp;
	private Class<Response> type;
	
	public Task(String name, String description, Date timestamp)
	{
		this.name = name;
		this.description = description;
		this.timestamp = timestamp;
		responses = new ArrayList<Response>();
	}
	
	public void addResponse(Response response)
	{
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	public void Update()
	{
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	// getters and setters
	public List<Response> getResponses()
	{
		return this.responses;
	}
	public void setTid(String tid)
	{
		this.tid = tid;
	}	
	public Class<Response> getType()
	{
		return this.type;
	}
	public Date getTimestamp()
	{
		return this.timestamp;
	}
	public String getTid()
	{
		return this.tid;
	}
	public String getName()
	{
		return this.name;
	}	
	public String getDescription()
	{
		return this.description;
	}
}

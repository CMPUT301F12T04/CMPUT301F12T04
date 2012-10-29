package com.example.cmput301;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task
{
	private List<Response> responses;
	
	private String name;
	private String description;
	private String id;
	private Date timestamp;
	private Class<Response> type;
	
	public Task(String name, String description)
	{
		this.name = name;
		this.description = description;
		responses = new ArrayList<Response>();
	}
	
	public Task(String name, String description, String id)
	{
		this.name = name;
		this.description = description;
		this.id = id;
		responses = new ArrayList<Response>();
	}

	public void addResponse(Response response)
	{
		this.responses.add(response);
	}
	public void addResponse(TextResponse response)
	{
		this.responses.add(response);
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
	public void setId(String id)
	{
		this.id = id;
	}	
	public Class<Response> getType()
	{
		return this.type;
	}
	public Date getTimestamp()
	{
		return this.timestamp;
	}
	public String getId()
	{
		return this.id;
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

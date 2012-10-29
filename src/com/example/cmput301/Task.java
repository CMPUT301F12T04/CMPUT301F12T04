package com.example.cmput301;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task
{
	public static final int OWNER_LOCAL = 1;
	public static final int OWNER_REMOTE = 2;
	private List<Response> responses;

	private String name;
	private String description;
	private String id;
	private Date timestamp;
	private Class<Response> type;
	private int owner;

	public Task(String name, String description)
	{
		this.name = name;
		this.description = description;
		responses = new ArrayList<Response>();
		this.owner = Task.OWNER_LOCAL;
	}

	public Task(String name, String description, String id)
	{
		this.name = name;
		this.description = description;
		this.id = id;
		responses = new ArrayList<Response>();
		this.owner = Task.OWNER_LOCAL;
	}

	public Task(String name, String description, int owner)
	{
		this.name = name;
		this.description = description;
		responses = new ArrayList<Response>();
		this.owner = owner;
	}

	public Task(String name, String description, String id, int owner)
	{
		this.name = name;
		this.description = description;
		this.id = id;
		responses = new ArrayList<Response>();
		this.owner = owner;
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
	public void setOwner(int owner) 
	{
		this.owner = owner;
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

	public int getOwner() 
	{
		return this.owner;
	}
}
package com.example.crowdsource;

import java.util.Date;
import java.util.List;

public class Task
{
	private List<Response> responses;
	
	public Task(String name, String description, Date timestamp)
	{
		
	}
	
	public void addResponse(Response response)
	{
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	public void Update()
	{
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	public List<Response> getResponses()
	{
		return this.responses;
	}

}

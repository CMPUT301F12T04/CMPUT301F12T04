/*******************************************************************************
 * Copyright (c) 2012 Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu - initial API and                              
 *     implementation
 ******************************************************************************/
package com.example.cmput301.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cmput301.model.response.Response;

/**
 * This is the model of a basic "Task" which the user wanted fulfilled.
 */
@SuppressWarnings("serial")
public class Task implements Comparable<Object>, Serializable, Cloneable
{

	public static final int STATUS_PRIVATE = 1;
	public static final int STATUS_SHARED = 2;
	private List<Response> responses;
	private String name;
	private String description;
	private String id;
	private String type;
	private int status;
	private int votes = 0;

	/**
	 * Constructor for a new task object with the following properties. Type is
	 * automatically set to TextResponse type.
	 * 
	 * @param name
	 * @param description
	 * @param id
	 * @param status
	 * @param responses
	 */
	public Task(String name, String description, String id, int status,
			List<Response> responses, String type, int votes)
	{
		this.name = name;
		this.description = description;
		this.id = id;
		this.status = status;
		this.responses = responses;
		this.type = type;
		this.votes = votes;
	}

	/**
	 * Constructor for a new task object with the following properties. Id is
	 * set to null, status is Task.STATUS_PRIVATE, type is TextResponse, and
	 * responses is an empty list.
	 * 
	 * @param name
	 * @param description
	 */
//	public Task(String name, String description)
//	{
//		this.name = name;
//		this.description = description;
//		responses = new ArrayList<Response>();
//		this.status = Task.STATUS_PRIVATE;
//		this.type = TextResponse.class.toString();
//		this.votes = 0;
//	}
	public Task(String name, String description,String type)
	{
		this.name = name;
		this.description = description;
		responses = new ArrayList<Response>();
		this.status = Task.STATUS_PRIVATE;
		this.type = type;
		this.votes = 0;
	}

	public Task(String name, String description, String id, String type)
	{
		this.name = name;
		this.description = description;
		this.id = id;
		this.type = type;
		this.responses = new ArrayList<Response>();
		this.type = type;
		this.votes = 0;
	}

	/**
	 * Add a new response to the list of responses.
	 * 
	 * @param response
	 */
	public void addResponse(Response response)
	{
		if(response.getClass().toString().equals(type)) {
			this.responses.add(response);
		}
	}

	// getters and setters
	public void setId(String id)
	{
		this.id = id;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public void increaseVotes()
	{
		this.votes++;
	}

	public String getType()
	{
		return this.type;
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

	public int getStatus()
	{
		return this.status;
	}

	public int getVotes()
	{
		return this.votes;
	}

	public List<Response> getResponses()
	{
		return this.responses;
	}

	@Override
	public boolean equals(Object another)
	{
		return this.compareTo(another) == 0;
	}

	@Override
	public Task clone()
	{
		String cloneid = null;
		if (this.id != null)
		{
			cloneid = this.id.substring(0);
		}
		Task clone = new Task(this.name.substring(0),
				this.description.substring(0), cloneid, this.status,
				new ArrayList<Response>(), this.type.substring(0), this.votes);
		// Fill in the clone with the response clones.
		for (Response resp : this.responses)
		{
			clone.addResponse(resp.clone());
		}

		return clone;

	}

	public int compareTo(Object another)
	{
		if (!(another instanceof Task))
		{
			return -1;
		}

		// If it was an instance of task then make it so.
		Task anotherTask = (Task) another;

		// Return the string comparison of their ids.
		return anotherTask.getId().compareTo(this.getId());
	}

	public JSONObject toJson() throws JSONException
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", getName());
		jsonObject.put("description", getDescription());
		jsonObject.put("id", getId());
		jsonObject.put("type", getType());
		jsonObject.put("status", getStatus());
		jsonObject.put("votes", getVotes());
		List<Response> responses = getResponses();
		JSONArray arr = new JSONArray();
		for (Response response : responses)
		{
			JSONObject jo = new JSONObject();
			jo.put("annotation", response.getAnnotation());
			jo.put("content", response.getSaveable());
			jo.put("timestamp", response.getTimestamp());
			arr.put(jo);
		}
		jsonObject.put("responses", arr);
		return jsonObject;
	}
}

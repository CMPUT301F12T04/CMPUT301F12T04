package com.example.cmput301;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amccann
 */
public class Task implements Comparable {

    public static final int STATUS_PRIVATE = 1;
    public static final int STATUS_SHARED = 2;
    private List<Response> responses;
    private String name;
    private String description;
    private String id;
    private Date timestamp;
    private Class<Response> type;
    private int status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        responses = new ArrayList<Response>();
        this.status = Task.STATUS_PRIVATE;
    }

    public Task(String name, String description, String id) {
        this.name = name;
        this.description = description;
        this.id = id;
        responses = new ArrayList<Response>();
        this.status = Task.STATUS_PRIVATE;
    }

    public Task(String name, String description, int status) {
        this.name = name;
        this.description = description;
        responses = new ArrayList<Response>();
        this.status = status;
    }

    public Task(String name, String description, String id, int status) {
        this.name = name;
        this.description = description;
        this.id = id;
        responses = new ArrayList<Response>();
        this.status = status;
    }

    public void addResponse(Response response) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void Update() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // getters and setters
    public List<Response> getResponses() {
        return this.responses;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Class<Response> getType() {
        return this.type;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getStatus() {
        return this.status;
    }

    public int compareTo(Object another) {
        if (!(another instanceof Task)) {
            return -1;
        } 
        
        //If it was an instance of task then make it so.
         Task anotherTask = (Task) another;
        
<<<<<<< HEAD
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
=======
         
         //Return the string comparison of their ids.
         return anotherTask.getId().compareTo(this.getId());
>>>>>>> df5fe3b7f414b871b0e565f5140663840aa4db92
        
    }
}

package com.example.cmput301;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amccann
 */
public class Task implements Comparable, Serializable {
    
    //All remote tasks MUST have a shared status.
    public static final int STATUS_PRIVATE = 1;
    public static final int STATUS_SHARED = 2;
    
    private List<Response> responses;
    private String name;
    private String description;
    private String id;
    private Date timestamp;
    private String type;
    private int status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        responses = new ArrayList<Response>();
        this.status = Task.STATUS_PRIVATE;
		this.type = TextResponse.class.toString();
    }

    /* CAN BE DELTED IMO */
//    public Task(String name, String description, String id) {
//        this.name = name;
//        this.description = description;
//        this.id = id;
//        responses = new ArrayList<Response>();
//        this.status = Task.STATUS_PRIVATE;
//		this.type = TextResponse.class.toString();
//    }

//    public Task(String name, String description, int status) {
//        this.name = name;
//        this.description = description;
//        responses = new ArrayList<Response>();
//        this.status = status;
//		this.type = TextResponse.class.toString();
//    }
//
//    public Task(String name, String description, String id, int status) {
//        this.name = name;
//        this.description = description;
//        this.id = id;
//        responses = new ArrayList<Response>();
//        this.status = status;
//		this.type = TextResponse.class.toString();
//    }

    public Task(String name, String description, String id, int status,
			List<Response> responses)
	{
		this.name = name;
		this.description = description;
		this.id = id;
		this.status = status;
		this.responses = responses;
		this.type = TextResponse.class.toString();
	}

	public Task(String name, String description, String id, String type)
	{
		this.name = name;
		this.description = description;
		this.id = id;
		this.type = type;
		this.responses = new ArrayList<Response>();
		this.type = TextResponse.class.toString();
	}

	public void addResponse(Response response) {
        this.responses.add(response);
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

    public String getType() {
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
        
         
         //Return the string comparison of their ids.
         return anotherTask.getId().compareTo(this.getId());
        
    }


}

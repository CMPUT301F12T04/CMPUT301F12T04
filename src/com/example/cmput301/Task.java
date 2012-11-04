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
    private String type;
    private int status;

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
            List<Response> responses) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.responses = responses;
        this.type = TextResponse.class.toString();
    }

    /**
     * Constructor for a new task object with the following properties. Id is
     * set to null, status is Task.STATUS_PRIVATE, type is TextResponse, and
     * responses is an empty list.
     *
     * @param name
     * @param description
     */
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        responses = new ArrayList<Response>();
        this.status = Task.STATUS_PRIVATE;
        this.type = TextResponse.class.toString();
    }

    /**
     * Add a new response to the list of responses.
     *
     * @param response
     */
    public void addResponse(Response response) {
        this.responses.add(response);
    }

    // getters and setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
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

    public List<Response> getResponses() {
        return this.responses;
    }

    @Override
    public boolean equals(Object another) {
        return this.compareTo(another) == 0;
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

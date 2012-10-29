package com.example.cmput301;

import java.util.List;

public class TaskManager {

    private List<Task> tasks;

    /**
     * Attaches task to the local database and returns the task which was added
     * along with it's id.
     * 
     * @param task The task you want to be added.
     * @return The task which was added with the local task id attached.
     */
    public Task addTask(Task task) {
        
        Task addedTask = DatabaseManager.post(task);
        
        return addedTask;
    }
    
    /**
     * Share the task with the given id.
     * @param id The id of the task you want shared.
     */
    public void shareTask(String id) {
        
        //Get the task from the database.
        Task localTask = DatabaseManager.getTask(id);
        
        //Post to the webservice.
        Task remoteTask = WebService.post(localTask);
        
        //Remove the local version of the task.
        DatabaseManager.deleteTask(localTask.getId());
        
        //Add the remote version of the task.
        remoteTask.setOwner(Task.OWNER_LOCAL);
        DatabaseManager.post(remoteTask);
        
    }
    
    public boolean deleteTask(String id) {
        
        WebService.deleteTask(id);
        
        return true;
    }

    public boolean update() {
        return true;
    }

    public boolean updateTask() {
        return true;
    }
}

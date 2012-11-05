package com.example.cmput301;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the entry point for the model of this application. It handles 
 * all the data storage on local and remote databases as well as the addition of
 * responses to tasks, and allows you to grab tasks which meet certain criteria
 */
public class TaskManager {

    private DatabaseManager dbman;

    /**
     * Constructor for the Task manager.
     *
     * @param context The android context that we will be saving the database
     * in.
     */
    public TaskManager(Context context) {
        this.dbman = new DatabaseManager("database_tables", context);
    }

    /**
     * Attaches task to the local database and returns the task which was added
     * along with it's id.
     *
     * @param task The task you want to be added.
     * @return The task which was added with the local task id attached.
     */
    public Task addTask(Task task) {

        Task addedTask = dbman.postLocal(task);

        return addedTask;
    }

    /**
     * Share the task with the given id.
     *
     * @param id The id of the task you want shared.
     */
    public Task shareTask(String id) {

        //Get the task from the database.
        Task localTask = dbman.getLocalTask(id);

        //Post to the webservice.
        Task remoteTask = WebService.put(localTask);

        //--
        //Since we are replacing the id we need to remove the old task and put in
        //a new one with an id generated by the webservice.
        //--

        //Remove the local version of the task.
        dbman.deleteLocalTask(localTask.getId());

        //Add the remote version of the task.
        remoteTask.setStatus(Task.STATUS_SHARED);
        dbman.postLocal(remoteTask);

        return remoteTask;

    }

    /**
     * Delete the task with the given id deletes from local tables only. You
     * cannot delete a remote user's task you can only delete you own.
     *
     * @param id The id of the task you want deleted
     * @return Whether or not the task existed and was deleted
     */
    public boolean deleteTask(String id) {

        //Check if the task is local
        Task local = dbman.getLocalTask(id);
        if (local != null) {

            WebService.delete(id);

            dbman.deleteLocalTask(id);

            this.Refresh();

            return true;
        }

        return false;

    }

    /**
     * Post a response to a task with the given id in the database.
     *
     * @param response The response you want to add
     * @param task The task you want the response added to.
     */
    public void postResponse(Task task, Response response) {

        //If its private dont update the webservice first.
        if (task.getStatus() == Task.STATUS_PRIVATE) {
            task.addResponse(response);
            dbman.updateTask(task);
        } else {
            Task updatedTask = WebService.post(task, response);
            dbman.updateTask(updatedTask);
        }
    }

    /**
     * Get a task from the local table of the database.
     *
     * @param id The id of the task you are requesting
     * @return The task you requested
     */
    public Task getLocalTask(String id) {
        return dbman.getLocalTask(id);
    }

    /**
     * Get a task from the remote table of the database.
     *
     * @param id The id of the task you are requesting
     * @return The task you requested
     */
    public Task getRemoteTask(String id) {
        return dbman.getRemoteTask(id);
    }

    /**
     * Get a list of all tasks in the local table of the database.
     *
     * @return
     */
    public ArrayList<Task> getLocalTasks() {
        return this.dbman.getLocalTaskList();
    }

    /**
     * Get a list of all tasks in the local table of the database which have the
     * status STATUS_PRIVATE
     *
     * @return
     */
    public ArrayList<Task> getPrivateTasks() {
        ArrayList<Task> privateList = new ArrayList<Task>();

        for (Task task : this.dbman.getLocalTaskList()) {
            if (task.getStatus() == Task.STATUS_PRIVATE) {
                privateList.add(task);
            }
        }

        return privateList;
    }

    /**
     * Get a list of all tasks in the local table of the database which have the
     * status STATUS_SHARED
     *
     * @return
     */
    public ArrayList<Task> getSharedTasks() {
        ArrayList<Task> sharedList = new ArrayList<Task>();

        for (Task task : this.dbman.getLocalTaskList()) {
            if (task.getStatus() == Task.STATUS_SHARED) {
                sharedList.add(task);
            }
        }

        return sharedList;
    }

    /**
     * Get a list of all tasks in the remote table of the database which have no
     * responses
     *
     * @return
     */
    public ArrayList<Task> getUnansweredTasks() {
        ArrayList<Task> unansweredList = new ArrayList<Task>();

        for (Task task : this.dbman.getRemoteTaskList()) {
            if (task.getResponses().size() == 0) {
                unansweredList.add(task);
            }
        }

        return unansweredList;
    }

    /**
     * Get a list of all tasks in the remote table of the database which have no
     * responses
     *
     * @return
     */
    public ArrayList<Task> getRemoteTasks() {
        return this.dbman.getRemoteTaskList();
    }

    /**
     * Look up the tasks on the webservice and update the database with the changes
     */
    public void Refresh() {
        //Not the best method but will work.
        this.dbman.nukeRemote();

        for (Task task : WebService.list()) {
            //This is inefficient but will have to work for now.
            this.dbman.updateTask(task);
            this.dbman.postRemote(task);
        }

    }

    /**
     * Nuke the databse.
     */
    public void nukeLocal() {
        this.dbman.nukeAll();
    }

    /**
     * Nuke the webservice's database.
     */
    public void nukeRemote() {
        WebService.nuke("judgedredd");
    }
}

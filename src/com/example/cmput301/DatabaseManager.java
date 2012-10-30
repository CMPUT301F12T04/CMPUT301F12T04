package com.example.cmput301;

public class DatabaseManager {

    /**
     * Post a task to the "local" table of the database.
     *
     * @param task The task to be added.
     * @return The task that was added along with it's id.
     */
    public static Task postLocal(Task task) {
        return null;
    }

    /**
     * Post a task to the "remote" table of the database.
     *
     * @param task The task to be added.
     * @return The task that was added along with it's id.
     */
    public static Task postRemote(Task task) {
        return null;
    }

    /**
     * Deletes a task from the "local" table of the database.
     *
     * @param id The id of the task to be deleted.
     */
    public static void deleteLocalTask(String id) {
    }

    /**
     * Deletes a task from the "local" table of the database.
     *
     * @param id The id of the task to be deleted.
     */
    private static void deleteRemoteTask(String id) {
    }

    /**
     * Gets a task (if exists) from the "local" table of the database.
     *
     * @param id ID of task to search for
     * @return Task found, if nothing found returns null.
     */
    public static Task getLocalTask(String id) {
        return null;
    }
    
    /**
     * Gets a task (if exists) from the "remote" table of the database.
     *
     * @param id ID of task to search for
     * @return Task found, if nothing found returns null.
     */
    public static Task getRemoteTask(String id) {
        return null;
    }
    
    
    /**
     * Updates the database with the passed in task based on the id.
     * 
     * Looks through both local and remote tables for a matching task.
     * 
     * @param task The task that you want changed
     * @return 
     */
    public static void updateTask(Task task) {
        
    }

   
}

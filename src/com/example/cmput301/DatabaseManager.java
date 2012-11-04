package com.example.cmput301;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {

    private ArrayList<Task> localTable;
    private ArrayList<Task> remoteTable;
    private String filename;

    /**
     * Create a new database manager with the "database" being saved to a file.
     *
     * @param filename
     */
    public DatabaseManager(String filename) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        this.filename = filename;
        try {
            fis = new FileInputStream(filename);
            ois = new ObjectInputStream(fis);
            ArrayList<ArrayList<Task>> tables;
            tables = (ArrayList<ArrayList<Task>>) ois.readObject();
            this.localTable = tables.get(0);
            this.remoteTable = tables.get(1);

            ois.close();
            fis.close();

        } catch (OptionalDataException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StreamCorruptedException ex) {
            //Handled in finally
        } catch (IOException ex) {
            //Handled in finally
        } finally {

            this.localTable = new ArrayList<Task>();
            this.remoteTable = new ArrayList<Task>();

            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Post a task to the "local" table of the database.
     *
     * @param task The task to be added.
     * @return The task that was added along with it's id.
     */
    public Task postLocal(Task task) {

        String id;
        do {
            id = "local@" + UUID.randomUUID().toString();
        } while (this.localIdExists(id));

        task.setId(id);

        this.localTable.add(task);
        this.saveDatabase();

        return task;
    }

    private boolean localIdExists(String id) {
        for (Task localTask : this.localTable) {
            if (localTask.getId().equals(id)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Post a task to the "remote" table of the database.
     *
     * @param task The task to be added.
     * @return The task that was added along with it's id.
     */
    public Task postRemote(Task task) {

        this.remoteTable.add(task);
        this.saveDatabase();

        return task;
    }

    /**
     * Deletes a task from the "local" table of the database.
     *
     * @param id The id of the task to be deleted.
     */
    public void deleteLocalTask(String id) {
        for (int i = 0; i < this.localTable.size(); i++) {

            String taskId = this.localTable.get(i).getId();

            if (taskId.equals(id)) {
                this.localTable.remove(i);
                break;
            }
        }

        this.saveDatabase();
    }

    /**
     * Deletes a task from the "local" table of the database.
     *
     * @param id The id of the task to be deleted.
     */
    private void deleteRemoteTask(String id) {
        for (int i = 0; i < this.remoteTable.size(); i++) {

            String taskId = this.remoteTable.get(i).getId();

            if (taskId.equals(id)) {
                this.remoteTable.remove(i);
                break;
            }
        }

        this.saveDatabase();
    }

    /**
     * Gets a task (if exists) from the "local" table of the database.
     *
     * @param id ID of task to search for
     * @return Task found, if nothing found returns null.
     */
    public Task getLocalTask(String id) {

        for (int i = 0; i < this.localTable.size(); i++) {

            String taskId = this.localTable.get(i).getId();

            if (taskId.equals(id)) {
                return this.localTable.get(i);
            }
        }

        return null;
    }

    /**
     * Get the local task list.
     *
     * @return A list of tasks in the local table of the database
     */
    public ArrayList<Task> getLocalTaskList() {

        ArrayList<Task> out = new ArrayList<Task>();

        for (Task task : this.localTable) {
            out.add(task.clone());
        }

        return out;

    }

    /**
     * Gets a task (if exists) from the "remote" table of the database.
     *
     * @param id ID of task to search for
     * @return Task found, if nothing found returns null.
     */
    public Task getRemoteTask(String id) {
        for (int i = 0; i < this.remoteTable.size(); i++) {

            String taskId = this.remoteTable.get(i).getId();

            if (taskId.equals(id)) {
                return this.remoteTable.get(i);
            }
        }

        return null;
    }

    /**
     * Get the remote task list.
     *
     * @return A list of tasks in the remote table of the database
     */
    public ArrayList<Task> getRemoteTaskList() {

        ArrayList<Task> out = new ArrayList<Task>();

        for (Task task : this.remoteTable) {
            out.add(task.clone());
        }

        return out;

    }

    /**
     * Updates the database with the passed in task based on the id.
     *
     * Looks through both local and remote tables for a matching task.
     *
     * @param task The task that you want changed
     * @return
     */
    public void updateTask(Task task) {
        for (int i = 0; i < this.remoteTable.size(); i++) {

            if (this.remoteTable.get(i).equals(task)) {
                this.remoteTable.set(i, task);
            }
        }

        for (int i = 0; i < this.localTable.size(); i++) {

            if (this.localTable.get(i).equals(task)) {
                this.localTable.set(i, task);
            }
        }

        this.saveDatabase();
    }

    public void nukeRemote() {
        this.remoteTable = new ArrayList<Task>();
        this.saveDatabase();
    }

    public void nukeAll() {
        this.remoteTable = new ArrayList<Task>();
        this.localTable = new ArrayList<Task>();
        this.saveDatabase();

    }

    public void nukeLocal() {
        this.localTable = new ArrayList<Task>();
        this.saveDatabase();
    }

    private synchronized void saveDatabase() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(this.filename);
            oos = new ObjectOutputStream(fos);

            ArrayList<ArrayList<Task>> tables = new ArrayList<ArrayList<Task>>();

            tables.add(this.localTable);
            tables.add(this.remoteTable);

            oos.writeObject(tables);

        } catch (IOException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

package com.example.cmput301;

import android.content.Context;
import java.util.ArrayList;

class MainController {

    private TaskManager taskManager;
    private ArrayList<Task> tasks;

    public MainController(Context context) {
        taskManager = new TaskManager(context);
        tasks = taskManager.getPrivateTasks();
    }

    public void addTask(String name, String description, String type) {
        Task task = new Task(name, description);

        taskManager.addTask(task);
        tasks = taskManager.getPrivateTasks();
    }

    public void addResponse(Task task, Response resp) {
        taskManager.postResponse(task, resp);
    }

    public void shareTask(String taskid) {
        taskManager.shareTask(taskid);
        tasks = taskManager.getSharedTasks();

    }

    public void deleteTask(String taskid) {
        taskManager.deleteTask(taskid);
        tasks = taskManager.getPrivateTasks();
    }

    public ArrayList<Task> getList() {
        return tasks;
    }

    public void checkoutPrivate() {
        tasks = taskManager.getPrivateTasks();
    }

    public void checkoutShared() {
        tasks = taskManager.getSharedTasks();
    }

    public void checkoutUnanswered() {
        tasks = taskManager.getUnansweredTasks();
    }

    public void checkoutRemote() {
        tasks = taskManager.getRemoteTasks();
    }

    public ArrayList<Task> search(String searchParams) {
        ArrayList<Task> filtered = new ArrayList<Task>();

        //Assuming search parameters are seperated by a space.
        String[] parameters = searchParams.split(" ");

        for (Task task : filtered) {
            boolean matched = false;

            for (String param : parameters) {
                if (task.getName().indexOf(param) != -1
                        || task.getDescription().indexOf(param) != -1) {
                    matched = true;
                    break;
                }
            }

            if (matched) {
                filtered.add(task);
            }
        }

        return filtered;

    }
}
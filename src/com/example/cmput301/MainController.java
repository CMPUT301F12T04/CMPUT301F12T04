package com.example.cmput301;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class MainController {

    private TaskManager taskManager;
    private ArrayList<Task> tasks;
    private TaskListAdapter adapter;
    private Activity activity;

    public MainController(Context context, Activity activity) {
        taskManager = new TaskManager(context);
        tasks = taskManager.getPrivateTasks();
        this.activity = activity;
        adapter = new TaskListAdapter(activity);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void addTask(String name, String description, String type) {
        Task task = new Task(name, description);

        taskManager.addTask(task);
        tasks = taskManager.getPrivateTasks();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void addResponse(Task task, Response resp) {
        taskManager.postResponse(task, resp);
    }

    public void shareTask(String taskid) {
        taskManager.shareTask(taskid);
        tasks = taskManager.getSharedTasks();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void deleteTask(String taskid) {
        taskManager.deleteTask(taskid);
        tasks = taskManager.getPrivateTasks();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public Task getTask(String taskid) {

        Task task = taskManager.getLocalTask(taskid);
        if (task == null) {
            task = taskManager.getRemoteTask(taskid);
        }
        return task;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Task> getList() {
        return tasks;
    }

    public void checkoutPrivate() {
        tasks = taskManager.getPrivateTasks();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void checkoutShared() {
        tasks = taskManager.getSharedTasks();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void checkoutUnanswered() {
        tasks = taskManager.getUnansweredTasks();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void checkoutRemote() {
        tasks = taskManager.getRemoteTasks();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public ArrayList<Task> filter(String searchParams) {
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

    public TaskListAdapter getListAdapter() {
        return adapter;
    }

    class TaskListAdapter extends BaseAdapter {

        private Context context;

        TaskListAdapter(Context context) {
            this.context = context;
        }

        /**
         * Overrided method that allows a task to display it's title and
         * description. An image can also be included to indicate the type of
         * task.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(R.layout.task_entry, null);
            }

            //set the title of the task
            TextView titleView;
            titleView = (TextView) row.findViewById(R.id.TaskTitleListEntry);
            titleView.setText(tasks.get(position).getName());

            //set the description of the task
            TextView descView;
            descView = (TextView) row.findViewById(R.id.TaskDescListEntry);
            descView.setText(tasks.get(position).getDescription());

            return row;
        }

        public int getCount() {
            return tasks.size();
        }

        public Object getItem(int position) {
            return tasks.get(position);
        }

        public long getItemId(int position) {
            return position;
        }
    }
}
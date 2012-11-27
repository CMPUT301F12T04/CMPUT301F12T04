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
package com.example.cmput301.controller;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import com.example.cmput301.model.*;
import com.example.cmput301.model.response.Response;
import com.example.cmput301.application.*;

import com.example.cmput301.R;
/**
 * This is a go between method for the View and Model components for this
 * application. It was written in a very hacky way because the original author
 * decided to tell us two days before it was due he couldn't handle this.
 *
 * Note: In future versions this will be broken into multiple controllers
 * depending on the view.
 */
public class MainController {

	private TaskManager taskManager;
	private ArrayList<Task> tasks;
	private TaskListAdapter adapter;
	public static MyCallback callBack;
	
	/**
	 * Basic constructor for the main controller.
	 *
	 * @param context The context we want data saved to.
	 * @param activity The active activity.
	 */
	public MainController(Context context, Activity activity) {

		Log.d("RESPONSE","CREATED NEW MAIN CONTROLLER");
		taskManager = new TaskManager(context);
		tasks = taskManager.getPrivateTasks();
		adapter = new TaskListAdapter(activity);
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}	
		
	}

	/**
	 * Adds a task to the local database as a private task.
	 *
	 * Refreshes the view.
	 *
	 * @param name
	 * @param description
	 * @param type The type of response that this task will require.
	 */
	public void addTask(String name, String description, String type) {
		Task task = new Task(name, description, type);

		taskManager.addTask(task);

		// Set the view to show private tasks when a task is added.
		tasks = taskManager.getPrivateTasks();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Add a response to the given task in the database.
	 *
	 * @param task The task you want a response added to
	 * @param resp The response.
	 */
	public void addResponse(Task task, Response resp) {
		new AddResponse().execute(task, resp);
	}



	private class AddResponse extends AsyncTask<Object,Void,Task>
	{

		@Override
		protected Task doInBackground(Object... args)
		{
			if(args.length==2)
			{
				Log.d("RESPONSE",((Task)args[0]).getName());
				Log.d("RESPONSE",(String)((Response)args[1]).getContent());
				Task task = (Task)args[0];
				Response response = (Response)args[1];
				taskManager.postResponse(task, response);
				return task;
			}
			return null;
		}

		protected void onPostExecute(Task result)
		{
			//update task
		}

	}
	/**
	 * Convert a task from private to shared which has the given task id.
	 *
	 * Refreshes the view.
	 *
	 * @param taskid
	 */
	public void shareTask(String taskid) {
//		taskManager.hide(taskid);
		new ShareTask().execute("share", taskid);
		checkoutPrivate();
	}

	private class ShareTask extends AsyncTask<String,Void,Task>
	{

		@Override
		protected Task doInBackground(String... args)
		{
			if(args.length == 2)
			{
				String taskid = args[1];
				Task result = taskManager.shareTask(taskid);
				return result;
			}
			return null;
		}

		protected void onPostExecute(Task result)
		{
			tasks = taskManager.getPrivateTasks();
			checkoutPrivate();   
			Log.d("REMOTE","REFRESHED");
			
			callBack.callbackCall();
		}
	}
	/**
	 * Delete a task with the given id.
	 * Note: This only works if it is a local task.
	 * @param taskid 
	 */
	public void deleteTask(String taskid) {
		taskManager.deleteTask(taskid);
		tasks = taskManager.getPrivateTasks();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Get a task with the given task id from the database.
	 * 
	 * @param taskid
	 * @return 
	 */
	public Task getTask(String taskid) {

		Task task = taskManager.getLocalTask(taskid);
		if (task == null) {
			task = taskManager.getRemoteTask(taskid);
		}
		return task;
	}

	/**
	 * Adds one vote to the task.
	 *
	 * @param task
	 */
	public void voteTask(Task task) {
		taskManager.voteTask(task);
	}

	/**
	 * Get list of all tasks that are currently being shown.
	 * 
	 * @return 
	 */
	public ArrayList<Task> getList() {
		return tasks;
	}

	/**
	 * Checkout the private task list.
	 */
	public void checkoutPrivate() {
		tasks = taskManager.getPrivateTasks();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Checkout the shared task list.
	 */
	public void checkoutShared() {
		tasks = taskManager.getSharedTasks();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Checkout the unanswered task list.
	 */
	public void checkoutUnanswered() {
		tasks = taskManager.getUnansweredTasks();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Checkout the remote task list.
	 */
	public void checkoutRemote() {
		tasks = taskManager.getRemoteTasks();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Apply a filter to the task list being viewed and return the result.
	 * 
	 * If any task has one or more keywords from the search parameters in it's
	 * title or description then it will be included in the list.
	 * 
	 * @param searchParams A string with keywords seperated by spaces.
	 * @return 
	 */
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

	/**
	 * Returns the listitem adapter
	 * @return 
	 */
	public TaskListAdapter getListAdapter() {
		Log.d("RESPONSE","NEW LIST ADAPTER");
		return adapter;
	}

	/**
	 * Hacky private class that represents the list item adapter.
	 */
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

			//Sets the image for the task, all tasks are currently set for TEXT only
			ImageView taskTypeImg = (ImageView) row.findViewById(R.id.TasktypePic);
			taskTypeImg.setImageResource(android.R.drawable.ic_menu_edit);

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



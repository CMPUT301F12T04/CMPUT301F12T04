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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

import com.example.cmput301.model.*;
import com.example.cmput301.model.response.PictureResponse;
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
	private Manager taskManager;
	private ArrayList<Task> tasks;
	private ArrayList<Task> tasksBackup;
	private TaskListAdapter adapter;
	public static ConnUpdateCallback callBack;
	private Context context;

	/**
	 * Basic constructor for the main controller.
	 *
	 * @param context The context we want data saved to.
	 * @param activity The active activity.
	 */
	public MainController(Context context, Activity activity) {
		this.context = context;
		this.taskManager = new Manager(context);
		this.tasks = taskManager.getPrivateTasks();
		this.adapter = new TaskListAdapter(activity);
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
	 * Convert a task from private to shared which has the given task id.
	 * @param taskid
	 */
	public void updateRemoteTasks() {
		if(isConnected())
		{
			new RemoteTaskUpdate().execute();
		}
		else
		{
			callBack.failed();
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

	public void checkoutRandom() {
		tasks = taskManager.getRemoteTasks();
		Collections.shuffle(tasks);
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	public void checkoutPopular() {
		tasks = taskManager.getRemoteTasks();
		ArrayList<Task> tasks2 = taskManager.getSharedTasks();
		tasks.addAll(tasks2);
		Collections.sort(tasks, new VotesComparator());
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
	
	public void refreshCurrentList(String listIndex)
	{	
		//refresh current list
		if (listIndex.equals("My Tasks")) {
			this.checkoutPrivate();
		} else if (listIndex.equals("My Shared")) {
			this.checkoutShared();
		} else if (listIndex.equals("Unanswered")) {
			this.checkoutUnanswered();
		} else if (listIndex.equals("Other User's Tasks")) {
			this.checkoutRemote();
		} else if (listIndex.equals("Random Tasks")) {
			this.checkoutRandom();
		} else if (listIndex.equals("Popular")) {
			this.checkoutPopular();
		}      
	}

	/**
	 * Determines Internet connectivity
	 * @return true if connected, either in 3G or wi-fi, false if otherwise
	 */
	public boolean isConnected() {
		ConnectivityManager cm =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}


	/**
	 * Apply a filter to the task list being viewed and change the tasks view 
	 * to match.
	 * 
	 * If any task has one or more keywords from the search parameters in it's
	 * title or description then it will be included in the list.
	 * 
	 * @param searchParams A string with keywords seperated by spaces
	 */
	public void filter(String searchParams) {
		if(tasksBackup == null) {
			tasksBackup = tasks;
		}
		ArrayList<Task> filtered = new ArrayList<Task>();

		for (Task task : tasksBackup) {
			if (matched(task,searchParams.split(" "))) {
				filtered.add(task);
			}
		}
		tasks = filtered;
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	private boolean matched(Task task, String parameters[])
	{
		for (String param : parameters) {
			if (task.getName().indexOf(param) != -1
					|| task.getDescription().indexOf(param) != -1) {
				return true;
			}
		}
		return false;
	}

	public void restoreUnfiltered() {
		if(tasksBackup != null) {
			tasks = tasksBackup;
			tasksBackup = null;
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
		}
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
			TextView titleView = getTitleView(convertView);
			titleView.setText(tasks.get(position).getName());

			//set the description of the task
			TextView descView =getDescView(convertView);
			descView.setSingleLine(false);
			descView.setText(tasks.get(position).getDescription() + "\nVotes: " + tasks.get(position).getVotes());

			//Sets the image for the task, task in this case is a picture type task
			if(tasks.get(position).getType().equals(PictureResponse.class.toString()))
			{
				ImageView taskTypeImg = (ImageView) row.findViewById(R.id.TasktypePic);
				taskTypeImg.setImageResource(android.R.drawable.ic_menu_gallery);
			}
			//task is another type
			else
			{
				ImageView taskTypeImg = (ImageView) row.findViewById(R.id.TasktypePic);
				taskTypeImg.setImageResource(android.R.drawable.ic_menu_edit);
			}

			return row;
		}

		private TextView getTitleView(View convertView)
		{
			View row = convertView;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(R.layout.task_entry, null);
			}
			return (TextView) row.findViewById(R.id.TaskTitleListEntry);
		}

		private TextView getDescView(View convertView)
		{
			View row = convertView;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(R.layout.task_entry, null);
			}

			return (TextView) row.findViewById(R.id.TaskDescListEntry);
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

	/**
	 * Gets tasks from the web service, and controls a loading
	 * screen in MainController 
	 */
	private class RemoteTaskUpdate extends AsyncTask<Void,Void,Boolean>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			callBack.startSyncLoadingScreen();
		}

		@Override
		protected Boolean doInBackground(Void... args)
		{
			taskManager.Refresh();
			return true;
		}

		protected void onPostExecute(Boolean success)
		{
				callBack.finished();
		}
	}
}



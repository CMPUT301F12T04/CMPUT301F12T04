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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


import com.example.cmput301.model.*;
import com.example.cmput301.model.response.PictureResponse;
import com.example.cmput301.application.*;

import com.example.cmput301.R;

public class MainController {
	private TaskListController checkout = new TaskListController();
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
		checkout.setTaskManager(new Manager(context)); 
		checkout.setTasks(checkout.getTaskManager().getPrivateTasks());
		checkout.setAdapter(new TaskListAdapter(activity));
		if (checkout.getAdapter() != null) {  
			checkout.getAdapter().notifyDataSetChanged();
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
		checkout.addTask(name, description, type);
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
	 * Get list of all tasks that are currently being shown.
	 * 
	 * @return 
	 */
	public ArrayList<Task> getList() {
		return checkout.getTasks();
	}

	/**
	 * Checkout the private task list.
	 */
	public void checkoutPrivate() {
		checkout.checkoutPrivate();
	}

	/**
	 * Checkout the shared task list.
	 */
	public void checkoutShared() {
		checkout.checkoutShared();
	}

	/**
	 * Checkout the unanswered task list.
	 */
	public void checkoutUnanswered() {
		checkout.checkoutUnanswered();
	}

	/**
	 * Checkout the remote task list.
	 */
	public void checkoutRemote() {
		checkout.checkoutRemote();
	}

	public void checkoutRandom() {
		checkout.checkoutRandom();
	}

	public void checkoutPopular() {
		checkout.checkoutPopular();
	}
	
	public void refreshCurrentList(String listIndex)
	{	
		checkout.refreshCurrentList(listIndex);      
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
		checkout.filter(searchParams);
	}

	public void restoreUnfiltered() {
		checkout.restoreUnfiltered();
	}
	/**
	 * Returns the listitem adapter
	 * @return 
	 */
	public TaskListAdapter getListAdapter() {
		return checkout.getListAdapter();
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
			TextView titleView = getTitleView(row);
			titleView.setText(checkout.getTasks().get(position).getName());

			//set the description of the task
			TextView descView = getDescView(row);
			descView.setSingleLine(false);
			descView.setText(checkout.getTasks().get(position).getDescription() + "\nVotes: " + checkout.getTasks().get(position).getVotes());
			//Sets the image for the task, task in this case is a picture type task
			if(checkout.getTasks().get(position).getType().equals(PictureResponse.class.toString()))
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
			return checkout.getTasks().size();
		}

		public Object getItem(int position) {
			return checkout.getTasks().get(position);
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
			checkout.getTaskManager().Refresh();
			return true;
		}

		protected void onPostExecute(Boolean success)
		{
				callBack.finished();
		}
	}
}



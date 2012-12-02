package com.example.cmput301.controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cmput301.application.ConnUpdateCallback;
import com.example.cmput301.controller.MainController.TaskListAdapter;
import com.example.cmput301.model.Task;
import com.example.cmput301.model.TaskManager;
import com.example.cmput301.model.response.Response;

public class IndividualTaskController
{
	public static ConnUpdateCallback callBack;
	private TaskManager taskManager;
	private TaskListAdapter adapter;
	private Context context;

	/**
	 * Basic constructor for the main controller.
	 *
	 * @param context The context we want data saved to.
	 * @param activity The active activity.
	 */
	public IndividualTaskController(Context context, Activity activity) {
		this.context = context;
		this.taskManager = new TaskManager(context);
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
	 * Add a response to the given task in the database.
	 *
	 * @param task The task you want a response added to
	 * @param resp The response.
	 */
	public void addResponse(Task task, Response resp) {
		if(task.getStatus()==Task.STATUS_PRIVATE)
		{
			taskManager.postResponse(task, resp);
			return;
		}
		else
		{
			new AddResponse().execute(task, resp);
		}
	}

	/**
	 * Shares responses with the web service
	 */
	private class AddResponse extends AsyncTask<Object,Void,Task>
	{
		@Override
		protected void onPreExecute()
		{
			//start loading screen in main controller
			Log.d("RESPONSE","WORKED SOMEWHAT");
			callBack.startUploadingScreen();
		}

		@Override
		protected Task doInBackground(Object... args)
		{
			if(args.length==2)
			{
				Task task = (Task)args[0];
				Response response = (Response)args[1];
				taskManager.postResponse(task, response);
				taskManager.Refresh();
				return task;
			}
			return null;
		}
		protected void onPostExecute(Task result)
		{
			Log.d("RESPONSE","WORKED SOMEWHAT");
			callBack.finished();
		}
	}

	/**
	 * Convert a task from private to shared which has the given task id.
	 * @param taskid
	 */
	public void shareTask(String taskid) {
		if(isConnected())
		{
			new ShareTask().execute(taskid);
		}
		else
		{
			callBack.failed();
		}
	}

	/** Determines Internet connectivity
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
	 * Shares tasks with the web service, and controls a loading
	 * screen in MainController 
	 */
	private class ShareTask extends AsyncTask<String,Void,Task>
	{
		@Override
		protected void onPreExecute()
		{
			//start loading screen in main controller
			callBack.startUploadingScreen();
		}

		@Override
		protected Task doInBackground(String... args)
		{
			if(args.length == 1)
			{
				String taskid = args[0];

				//share task to web service and return result
				Task result = taskManager.shareTask(taskid);
				return result;
			}
			return null;
		}
		protected void onPostExecute(Task result)
		{
			callBack.finished();
		}
	}
}

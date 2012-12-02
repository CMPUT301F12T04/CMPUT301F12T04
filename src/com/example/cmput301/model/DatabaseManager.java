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
package com.example.cmput301.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cmput301.application.*;
import com.example.cmput301.model.response.PictureResponse;
import com.example.cmput301.model.response.Response;
import com.example.cmput301.model.response.TextResponse;
import com.example.cmput301.model.response.factory.PictureResponseFactory;
import com.example.cmput301.model.response.factory.ResponseFactory;
import com.example.cmput301.model.response.factory.TextResponseFactory;

public class DatabaseManager {
	private SQLiteDatabase db; 

	/**
	 * Create a new database manager with the "database" being saved to a file.
	 *
	 * @param filename
	 */
	public DatabaseManager(Context context) {
		DatabaseSingleton ds = DatabaseSingleton.getInstance(context);
		db = ds.getDB();
	}

	/**
	 * Post a task to the "local" table of the database.
	 *
	 * @param task The task to be added.
	 * @return The task that was added along with it's id.
	 */
	//fixed
	public Task postLocal(Task task) {

		if(task.getStatus()==2)
		{
//			Log.d("REMOTE","SHARED " + task.getName());

		}
		if(task.getId()==null)
		{
			String id;

			do {
				id = "local@" + UUID.randomUUID().toString();
			} while (this.localIdExists(id));
			task.setId(id);			
		}
		this.addTask_LocaleTable(task);
		return task;

	}
	
	private void addTask_LocaleTable(Task task)
	{
		ContentValues cv = new ContentValues();
		cv.put(StringRes.COL_ID, task.getId());
		try
		{
			cv.put(StringRes.COL_CONTENT, toJson(task).toString() );
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		db.insert(StringRes.LOCAL_TASK_TABLE_NAME, StringRes.COL_ID, cv);
	}

	private boolean localIdExists(String id) {
		Cursor c = db.rawQuery("SELECT * FROM "+ StringRes.LOCAL_TASK_TABLE_NAME+" WHERE "+StringRes.COL_ID+"=?", new String[]{id,});
		if(c==null||c.getCount()==0)
		{
			return false;
		}
		return true;
	}

	/**
	 * Post a task to the "remote" table of the database.
	 *
	 * @param task The task to be added.
	 * @return The task that was added along with it's id.
	 */
	public Task postRemote(Task task) {
		ContentValues cv = new ContentValues();
		cv.put(StringRes.COL_ID, task.getId());
		try
		{
			cv.put(StringRes.COL_CONTENT, toJson(task).toString());
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.insert(StringRes.REMOTE_TASK_TABLE_NAME, StringRes.COL_ID, cv);
		return task;
	}

	private JSONObject toJson(Task task) throws JSONException
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", task.getName());
		jsonObject.put("description", task.getDescription());
		jsonObject.put("id", task.getId());
		jsonObject.put("type", task.getType());
		jsonObject.put("status", task.getStatus());
		jsonObject.put("votes", task.getVotes());

		List<Response> responses = task.getResponses();
		JSONArray arr = new JSONArray();
		for(Response response : responses)
		{
			JSONObject jo = new JSONObject();
			jo.put("annotation", response.getAnnotation());
			jo.put("content", response.getSaveable());
			jo.put("timestamp", response.getTimestamp());
			arr.put(jo);
		}
		jsonObject.put("responses", arr);
		return jsonObject;
	}

	/**
	 * Deletes a task from the "local" table of the database.
	 *
	 * @param id The id of the task to be deleted.
	 */
	public void deleteLocalTask(String id) {
		db.delete(StringRes.LOCAL_TASK_TABLE_NAME, StringRes.COL_ID + " =?", new String[]{id,});
	}

	/**
	 * Deletes a task from the "local" table of the database.
	 *
	 * @param id The id of the task to be deleted.
	 */
	@SuppressWarnings("unused")
	// fixed
	private void deleteRemoteTask(String id) {
		db.delete(StringRes.REMOTE_TASK_TABLE_NAME, StringRes.COL_ID + " =?", new String[]{id,});
	}

	/**
	 * Gets a task (if exists) from the "local" table of the database.
	 *
	 * @param id ID of task to search for
	 * @return Task found, if nothing found returns null.
	 * @throws JSONException 
	 */
	//  fixed
	public Task getLocalTask(String id) {
		try
		{
			Cursor c = db.rawQuery("SELECT * FROM " + StringRes.LOCAL_TASK_TABLE_NAME + " WHERE "+StringRes.COL_ID+"=?", new String[]{id,});
			if(c==null||c.getCount()==0)
			{ 
				return null;
			}
			else
			{
				c.moveToFirst();
				String taskContent = c.getString(c.getColumnIndex(StringRes.COL_CONTENT));
				JSONObject jsonTask;

				jsonTask = toJsonTask(taskContent);

				return toTask(jsonTask);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Converts json string into a task object and returns.
	 * @param jsonTask , task object in json format.
	 * @return Task
	 * @throws JSONException
	 */
	private static Task toTask(JSONObject jsonTask) throws JSONException
	{
		if(jsonTask==null)
		{
			return null;
		}
		else
		{
			return new Task(jsonTask.getString("name"), jsonTask.getString("description"), jsonTask.getString("id")
					,jsonTask.getInt("status"), toResponses(jsonTask),jsonTask.getString("type"),jsonTask.getInt("votes"));
		}
	}

	/**
	 * Gets list of responses from jsonObject and returns
	 * @param jsonTask , task object in json format.
	 * @return List<Response>
	 * @throws JSONException
	 */
	private static List<Response> toResponses(JSONObject jsonTask) throws JSONException
	{
		try
		{
			JSONArray jsonArray = jsonTask.getJSONArray("responses");
			List<Response> responses = new ArrayList<Response>();
			String type = jsonTask.getString("type");
			
			ResponseFactory respFactory;
			
			if(type.equals(TextResponse.class.toString())){
				respFactory = new TextResponseFactory();
			} else if (type.equals(PictureResponse.class.toString())) {
				respFactory = new PictureResponseFactory();
			} else {
				throw new UnsupportedOperationException("Not implemented");
			}
			
			for(int i = 0; i < jsonArray.length(); i++)
			{
				Response resp = respFactory.createResponse(jsonArray.getJSONObject(i).getString("annotation"), jsonArray.getJSONObject(i).getString("content"));
				resp.setTimestamp(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(jsonArray.getJSONObject(i).getString("timestamp")));
				responses.add(resp);			
			}	
			return responses;
		}
		catch(ParseException e)
		{
			System.err.println("Could not parse date");
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject toJsonTask(String taskContent) throws JSONException
	{
		JSONObject jsonTask = new JSONObject(taskContent);
		return jsonTask;
	}

	/**
	 * Get the local task list.
	 *
	 * @return A list of tasks in the local table of the database
	 * @throws JSONException 
	 */
	public ArrayList<Task> getLocalTaskList()  {
		try
		{
			ArrayList<Task> out = new ArrayList<Task>();

			Cursor c = db.rawQuery("SELECT * FROM "+StringRes.LOCAL_TASK_TABLE_NAME, new String[]{});
			if(c.moveToFirst())
			{
				while(c.isAfterLast()==false)
				{
					JSONObject obj = toJsonTask(c.getString(c.getColumnIndex(StringRes.COL_CONTENT)));
					out.add(toTask(obj));
					c.moveToNext();
				}
				return out;
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets a task (if exists) from the "remote" table of the database.
	 *
	 * @param id ID of task to search for
	 * @return Task found, if nothing found returns null.
	 * @throws JSONException 
	 */
	public Task getRemoteTask(String id)  {
		try
		{
			Cursor c = db.rawQuery("SELECT * FROM " + StringRes.REMOTE_TASK_TABLE_NAME + " WHERE " + StringRes.COL_ID + "=?", new String[]{id,});
			c.moveToFirst();
			if(c==null||c.getCount()==0)
			{ 
				return null;
			}
			else
			{
				String taskContent = c.getString(c.getColumnIndex(StringRes.COL_CONTENT));
				JSONObject jsonTask = toJsonTask(taskContent);
				return toTask(jsonTask);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the remote task list.
	 *
	 * @return A list of tasks in the remote table of the database
	 * @throws JSONException 
	 */
	//fixed
	public ArrayList<Task> getRemoteTaskList()  {
		try
		{
			Log.d("refresh","STARTING REMOTE TASK LIST");
			ArrayList<Task> out = new ArrayList<Task>();
			Cursor c = db.rawQuery("SELECT * FROM "+StringRes.REMOTE_TASK_TABLE_NAME, new String[]{});
			if(c.moveToFirst())
			{
				while(c.isAfterLast()==false)
				{
					JSONObject obj = toJsonTask(c.getString(c.getColumnIndex(StringRes.COL_CONTENT)));
					out.add(toTask(obj));
					c.moveToNext();
				}
			}
			Log.d("refresh","sizeof remotetasklist = "+out.size());
			return out;

		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
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
	public void updateTask(Task task)
	{	
		try {
			ContentValues cv = new ContentValues();
			cv.put(StringRes.COL_CONTENT, toJson(task).toString());	
			cv.put(StringRes.COL_ID,task.getId());
			int n = db.delete(StringRes.LOCAL_TASK_TABLE_NAME, StringRes.COL_ID + "=?", new String[]{task.getId(),});
			if(n==1)
			{
				db.insert(StringRes.LOCAL_TASK_TABLE_NAME, StringRes.COL_ID, cv);;
			}
			int b = db.delete(StringRes.REMOTE_TASK_TABLE_NAME, StringRes.COL_ID + "=?", new String[]{task.getId(),});
			if(b==1)
			{
				db.insert(StringRes.REMOTE_TASK_TABLE_NAME, StringRes.COL_ID, cv);
			}

			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void nukeRemote() {
		db.delete(StringRes.REMOTE_TASK_TABLE_NAME, null, null);
	}

	//fixed
	public void nukeAll() {
		db.delete(StringRes.LOCAL_TASK_TABLE_NAME, null, null);
		db.delete(StringRes.REMOTE_TASK_TABLE_NAME, null, null);
	}

	//fixed
	public void nukeLocal() {
		db.delete(StringRes.LOCAL_TASK_TABLE_NAME, null, null);
	}

	public void close()
	{
		db.close();
	}

	public void hideTask(String taskid)
	{
		Task task = this.getLocalTask(taskid);
		task.setStatus(5);
		ContentValues cv = new ContentValues();
		cv.put(StringRes.COL_ID, task.getId());
		try
		{
			cv.put(StringRes.COL_CONTENT, this.toJson(task).toString());
			db.delete(StringRes.LOCAL_TASK_TABLE_NAME, StringRes.COL_ID+"=?", new String[]{taskid,});
			db.insert(StringRes.LOCAL_TASK_TABLE_NAME, StringRes.COL_ID, cv);			
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

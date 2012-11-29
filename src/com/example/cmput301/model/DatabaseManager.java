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

	//	private ArrayList<Task> localTable;
	//	private ArrayList<Task> remoteTable;
	private SQLiteDatabase db; // new
	String local_task_table = "local_task_table"; // new
	String remote_task_table = "remote_task_table";// new
	String col_id = "id";// new
	String col_content = "content";// new

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
			Log.d("REMOTE","SHARED " + task.getName());

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

		Log.d("DATABASE",task.getId());
		return task;

	}
	// fixed
	private void addTask_LocaleTable(Task task)
	{
		ContentValues cv = new ContentValues();
		cv.put(col_id, task.getId());
		try
		{
			Log.d("DATABASE",toJson(task).toString());
			cv.put(col_content, toJson(task).toString() );
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.insert(local_task_table, this.col_id, cv);
	}

	private boolean localIdExists(String id) {
		Cursor c = db.rawQuery("SELECT * FROM "+local_task_table+" WHERE "+col_id+"=?", new String[]{id,});
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
		cv.put(col_id, task.getId());
		try
		{
			cv.put(col_content, toJson(task).toString());
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.insert(remote_task_table, col_id, cv);
		return task;
	}

	private JSONObject toJson(Task task) throws JSONException
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", task.getName());
		jsonObject.put("description", task.getDescription());

		jsonObject.put("id", task.getId());
		Log.d("RESPONSE","ID:===" + task.getId());

		jsonObject.put("type", task.getType());
		Log.d("TYPE",task.getType());
		jsonObject.put("status", task.getStatus());
		jsonObject.put("votes", task.getVotes());

		List<Response> responses = task.getResponses();
		JSONArray arr = new JSONArray();
		for(Response response : responses)
		{
			JSONObject jo = new JSONObject();
			jo.put("content", response.getContent());
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
	// fixed
	public void deleteLocalTask(String id) {
		db.delete(local_task_table, col_id + " =?", new String[]{id,});
	}

	/**
	 * Deletes a task from the "local" table of the database.
	 *
	 * @param id The id of the task to be deleted.
	 */
	@SuppressWarnings("unused")
	// fixed
	private void deleteRemoteTask(String id) {
		db.delete(remote_task_table, col_id + " =?", new String[]{id,});
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
			Cursor c = db.rawQuery("SELECT * FROM " + local_task_table + " WHERE "+col_id+"=?", new String[]{id,});
			if(c==null||c.getCount()==0)
			{ 
				return null;
			}
			else
			{
				c.moveToFirst();
				String taskContent = c.getString(c.getColumnIndex(this.col_content));
				JSONObject jsonTask;

				jsonTask = toJsonTask(taskContent);

				return toTask(jsonTask);
			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
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

				Response resp = respFactory.createResponse(null /*Annotation goes here when it's implemented.*/, jsonArray.getJSONObject(i).getString("content"));
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
	//fixed
	public ArrayList<Task> getLocalTaskList()  {

		try
		{
			ArrayList<Task> out = new ArrayList<Task>();

			Cursor c = db.rawQuery("SELECT * FROM "+local_task_table, new String[]{});
			if(c.moveToFirst())
			{
				while(c.isAfterLast()==false)
				{
					Log.d("DATABASE",c.getString(c.getColumnIndex(col_content)));
					JSONObject obj = toJsonTask(c.getString(c.getColumnIndex(col_content)));
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
	// fixed
	public Task getRemoteTask(String id)  {

		try
		{
			Cursor c = db.rawQuery("SELECT * FROM " + remote_task_table, new String[]{id,});
			c.moveToFirst();
			if(c==null||c.getCount()==0)
			{ 
				return null;
			}
			else
			{
				String taskContent = c.getString(c.getColumnIndex(this.col_content));
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
			Log.d("REMOTE","STARTING REMOTE TASK LIST");
			ArrayList<Task> out = new ArrayList<Task>();

			Cursor c = db.rawQuery("SELECT * FROM "+remote_task_table, new String[]{});
			if(c.moveToFirst())
			{
				while(c.isAfterLast()==false)
				{
					JSONObject obj = toJsonTask(c.getString(c.getColumnIndex(col_content)));
					out.add(toTask(obj));
				}
			}
			
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
		ContentValues cv = new ContentValues();
		try {
			Cursor c = db.rawQuery("SELECT * FROM " + local_task_table + " WHERE " + this.col_id + "='" + task.getId() + "'",  new String[]{});
			c.moveToFirst();
			
			Log.d("TEST", "" +  toJson(task).toString());
			Log.d("TEST!B", c.getString(c.getColumnIndex(this.col_content)));

			cv.put(this.col_content, toJson(task).toString());
			
			db.update(local_task_table, cv, this.col_id + "='" + task.getId() + "'",  new String[]{});
			db.update(remote_task_table, cv, this.col_id + "='" + task.getId() + "'",  new String[]{});
			
			Cursor c2 = db.rawQuery("SELECT * FROM " + local_task_table + " WHERE " + this.col_id + "='" + task.getId() + "'",  new String[]{});
			c2.moveToFirst();
			Log.d("TEST!A", c2.getString(c.getColumnIndex(this.col_content)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	//fixed
	public void nukeRemote() {
		db.delete(remote_task_table, null, null);
	}

	//fixed
	public void nukeAll() {
		db.delete(local_task_table, null, null);
		db.delete(remote_task_table, null, null);
	}

	//fixed
	public void nukeLocal() {
		db.delete(local_task_table, null, null);
	}

	public void close()
	{
		// TODO Auto-generated method stub
		db.close();
	}

	public void hideTask(String taskid)
	{
		Task task = this.getLocalTask(taskid);
		task.setStatus(5);
		ContentValues cv = new ContentValues();
		cv.put(col_id, task.getId());
		try
		{
			cv.put(col_content, this.toJson(task).toString());
			db.delete(local_task_table, col_id+"=?", new String[]{taskid,});
			db.insert(local_task_table, col_id, cv);			
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

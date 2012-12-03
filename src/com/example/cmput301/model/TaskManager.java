/**
 * Copyright (c) 2012 Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html Contributors: Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu - initial API and                               implementation
 */
package com.example.cmput301.model;


import android.util.Log;

public class TaskManager
{
	/**
	 * Attaches task to the local database and returns the task which was added along with it's id.
	 * @param task  The task you want to be added.
	 * @return  The task which was added with the local task id attached.
	 */
	public Task addTask(Task task)
	{
		Log.d("TYPE", task.getType());
		Task addedTask = Manager.dbman.postLocal(task);
		return addedTask;
	}

	/**
	 * Share the task with the given id.
	 * @param id  The id of the task you want shared.
	 */
	public Task shareTask(String id)
	{
		Task localTask = Manager.dbman.getLocalTask(id);
		Task sharedTask = WebService.put(localTask);
		if (sharedTask != null)
		{
			Manager.dbman.deleteLocalTask(id);
			sharedTask.setStatus(Task.STATUS_SHARED);
			Manager.dbman.postLocal(sharedTask);
			return sharedTask;
		}
		return null;
	}

	/**
	 * Delete the task with the given id deletes from local tables only. You cannot delete a remote user's task you can only delete you own.
	 * @param id  The id of the task you want deleted
	 * @return  Whether or not the task existed and was deleted
	 */
	public boolean deleteTask(String id)
	{
		Task local = Manager.dbman.getLocalTask(id);
		if (local != null)
		{
			WebService.delete(id);
			Manager.dbman.deleteLocalTask(id);
			return true;
		}
		return false;
	}
}
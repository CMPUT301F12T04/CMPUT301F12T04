/**
 * Copyright (c) 2012 Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html Contributors: Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu - initial API and                               implementation
 */
package com.example.cmput301.model;


import com.example.cmput301.model.response.Response;
import android.util.Log;

public class ResponseManager
{
	/**
	 * Vote for a given task.
	 * @param id  The task you want one vote added to.
	 */
	public void voteTask(Task task)
	{
		task.increaseVotes();
		Manager.dbman.updateTask(task);
	}

	/**
	 * Post a response to a task with the given id in the database.
	 * @param response  The response you want to add
	 * @param task  The task you want the response added to.
	 */
	public void postResponse(Task task, Response response)
	{
		if (task.getStatus() == Task.STATUS_PRIVATE)
		{
			Log.d("RESPONSE", "Still private");
			task.addResponse(response);
			Manager.dbman.updateTask(task);
		}
		else
		{
			Log.d("RESPONSE", "Content ====" + (String) response.getContent());
			Task updatedTask = WebService.post(task, response);
			Manager.dbman.updateTask(updatedTask);
		}
	}
}
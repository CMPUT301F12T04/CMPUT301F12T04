/**
 * Copyright (c) 2012 Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html Contributors: Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu - initial API and                               implementation
 */
package com.example.cmput301.model;


import java.util.ArrayList;

public class TaskLists
{
	/**
	 * Get a list of all tasks in the local table of the database which have the status STATUS_PRIVATE
	 * @return
	 */
	public ArrayList<Task> getPrivateTasks()
	{
		ArrayList<Task> privateList = new ArrayList<Task>();
		ArrayList<Task> tasks = Manager.dbman.getLocalTaskList();
		if (tasks != null)
		{
			for (Task task : Manager.dbman.getLocalTaskList())
			{
				if (task.getStatus() == Task.STATUS_PRIVATE)
				{
					privateList.add(task);
				}
			}
		}
		return privateList;
	}

	/**
	 * Get a list of all tasks in the local table of the database which have the status STATUS_SHARED
	 * @return
	 */
	public ArrayList<Task> getSharedTasks()
	{
		ArrayList<Task> sharedList = new ArrayList<Task>();
		if (Manager.dbman.getLocalTaskList() == null)
		{
			return sharedList;
		}
		for (Task task : Manager.dbman.getLocalTaskList())
		{
			if (task.getStatus() == Task.STATUS_SHARED)
			{
				sharedList.add(task);
			}
		}
		return sharedList;
	}
}
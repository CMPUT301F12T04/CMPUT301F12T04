/**
 * Copyright (c) 2012 Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html Contributors: Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu - initial API and                               implementation
 */
package com.example.cmput301.controller;


import com.example.cmput301.controller.MainController.TaskListAdapter;
import com.example.cmput301.model.Manager;
import java.util.ArrayList;
import com.example.cmput301.model.Task;
import java.util.Collections;
import com.example.cmput301.model.VotesComparator;

public class TaskListController
{
	private Manager taskManager;
	private ArrayList<Task> tasks;
	private TaskListAdapter adapter;
	private ArrayList<Task> tasksBackup;

	public Manager getTaskManager()
	{
		return taskManager;
	}

	public void setTaskManager(Manager taskManager)
	{
		this.taskManager = taskManager;
	}

	public ArrayList<Task> getTasks()
	{
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks)
	{
		this.tasks = tasks;
	}

	public TaskListAdapter getAdapter()
	{
		return adapter;
	}

	public void setAdapter(TaskListAdapter adapter)
	{
		this.adapter = adapter;
	}

	public ArrayList<Task> getTasksBackup()
	{
		return tasksBackup;
	}

	public void setTasksBackup(ArrayList<Task> tasksBackup)
	{
		this.tasksBackup = tasksBackup;
	}

	public void refreshCurrentList(String listIndex)
	{
		if (listIndex.equals("My Tasks"))
		{
			this.checkoutPrivate();
		}
		else if (listIndex.equals("My Shared"))
		{
			this.checkoutShared();
		}
		else if (listIndex.equals("Unanswered"))
		{
			this.checkoutUnanswered();
		}
		else if (listIndex.equals("Other User's Tasks"))
		{
			this.checkoutRemote();
		}
		else if (listIndex.equals("Random Tasks"))
		{
			this.checkoutRandom();
		}
		else if (listIndex.equals("Popular"))
		{
			this.checkoutPopular();
		}
	}

	/**
	 * Apply a filter to the task list being viewed and change the tasks view  to match. If any task has one or more keywords from the search parameters in it's title or description then it will be included in the list.
	 * @param searchParams  A string with keywords seperated by spaces
	 */
	public void filter(String searchParams)
	{
		if (tasksBackup == null)
		{
			tasksBackup = tasks;
		}
		ArrayList<Task> filtered = new ArrayList<Task>();
		for (Task task : tasksBackup)
		{
			if (matched(task, searchParams.split(" ")))
			{
				filtered.add(task);
			}
		}
		tasks = filtered;
		if (adapter != null)
		{
			adapter.notifyDataSetChanged();
		}
	}

	public void restoreUnfiltered()
	{
		if (tasksBackup != null)
		{
			tasks = tasksBackup;
			tasksBackup = null;
			if (adapter != null)
			{
				adapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * Adds a task to the local database as a private task. Refreshes the view.
	 * @param name
	 * @param description
	 * @param type  The type of response that this task will require.
	 */
	public void addTask(String name, String description, String type)
	{
		Task task = new Task(name, description, type);
		taskManager.addTask(task);
		tasks = taskManager.getPrivateTasks();
		if (adapter != null)
		{
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Checkout the private task list.
	 */
	public void checkoutPrivate()
	{
		tasks = taskManager.getPrivateTasks();
		if (adapter != null)
		{
			adapter.notifyDataSetChanged();
		}
	}


	/**
	 * Checkout the shared task list.
	 */
	public void checkoutShared()
	{
		tasks = taskManager.getSharedTasks();
		if (adapter != null)
		{
			adapter.notifyDataSetChanged();
		}
	}

	public void checkoutPopular()
	{
		tasks = taskManager.getRemoteTasks();
		ArrayList<Task> tasks2 = taskManager.getSharedTasks();
		tasks.addAll(tasks2);
		Collections.sort(tasks, new VotesComparator());
		if (adapter != null)
		{
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Checkout the remote task list.
	 */
	public void checkoutRemote()
	{
		tasks = taskManager.getRemoteTasks();
		if (adapter != null)
		{
			adapter.notifyDataSetChanged();
		}
	}

	public void checkoutRandom()
	{
		tasks = taskManager.getRemoteTasks();
		Collections.shuffle(tasks);
		if (adapter != null)
		{
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Checkout the unanswered task list.
	 */
	public void checkoutUnanswered()
	{
		tasks = taskManager.getUnansweredTasks();
		if (adapter != null)
		{
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Returns the listitem adapter
	 * @return  
	 */
	public TaskListAdapter getListAdapter()
	{
		return adapter;
	}

	public boolean matched(Task task, String parameters[])
	{
		for (String param : parameters)
		{
			if (task.getName().indexOf(param) != -1
					|| task.getDescription().indexOf(param) != -1)
			{
				return true;
			}
		}
		return false;
	}
}
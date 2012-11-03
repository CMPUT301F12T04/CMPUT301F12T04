package com.example.cmput301;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.example.cmput301.Response;
import com.example.cmput301.Task;
import com.example.cmput301.TextResponse;
import com.example.cmput301.WebService;
import junit.framework.TestCase;


public class webservicetest extends TestCase
{
	public void testWebServiceGetTask()
	{
		Task task = new Task("task1","description1",null, TextResponse.class.toString());
		task = WebService.put(task);
		Task newTask = WebService.get(task.getId());
		assertTrue(task.getName().equals(newTask.getName()));
		assertTrue(task.getDescription().equals(newTask.getDescription()));
		WebService.delete(newTask.getId());
		assertTrue(WebService.get(newTask.getId())==null);
	}

	public void testAddResponse()
	{
		Task task = new Task("Task without responses","some crazy ass description",null, TextResponse.class.toString());
		assertTrue(task.getType().equals(TextResponse.class.toString()));
		Response r1 = new TextResponse("text response 1", new Date());
		Response r2 = new TextResponse("text response 2", new Date());
		
		Task t = WebService.put(task);
		t = WebService.post(t, r1);
		assertTrue(t.getResponses().get(0).getContent().equals(r1.getContent()));
		t = WebService.post(t, r2);
		assertTrue(t.getResponses().get(1).getContent().equals(r2.getContent()));
	}
	
	public void testAddTaskWithResponses()
	{
		Task task = new Task("Task without responses","some crazy ass description",null, TextResponse.class.toString());
		Response r1 = new TextResponse("text response 1", new Date());
		Response r2 = new TextResponse("text response 2", new Date());
		task.addResponse(r1);
		task.addResponse(r2);
		Task newTask = WebService.put(task);
	
		assertTrue(task.getName().equals(newTask.getName()));
		assertTrue(task.getDescription().equals(newTask.getDescription()));
		assertTrue(task.getResponses().get(0).getContent().equals(newTask.getResponses().get(0).getContent()));
		assertTrue(task.getResponses().get(1).getContent().equals(newTask.getResponses().get(1).getContent()));

		WebService.delete(newTask.getId());
		assertTrue(WebService.get(newTask.getId())==null);
	}
	
	public void testAddTaskWithNoResponses()
	{
		Task task = new Task("Task without responses","some crazy ass description",null, TextResponse.class.toString());
		Task newTask = WebService.put(task);
		assertTrue(task.getName().equals(newTask.getName()));
		assertTrue(task.getDescription().equals(newTask.getDescription()));

		WebService.delete(newTask.getId());
		assertTrue(WebService.get(newTask.getId())==null);
	}
	
	public void testGetAll()
	{
		WebService.nuke("judgedredd");
		Task task1 = new Task("task1","some crazy ass description",null, TextResponse.class.toString());
		Task task2 = new Task("task2","some crazy ass description",null, TextResponse.class.toString());
		Task task3 = new Task("task3","some crazy ass description",null, TextResponse.class.toString());
		Task task4 = new Task("task3","some crazy ass description",null, TextResponse.class.toString());
		
		Task t1 = WebService.put(task1);
		assertTrue(task1.getName().equals(t1.getName()));
		Task t2 = WebService.put(task2);
		assertTrue(task2.getName().equals(t2.getName()));
		Task t3 = WebService.put(task3);
		assertTrue(task3.getName().equals(t3.getName()));
		Task t4 = WebService.put(task4);
		assertTrue(task4.getName().equals(t4.getName()));	
		
		List<Task> tasks = WebService.list();
		
		for(Task task : tasks)
		{
			Log.d("get", task.getName());
		}

		assertTrue(task1.getName().equals(tasks.get(0).getName()));
		assertTrue(task2.getName().equals(tasks.get(1).getName()));
		assertTrue(task3.getName().equals(tasks.get(2).getName()));
		assertTrue(task4.getName().equals(tasks.get(3).getName()));
		
		WebService.delete(t1.getId());
		WebService.delete(t2.getId());
		WebService.delete(t3.getId());
		WebService.delete(t4.getId());
	}
}

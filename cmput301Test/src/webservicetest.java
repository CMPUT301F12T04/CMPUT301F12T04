



import com.example.cmput301.Task;
import com.example.cmput301.WebService;

import junit.framework.TestCase;


public class webservicetest extends TestCase
{

	
	public void testAddTask()
	{
		Task task = new Task("task1","description1",null);
		Task newTask = WebService.post(task);
		assertTrue(task.getName().equals(newTask.getName()));
		assertTrue(task.getDescription().equals(newTask.getDescription()));
		WebService.deleteTask(newTask.getId());
		assertTrue(WebService.getTask(newTask.getId())==null);
	}
	
	public void testGetTask()
	{
		Task task = new Task("task1","description1",null);
		task = WebService.post(task);
		Task newTask = WebService.getTask(task.getId());
		assertTrue(task.getName().equals(newTask.getName()));
		assertTrue(task.getDescription().equals(newTask.getDescription()));
		WebService.deleteTask(newTask.getId());
		assertTrue(WebService.getTask(newTask.getId())==null);
	}
	
	public void testDeleteTask()
	{
		Task task = new Task("task1","description1",null);
		task = WebService.post(task);
		Task newTask = WebService.getTask(task.getId());
		assertTrue(task.getName().equals(newTask.getName()));
		assertTrue(task.getDescription().equals(newTask.getDescription()));
		WebService.deleteTask(newTask.getId());
		Task deletedTask = WebService.getTask(newTask.getId());
		assertTrue(deletedTask==null);
	}
	
}

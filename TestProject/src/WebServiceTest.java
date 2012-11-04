
import android.util.Log;
import com.example.cmput301.Response;
import com.example.cmput301.Task;
import com.example.cmput301.TextResponse;
import com.example.cmput301.WebService;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;

public class WebServiceTest extends TestCase {

    public void testWebServiceGetTask() {
    	
    	//create new task and add to webservice
        Task task = new Task("task1", "description1", null, TextResponse.class.toString());
        task = WebService.put(task);
        
        //get task with same id from webservice
        Task newTask = WebService.get(task.getId());
        
        //test to see if tasks are equal
        assertTrue(task.getName().equals(newTask.getName()));
        assertTrue(task.getDescription().equals(newTask.getDescription()));
        assertTrue(task.getType().equals(newTask.getType()));
        
        //delete task
        WebService.delete(newTask.getId());
        assertTrue(WebService.get(newTask.getId()) == null);
    }

    public void testAddResponse() {
    	
    	//c create new task and add to webservice
        Task task = new Task("Task without responses", "some crazy ass description", null, TextResponse.class.toString());
        assertTrue(task.getType().equals(TextResponse.class.toString()));
        
        // create 2 random responses
        Response r1 = new TextResponse("text response 1", new Date());
        Response r2 = new TextResponse("text response 2", new Date());

        // put task in webservice
        Task t = WebService.put(task);
        
        // add responses, get back task, and check if properly added
        t = WebService.post(t, r1);
        assertTrue(t.getResponses().get(0).getContent().equals(r1.getContent()));
       
        t = WebService.post(t, r2);
        assertTrue(t.getResponses().get(1).getContent().equals(r2.getContent()));
        
        // delete task and make sure deleted
        WebService.delete(t.getId());
        assertTrue(WebService.get(t.getId()) == null);
    }

    public void testAddTaskWithResponses() {
    	
    	// add task and create responses
        Task task = new Task("Task without responses", "some crazy ass description", null, TextResponse.class.toString());
        Response r1 = new TextResponse("text response 1", new Date());
        Response r2 = new TextResponse("text response 2", new Date());
        
        // add responses to task
        task.addResponse(r1);
        task.addResponse(r2);
        
        // put tasks on webservice
        Task newTask = WebService.put(task);

        // check if task was added
        assertTrue(task.getName().equals(newTask.getName()));
        assertTrue(task.getDescription().equals(newTask.getDescription()));
        assertTrue(task.getResponses().get(0).getContent().equals(newTask.getResponses().get(0).getContent()));
        assertTrue(task.getResponses().get(1).getContent().equals(newTask.getResponses().get(1).getContent()));

        // delete task and check if deleted
        WebService.delete(newTask.getId());
        assertTrue(WebService.get(newTask.getId()) == null);
    }

    public void testAddTaskWithNoResponses() {
    	
    	// create task and add to webservice
        Task task = new Task("Task without responses", "some crazy ass description", null, TextResponse.class.toString());
        Task newTask = WebService.put(task);
        
        // check if task was added
        assertTrue(task.getName().equals(newTask.getName()));
        assertTrue(task.getDescription().equals(newTask.getDescription()));

        // delete task and check if deleted
        WebService.delete(newTask.getId());
        assertTrue(WebService.get(newTask.getId()) == null);
    }

    public void testGetAll() {
    	
    	// nuke webservice
        WebService.nuke("judgedredd");
        
        // create tasks
        Task task1 = new Task("task1", "some crazy ass description", null, TextResponse.class.toString());
        Task task2 = new Task("task2", "some crazy ass description", null, TextResponse.class.toString());
        Task task3 = new Task("task3", "some crazy ass description", null, TextResponse.class.toString());
        Task task4 = new Task("task3", "some crazy ass description", null, TextResponse.class.toString());

        // add all tasks to web service and check if added
        Task t1 = WebService.put(task1);
        assertTrue(task1.getName().equals(t1.getName()));
        Task t2 = WebService.put(task2);
        assertTrue(task2.getName().equals(t2.getName()));
        Task t3 = WebService.put(task3);
        assertTrue(task3.getName().equals(t3.getName()));
        Task t4 = WebService.put(task4);
        assertTrue(task4.getName().equals(t4.getName()));

        // get all tasks from webserivce
        List<Task> tasks = WebService.list();

        // check if all tasks where added to webservice
        assertTrue(task1.getName().equals(tasks.get(0).getName()));
        assertTrue(task2.getName().equals(tasks.get(1).getName()));
        assertTrue(task3.getName().equals(tasks.get(2).getName()));
        assertTrue(task4.getName().equals(tasks.get(3).getName()));

        // delete all tasks from web service, and check if deleted.
        WebService.delete(t1.getId());
        assertTrue(WebService.get(t1.getId()) == null);
        WebService.delete(t2.getId());
        assertTrue(WebService.get(t2.getId()) == null);
        WebService.delete(t3.getId());
        assertTrue(WebService.get(t3.getId()) == null);
        WebService.delete(t4.getId());
        assertTrue(WebService.get(t4.getId()) == null);
    }
}
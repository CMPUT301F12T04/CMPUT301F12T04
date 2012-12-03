package TestProject.src;

import com.example.cmput301.model.Manager;
import com.example.cmput301.model.Task;
import com.example.cmput301.model.TaskLists;
import com.example.cmput301.model.response.TextResponse;

import TestProject.src.stubs.DatabaseManager;
import android.test.AndroidTestCase;

public class TestTaskList extends AndroidTestCase {
	Manager man;

	@Override
	public void setUp() {
		Manager.dbman = new DatabaseManager();
		man = new Manager(null);
	}
	
	public void testGetPrivateTasks() {
		TaskLists tl = new TaskLists();
		//Start clean.
		man.nukeLocal();
		man.nukeRemote();
		
		Task t1 = new Task("das", "fdas", TextResponse.class.toString());
        t1 = man.addTask(t1);

        assertEquals(1, tl.getPrivateTasks().size());
        assertEquals(0, tl.getSharedTasks().size());
	}
	
	public void testGetSharedTasks() {
		TaskLists tl = new TaskLists();

		//Start clean.
		man.nukeLocal();
		man.nukeRemote();
		
		Task t1 = new Task("das", "fdas", TextResponse.class.toString());
        t1.setStatus(Task.STATUS_SHARED);
        t1 = man.addTask(t1);

        assertEquals(0, tl.getPrivateTasks().size());
        assertEquals(1, tl.getSharedTasks().size());
	}
}

package TestProject.src;
import android.test.AndroidTestCase;
import android.util.Log;
import com.example.cmput301.DatabaseManager;
import com.example.cmput301.Task;
import com.example.cmput301.TaskManager;
import com.example.cmput301.TextResponse;
import com.example.cmput301.WebService;
import java.util.Date;

public class TestTaskManager extends AndroidTestCase {

    private static final String FILENAME = "database_tables";

    /**
     * Test the add task method of the Task Manager Class.
     */
    public void testAddTask() {

        TaskManager tman = new TaskManager(getContext());
        //Start clean.
        tman.nukeLocal();
        tman.nukeRemote();

        //Confirm preconditions
        assertEquals(0, tman.getLocalTasks().size());
        assertEquals(0, tman.getPrivateTasks().size());
        assertEquals(0, tman.getSharedTasks().size());
        assertEquals(0, tman.getUnansweredTasks().size());
        assertEquals(0, tman.getRemoteTasks().size());

        Task t1 = new Task("das", "fdas");
        t1 = tman.addTask(t1);

        assertEquals(1, tman.getLocalTasks().size());
        assertEquals(1, tman.getPrivateTasks().size());
        assertEquals(0, tman.getSharedTasks().size());
        assertEquals(0, tman.getUnansweredTasks().size());
        assertEquals(0, tman.getRemoteTasks().size());

        //Make sure the task added is the correct one.
        assertEquals(t1, tman.getLocalTask(t1.getId()));
        assertTrue(t1.getId().indexOf("local@") == 0);
        assertEquals(t1, tman.getPrivateTasks().get(0));
    }

    /**
     * Tests the share task method of the task manager class.
     */
    public void testShareTask() {
        TaskManager tman = new TaskManager(getContext());
        //Start clean.
        tman.nukeLocal();
        tman.nukeRemote();

        Task t1 = new Task("das", "fdas");
        t1 = tman.addTask(t1);

        assertEquals(1, tman.getLocalTasks().size());
        assertEquals(1, tman.getPrivateTasks().size());
        assertEquals(0, tman.getSharedTasks().size());
        assertEquals(0, tman.getUnansweredTasks().size());
        assertEquals(0, tman.getRemoteTasks().size());

        Task sharedTask = tman.shareTask(t1.getId());

        assertEquals(1, tman.getLocalTasks().size());
        assertEquals(0, tman.getPrivateTasks().size());
        assertEquals(1, tman.getSharedTasks().size());
        assertEquals(0, tman.getUnansweredTasks().size());
        assertEquals(0, tman.getRemoteTasks().size());

        //Make sure the task added is the correct one.
        assertEquals(sharedTask, tman.getLocalTask(sharedTask.getId()));
        assertEquals(sharedTask, tman.getSharedTasks().get(0));

        //Confirm id has changed
        assertTrue(!sharedTask.getId().equals(t1.getId()));

        //Confirm all other fields are the same.
        assertEquals(t1.getDescription(), sharedTask.getDescription());
        assertEquals(t1.getName(), sharedTask.getName());
        assertEquals(t1.getType(), sharedTask.getType());

    }

    /**
     * Tets the deleteTask method of the TaskManager class
     */
    public void testdeleteTask() {
        Task t1 = new Task("dsa", "dsa");
        Task t2 = new Task("dfasfas", "dfasfsad");

        TaskManager tman = new TaskManager(getContext());

        //Start Clean.
        tman.nukeLocal();
        tman.nukeRemote();

        assertEquals(0, tman.getLocalTasks().size());
        assertEquals(0, tman.getPrivateTasks().size());
        assertEquals(0, tman.getSharedTasks().size());
        assertEquals(0, tman.getUnansweredTasks().size());
        assertEquals(0, tman.getRemoteTasks().size());

        DatabaseManager dbman = new DatabaseManager(FILENAME, getContext());

        //Post local
        t1 = dbman.postLocal(t1);

        //Post a remote (can't be deleted.)
        t2 = WebService.put(t2);
        t2 = dbman.postRemote(t2);

        tman = new TaskManager(getContext()); //Refresh the dbmanager

        //Confirm entries;
        assertEquals(1, tman.getLocalTasks().size());
        assertEquals(1, tman.getPrivateTasks().size());
        assertEquals(0, tman.getSharedTasks().size());
        assertEquals(1, tman.getUnansweredTasks().size());
        assertEquals(1, tman.getRemoteTasks().size());

        //Now try to delete remote.
        boolean rval2 = tman.deleteTask(t2.getId());

        //Confirm nothing was deleted.
        assertTrue(!rval2);
        assertEquals(1, tman.getLocalTasks().size());
        assertEquals(1, tman.getPrivateTasks().size());
        assertEquals(0, tman.getSharedTasks().size());
        assertEquals(1, tman.getRemoteTasks().size());
        assertEquals(1, tman.getUnansweredTasks().size());

        //Now try to delete local.
        boolean rval = tman.deleteTask(t1.getId());

        //Confirm deletion
        assertTrue(rval);
        assertEquals(0, tman.getLocalTasks().size());
        assertEquals(0, tman.getPrivateTasks().size());
        assertEquals(0, tman.getSharedTasks().size());
        assertEquals(1, tman.getRemoteTasks().size());
        assertEquals(1, tman.getUnansweredTasks().size());



    }

    /**
     * Tests the post response method of the TaskManager class.
     */
    public void testPostResponse() {

        TaskManager tman = new TaskManager(getContext());

        //Start clean.
        tman.nukeLocal();
        tman.nukeRemote();

        //Note these will have 0 responses each.
        Task t1 = new Task("dfsa", "sad");
        Task t2 = new Task("dfsa", "dasfsa");

        DatabaseManager dbman = new DatabaseManager(FILENAME, getContext());

        t1 = dbman.postLocal(t1);
        t2 = WebService.put(t2);
        t2 = dbman.postRemote(t2);

        tman = new TaskManager(getContext()); //Refresh the dbmanager

        tman.postResponse(t1, new TextResponse("DFSADFS", new Date()));

        Log.w("YOYO", t1.getId());


        //Make sure Response added correctly
        t1 = tman.getLocalTask(t1.getId());
        t2 = tman.getRemoteTask(t2.getId());

        assertEquals(1, t1.getResponses().size());
        assertEquals(0, t2.getResponses().size());

        tman.postResponse(t2, new TextResponse("DFSADFS", new Date()));

        assertEquals(1, t1.getResponses().size());
        assertEquals(1, t2.getResponses().size());
    }
}

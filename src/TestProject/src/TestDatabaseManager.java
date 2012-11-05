package TestProject.src;

import android.content.Context;
import android.test.AndroidTestCase;
import com.example.cmput301.DatabaseManager;
import com.example.cmput301.Task;
import java.io.File;

public class TestDatabaseManager extends AndroidTestCase {

    private static final String FILENAME = "testDatabase";

    /**
     * Test the postLocal and PostRemote methods. Also tests the getTask
     * Methods.
     */
    public void testPostGetMethods() {
        DatabaseManager dbman = new DatabaseManager(FILENAME, getContext());
        assertEquals(dbman.getLocalTaskList().size(), 0);
        assertEquals(dbman.getRemoteTaskList().size(), 0);

        Task localPostA = new Task("A", "A");
        Task localPostB = new Task("B", "B");


        Task locPostedA = dbman.postLocal(localPostA);
        Task locPostedB = dbman.postLocal(localPostB);

        Task remotePostA = new Task("A", "A");
        remotePostA.setId("testRA");
        Task remotePostB = new Task("B", "B");
        remotePostB.setId("testRB");

        Task remPostedA = dbman.postRemote(remotePostA);
        Task remPostedB = dbman.postRemote(remotePostB);

        //Make sure they both have two elements.
        assertEquals(dbman.getLocalTaskList().size(), 2);
        assertEquals(dbman.getRemoteTaskList().size(), 2);

        //Make sure the Local tasks were given unique ids
        assertTrue(!locPostedA.getId().equals(locPostedB.getId()));
        assertTrue(locPostedA.getId().indexOf("local@") == 0);
        assertTrue(locPostedB.getId().indexOf("local@") == 0);

        //Now make sure they are in the list.
        assertTrue(dbman.getLocalTask(locPostedA.getId()) != null);
        assertEquals(dbman.getLocalTask(locPostedA.getId()), locPostedA);
        assertTrue(dbman.getLocalTask(locPostedB.getId()) != null);
        assertEquals(dbman.getLocalTask(locPostedB.getId()), locPostedB);

        //Repeat for Remote Tasks.
        assertTrue(dbman.getRemoteTask("testRA") != null);
        assertEquals(dbman.getRemoteTask("testRA"), remPostedA);
        assertTrue(dbman.getRemoteTask("testRB") != null);
        assertEquals(dbman.getRemoteTask("testRB"), remPostedB);

        //Clean up after tests
        dbman.nukeAll();
    }

    /**
     * Tests the delete and nuke methods
     */
    public void testDeleteAndNuke() {
        //Make sure that a new database starts empty.
        DatabaseManager dbman = new DatabaseManager(FILENAME, getContext());
        assertEquals(dbman.getLocalTaskList().size(), 0);
        assertEquals(dbman.getRemoteTaskList().size(), 0);

        Task deleteMe = dbman.postLocal(new Task("dasa", "dasds"));
        dbman.postLocal(new Task("fdsa", "fds"));
        dbman.postRemote(new Task("fddssa", "fdsads"));

        //Test Delete
        dbman.deleteLocalTask(deleteMe.getId());

        assertEquals(dbman.getLocalTaskList().size(), 1);
        assertEquals(dbman.getRemoteTaskList().size(), 1);
        assertTrue(!deleteMe.equals(dbman.getLocalTaskList().get(0)));

        //Test Nuke Local
        dbman.nukeLocal();

        assertEquals(dbman.getLocalTaskList().size(), 0);
        assertEquals(dbman.getRemoteTaskList().size(), 1);

        //Test Nuke Remote
        dbman.postLocal(new Task("fdsa", "fds"));
        dbman.nukeRemote();

        assertEquals(dbman.getLocalTaskList().size(), 1);
        assertEquals(dbman.getRemoteTaskList().size(), 0);


        //Test Nuke All
        dbman.postLocal(new Task("fdsa", "fds"));
        dbman.postRemote(new Task("fddssa", "fdsads"));

        dbman.nukeAll();
        assertEquals(dbman.getLocalTaskList().size(), 0);
        assertEquals(dbman.getRemoteTaskList().size(), 0);

        //Clean up after tests
        dbman.nukeAll();
    }

    /**
     * Tests the updateTask method.
     */
    public void testUpdateMethod() {
        //Make sure that a new database starts empty.
        DatabaseManager dbman = new DatabaseManager(FILENAME, getContext());
        assertEquals(dbman.getLocalTaskList().size(), 0);
        assertEquals(dbman.getRemoteTaskList().size(), 0);

        String titleBeforeA = "abc";
        String descBeforeA = "def";

        String titleBeforeB = "ghi";
        String descBeforeB = "jkl";

        String titleBeforeC = "mno";
        String descBeforeC = "pqr";


        Task taskA = new Task(titleBeforeA, descBeforeA);
        Task taskB = new Task(titleBeforeB, descBeforeB);
        Task taskC = new Task(titleBeforeC, descBeforeC);
        //Task C is going in remote so we need to give it an id.
        taskC.setId("123"); //ALl local start with local@ so this cant match them.


        //Post task A into local and remote.
        taskA = dbman.postLocal(taskA);
        taskB = dbman.postLocal(taskB);
        dbman.postRemote(taskC);
        dbman.postRemote(taskA);

        String titleAfterA = "stu";
        String descAfterA = "vwx";

        Task afterA = new Task(titleAfterA, descAfterA);
        afterA.setId(taskA.getId());

        dbman.updateTask(afterA);

        //Confirm it wokred on local table
        assertEquals(dbman.getLocalTask(taskA.getId()).getName(), titleAfterA);
        assertEquals(dbman.getLocalTask(taskA.getId()).getDescription(), descAfterA);
        assertEquals(dbman.getLocalTask(taskB.getId()).getName(), titleBeforeB);
        assertEquals(dbman.getLocalTask(taskB.getId()).getDescription(), descBeforeB);

        //Confirm it worked on remote table
        assertEquals(dbman.getRemoteTask(taskA.getId()).getName(), titleAfterA);
        assertEquals(dbman.getRemoteTask(taskA.getId()).getDescription(), descAfterA);
        assertEquals(dbman.getRemoteTask(taskC.getId()).getName(), titleBeforeC);
        assertEquals(dbman.getRemoteTask(taskC.getId()).getDescription(), descBeforeC);

        //Clean up after tests
        dbman.nukeAll();

    }

    /**
     * Tests the Database manager constructors.
     */
    public void testConstructor() {
        //Make sure that a new database starts empty.
        Context ctxt = getContext();
        DatabaseManager dbman = new DatabaseManager(FILENAME, ctxt);
        assertEquals(dbman.getLocalTaskList().size(), 0);
        assertEquals(dbman.getRemoteTaskList().size(), 0);

        dbman.postLocal(new Task("dasa", "dasds"));
        dbman.postLocal(new Task("fdsa", "fds"));
        dbman.postRemote(new Task("fddssa", "fdsads"));

        //Make sure that a non empty database has stuff in it when created.
        DatabaseManager dbman2 = new DatabaseManager(FILENAME, ctxt);
        assertEquals(dbman2.getRemoteTaskList().size(), 1);
        assertEquals(dbman2.getLocalTaskList().size(), 2);

        //Clean up after tests
        dbman.nukeAll();
    }
}

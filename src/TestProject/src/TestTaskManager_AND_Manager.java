package TestProject.src;

/*******************************************************************************
 * Copyright (c) 2012 Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu - initial API and                              
 *     implementation
 ******************************************************************************/

import com.example.cmput301.model.Manager;
import com.example.cmput301.model.Task;
import com.example.cmput301.model.TaskManager;
import com.example.cmput301.model.response.TextResponse;

import TestProject.src.stubs.DatabaseManager;
import android.test.AndroidTestCase;

public class TestTaskManager_AND_Manager extends AndroidTestCase {
	
	Manager man;
	
	@Override
	public void setUp() {
		Manager.dbman = new DatabaseManager();
		man = new Manager(null);
	}
	
	public void testAddTask() {
		TaskManager tman = new TaskManager();
        //Start clean.
        man.nukeLocal();
        man.nukeRemote();

        //Confirm preconditions
        assertEquals(0, man.getLocalTasks().size());
        assertEquals(0, man.getPrivateTasks().size());
        assertEquals(0, man.getSharedTasks().size());
        assertEquals(0, man.getUnansweredTasks().size());
        assertEquals(0, man.getRemoteTasks().size());

        Task t1 = new Task("das", "fdas", TextResponse.class.toString());
        t1 = tman.addTask(t1);

        assertEquals(1, man.getLocalTasks().size());
        assertEquals(1, man.getPrivateTasks().size());
        assertEquals(0, man.getSharedTasks().size());
        assertEquals(0, man.getUnansweredTasks().size());
        assertEquals(0, man.getRemoteTasks().size());

        //Make sure the task added is the correct one.
        assertEquals(t1, man.getLocalTask(t1.getId()));
        assertTrue(t1.getId().indexOf("local@") == 0);
        assertEquals(t1, man.getPrivateTasks().get(0));
	}

	public void testDeleteTask() {

		Task t1 = new Task("dsa", "dsa", TextResponse.class.toString());
        Task t2 = new Task("dfasfas", "dfasfsad", TextResponse.class.toString());

        TaskManager tman = new TaskManager();

        //Start Clean.
        man.nukeLocal();
        man.nukeRemote();

        assertEquals(0, man.getLocalTasks().size());
        assertEquals(0, man.getPrivateTasks().size());
        assertEquals(0, man.getSharedTasks().size());
        assertEquals(0, man.getUnansweredTasks().size());
        assertEquals(0, man.getRemoteTasks().size());


        //Post local
        t1 = man.dbman.postLocal(t1);

        //Post a remote (can't be deleted.)
        t2 =  man.dbman.postRemote(t2);

        //Confirm entries;
        assertEquals(1, man.getLocalTasks().size());
        assertEquals(1, man.getPrivateTasks().size());
        assertEquals(0, man.getSharedTasks().size());
        assertEquals(1, man.getUnansweredTasks().size());
        assertEquals(1, man.getRemoteTasks().size());

        //Now try to delete remote.
        boolean rval2 = man.deleteTask(t2.getId());

        //Confirm nothing was deleted.
        assertTrue(!rval2);
        assertEquals(1, man.getLocalTasks().size());
        assertEquals(1, man.getPrivateTasks().size());
        assertEquals(0, man.getSharedTasks().size());
        assertEquals(1, man.getRemoteTasks().size());
        assertEquals(1, man.getUnansweredTasks().size());

        //Now try to delete local.
        boolean rval = tman.deleteTask(t1.getId());

        //Confirm deletion
        assertTrue(rval);
        assertEquals(0, man.getLocalTasks().size());
        assertEquals(0, man.getPrivateTasks().size());
        assertEquals(0, man.getSharedTasks().size());
        assertEquals(1, man.getRemoteTasks().size());
        assertEquals(1, man.getUnansweredTasks().size());

	}

	public void testShareTask() {
		TaskManager tman = new TaskManager();
        //Start clean.
		man.nukeLocal();
		man.nukeRemote();

        Task t1 = new Task("das", "fdas", TextResponse.class.toString());
        t1 = tman.addTask(t1);

        assertEquals(1, man.getLocalTasks().size());
        assertEquals(1, man.getPrivateTasks().size());
        assertEquals(0, man.getSharedTasks().size());
        assertEquals(0, man.getUnansweredTasks().size());
        assertEquals(0, man.getRemoteTasks().size());

        Task sharedTask = tman.shareTask(t1.getId());

        assertEquals(1, man.getLocalTasks().size());
        assertEquals(0, man.getPrivateTasks().size());
        assertEquals(1, man.getSharedTasks().size());
        assertEquals(0, man.getUnansweredTasks().size());
        assertEquals(0, man.getRemoteTasks().size());

        //Make sure the task added is the correct one.
        assertEquals(sharedTask, man.getLocalTask(sharedTask.getId()));
        assertEquals(sharedTask, man.getSharedTasks().get(0));

        //Confirm id has changed
        assertTrue(!sharedTask.getId().equals(t1.getId()));

        //Confirm all other fields are the same.
        assertEquals(t1.getDescription(), sharedTask.getDescription());
        assertEquals(t1.getName(), sharedTask.getName());
        assertEquals(t1.getType(), sharedTask.getType());
	}

}

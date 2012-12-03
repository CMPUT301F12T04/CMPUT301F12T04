package TestProject.src;

import java.util.Date;

import com.example.cmput301.model.Manager;
import com.example.cmput301.model.ResponseManager;
import com.example.cmput301.model.Task;
import com.example.cmput301.model.response.TextResponse;

import TestProject.src.stubs.DatabaseManager;
import android.test.AndroidTestCase;

public class TestResponseManager extends AndroidTestCase {
	Manager man;

	@Override
	public void setUp() {
		Manager.dbman = new DatabaseManager();
		man = new Manager(null);
	}
	
	public void testVoteTask() {
		
		ResponseManager rm = new ResponseManager();
		//Start clean.
		man.nukeLocal();
		man.nukeRemote();
		
		Task t1 = new Task("das", "fdas", TextResponse.class.toString());
	    t1 = man.addTask(t1);
	    
	    rm.voteTask(t1);
	    assertEquals(1, man.getLocalTask(t1.getId()).getVotes());
	    
	    rm.voteTask(t1);
	    assertEquals(2, man.getLocalTask(t1.getId()).getVotes());

	}
	
	public void testPostResponse() {
		ResponseManager rm = new ResponseManager();
		//Start clean.
		man.nukeLocal();
		man.nukeRemote();
		
		Task t1 = new Task("das", "fdas", TextResponse.class.toString());
	    t1 = man.addTask(t1);
	    
	    rm.postResponse(t1, new TextResponse("a", new Date(), "content"));
	    
	    //Ensure 1 response was added.
	    assertEquals(1, man.getLocalTask(t1.getId()).getResponses().size());
	    //Ensure that conetent is as expercted.
	    assertEquals("content", man.getLocalTask(t1.getId()).getResponses().get(0).getContent());

	}	
}

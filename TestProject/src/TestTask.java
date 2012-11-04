
import com.example.cmput301.Response;
import com.example.cmput301.Task;
import com.example.cmput301.TextResponse;
import java.util.ArrayList;
import java.util.Date;
import junit.framework.TestCase;

public class TestTask extends TestCase {

    /**
     * Tests the get and set methods for a Task object. Also tests the most
     * robust Constructor.
     */
    public void testGettersAndSetters() {
        //Create fields for the new task.
        String nameBefore = "aName";
        String descBefore = "aDescription";
        String idBefore = "anId";
        int statusBefore = Task.STATUS_PRIVATE;
        ArrayList<Response> responsesBefore = new ArrayList<Response>();
        Response respBefore = new TextResponse("aString", new Date());
        responsesBefore.add(respBefore);


        Task task = new Task(nameBefore, descBefore, idBefore, statusBefore, responsesBefore);

        //Make sure constructor worked.
        assertEquals(task.getName(), nameBefore);
        assertEquals(task.getDescription(), descBefore);
        assertEquals(task.getId(), idBefore);
        assertEquals(task.getStatus(), statusBefore);

        assertEquals(task.getResponses().size(), 1);
        if (task.getResponses().size() >= 1) {
            assertEquals(task.getResponses().get(0), respBefore);
        }

        //Create fields that need to be set.
        String idAfter = idBefore + "After";
        int statusAfter = Task.STATUS_SHARED;
        Response addedResponse = new TextResponse("anotherString", new Date());

        //Test Sets and Gets
        task.setId(idAfter);
        task.setStatus(statusAfter);
        task.addResponse(addedResponse);

        //Make sure changes took affect.
        assertEquals(task.getId(), idAfter);
        assertEquals(task.getStatus(), statusAfter);

        //Show that the status Private and Shared are different.
        assertTrue(task.getStatus() != statusBefore);
        assertEquals(task.getResponses().size(), 2);
        if (task.getResponses().size() >= 2) {
            ArrayList<Response> list = (ArrayList<Response>) task.getResponses();
            assertEquals(list.get(0), respBefore);
            assertEquals(list.get(1), addedResponse);
            //Show they are not equal to eachother.
            assertTrue(!list.get(0).equals(list.get(1)));
        }

    }

    public void testConstructors() {
        String name = "aName";
        String desc = "aDescription";
        String id = "anId";
        int status = Task.STATUS_PRIVATE;
        ArrayList<Response> responses = new ArrayList<Response>();
        Response resp = new TextResponse("aString", new Date());
        responses.add(resp);

        //Test the robust constructor
        Task task = new Task(name, desc, id, status, responses);
        assertEquals(task.getName(), name);
        assertEquals(task.getDescription(), desc);
        assertEquals(task.getId(), id);
        assertEquals(task.getStatus(), status);
        //Automatically should be set to this.
        assertEquals(task.getType(), TextResponse.class.toString());

        assertEquals(task.getResponses().size(), 1);
        if (task.getResponses().size() >= 1) {
            assertEquals(task.getResponses().get(0), resp);
        }

        //Test the simple constructor
        Task anotherTask = new Task(name, desc);
        assertEquals(anotherTask.getName(), name);
        assertEquals(anotherTask.getDescription(), desc);
        assertEquals(anotherTask.getId(), null);
        assertEquals(task.getStatus(), Task.STATUS_PRIVATE);
        assertEquals(task.getResponses().size(), 0);

    }
}

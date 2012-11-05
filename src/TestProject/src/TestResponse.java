package TestProject.src;
import com.example.cmput301.TextResponse;
import java.util.Date;
import junit.framework.TestCase;

public class TestResponse extends TestCase {

    /**
     * Tests all the set/get methods and constructors of the TextResponses
     */
    public void testTextResponse() {
        String contentBefore = "someContent";
        Date timestampBefore = new Date();

        TextResponse resp = new TextResponse(contentBefore, timestampBefore);
        assertEquals(resp.getContent(), contentBefore);
        assertEquals(resp.getTimestamp(), timestampBefore);

        //Test setters
        String contentAfter = contentBefore + "After";
        Date timestampAfter = new Date();

        resp.setContent(contentAfter);
        resp.setTimestamp(timestampAfter);

        assertEquals(resp.getContent(), contentAfter);
        assertEquals(resp.getTimestamp(), timestampAfter);

    }

    public void TestPhotoResponse() {
        //Doesn't make sense to test this until we decide to implement it...
    }

    public void testAudioResponse() {
        //Doesn't make sense to test this until we decide to implement it...
    }

    public void test_Supercalifragilisticexpialidocious_Responses() {
        //Doesn't make sense to test this until we decide to implement it...
    }
}

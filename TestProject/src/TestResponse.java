
import com.example.cmput301.TextResponse;
import java.util.Date;
import junit.framework.TestCase;

public class TestResponse extends TestCase {
    
    /**
     * Tests all the set/get methods and constructors. This object has no other 
     * methods.
     */
    public void testAll() {
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
    
}

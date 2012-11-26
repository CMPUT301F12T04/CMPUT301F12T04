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
package tests;
import com.example.cmput301.model.TextResponse;
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

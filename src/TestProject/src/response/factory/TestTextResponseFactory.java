package TestProject.src.response.factory;

import java.util.Date;
import java.util.UUID;

import com.example.cmput301.model.response.TextResponse;
import com.example.cmput301.model.response.factory.TextResponseFactory;

import android.test.AndroidTestCase;

public class TestTextResponseFactory extends AndroidTestCase {

	public void testCreateResponseObject() {
		String annotation = UUID.randomUUID().toString();

		// Using a proper string.
		TextResponseFactory fact = new TextResponseFactory();
		
		//Test passing in an object that is not a string.
		TextResponse invalid = fact.createResponse(annotation, new Object());
		assertEquals(null, invalid);

	}

	public void testCreateResponseString() {
		String annotation = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();

		// Using a proper string.
		TextResponseFactory fact = new TextResponseFactory();

		Date date = new Date();

		TextResponse tr = fact.createResponse(annotation, content);

		assertEquals(annotation, tr.getAnnotation());
		assertEquals(content, tr.getContent());
		assertTrue(Math.abs(date.getTime() - tr.getTimestamp().getTime()) < 1000);
	}
}

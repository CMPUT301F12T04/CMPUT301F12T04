package TestProject.src.response.factory;

import java.util.Date;
import java.util.UUID;

import com.example.cmput301.model.response.PictureResponse;
import com.example.cmput301.model.response.TextResponse;
import com.example.cmput301.model.response.factory.PictureResponseFactory;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;

public class TestPhotoResponseFactory extends AndroidTestCase {

	public void testCreateResponseObject() {
		String annotation = UUID.randomUUID().toString();

		// Using a proper string.
		PictureResponseFactory fact = new PictureResponseFactory();
		
		//Test passing in an object that is not a string.
		PictureResponse invalid = fact.createResponse(annotation, new Object());
		assertEquals(null, invalid);
	}

	public void testCreateResponeString() {
		String annotation = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();

		// Using a proper string.
		PictureResponseFactory fact = new PictureResponseFactory();

		Date date = new Date();

		PictureResponse tr = fact.createResponse(annotation, content);

		assertEquals(annotation, tr.getAnnotation());
		assertEquals(content, tr.getSaveable());
		assertTrue(Math.abs(date.getTime() - tr.getTimestamp().getTime()) < 1000);
	}
}

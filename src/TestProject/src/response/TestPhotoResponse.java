package TestProject.src.response;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import com.example.cmput301.model.response.PictureResponse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

public class TestPhotoResponse extends AndroidTestCase {

	public void testStringConstructor() {
		Random r = new Random();

		byte[] content = new byte[10];
		r.nextBytes(content);
		
		String cString = "";
		for(int i = 0; i < content.length; i++) {
			cString += content;
			
			if(i != content.length - 1) {
				cString += ",";
			}
		}
		
		String annotation = UUID.randomUUID().toString();
		Date date = new Date();
		
		PictureResponse pr = new PictureResponse(annotation, date, cString);
		
		assertEquals(annotation, pr.getAnnotation());
		assertEquals(date, pr.getTimestamp());
		assertEquals(cString, pr.getSaveable());
	}

	public void testGetSaveable() {
		Random r = new Random();

		byte[] content = new byte[10];
		r.nextBytes(content);
		
		String cString = "";
		for(int i = 0; i < content.length; i++) {
			cString += content;
			
			if(i != content.length - 1) {
				cString += ",";
			}
		}
		
		String annotation = UUID.randomUUID().toString();
		Date date = new Date();
		
		PictureResponse pr = new PictureResponse(annotation, date, cString);
		
		assertEquals(annotation, pr.getAnnotation());
		assertEquals(date, pr.getTimestamp());
		assertEquals(cString, pr.getSaveable());
	}

	public void testGetAnnotation() {
		String annotation = UUID.randomUUID().toString();
		Date date = new Date();
		
		PictureResponse pr = new PictureResponse(annotation, date, "Hello");
		
		assertEquals(annotation, pr.getAnnotation());
	}

	public void testGetTimestamp() {
		String annotation = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();
		Date date = new Date();
		PictureResponse resp = new PictureResponse(annotation, date, content);
		
		assertEquals(date, resp.getTimestamp());
	}

	public void testSetAnnotation() {
		String annotationBefore = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();
		Date date = new Date();
		PictureResponse resp = new PictureResponse(annotationBefore, date, content);
		
		String annotationAfter = UUID.randomUUID().toString();
		
		while(annotationAfter == annotationBefore) {
			 annotationAfter = UUID.randomUUID().toString();
		}
		
		resp.setAnnotation(annotationAfter);
		
		assertEquals(annotationAfter, resp.getAnnotation());
	}

	public void testSetConent() {
		String annotation = UUID.randomUUID().toString();
		String contentBefore = UUID.randomUUID().toString();
		Date date = new Date();
		PictureResponse resp = new PictureResponse(annotation, date, contentBefore);
		
		String contentAfter = UUID.randomUUID().toString();
		
		while(contentAfter == contentBefore) {
			contentAfter = UUID.randomUUID().toString();
		}
		
		resp.setContent(contentAfter);
		
		assertEquals(contentAfter, resp.getSaveable());
	}

	public void testSetTimestamp() {
		String annotation = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();
		Date dateBefore = new Date();
		PictureResponse resp = new PictureResponse(annotation, dateBefore, content);
		
		Date dateAfter = new Date();
		
		resp.setTimestamp(dateAfter);		
		assertEquals(dateAfter, resp.getTimestamp());
	}
	
}

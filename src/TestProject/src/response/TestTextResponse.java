package TestProject.src.response;

import java.util.Date;
import java.util.UUID;

import com.example.cmput301.model.response.TextResponse;

import android.test.AndroidTestCase;

public class TestTextResponse extends AndroidTestCase {

	public void testGetContent() {
		String annotation = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();
		Date date = new Date();
		TextResponse resp = new TextResponse(annotation, date, content);
		
		assertEquals(content, resp.getContent());
	}

	public void testGetSaveable() {
		String annotation = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();
		Date date = new Date();
		TextResponse resp = new TextResponse(annotation, date, content);
		
		assertEquals(content, resp.getSaveable());
	}

	public void testGetAnnotation() {
		String annotation = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();
		Date date = new Date();
		TextResponse resp = new TextResponse(annotation, date, content);
		
		assertEquals(annotation, resp.getAnnotation());
	}

	public void testGetTimestamp() {
		String annotation = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();
		Date date = new Date();
		TextResponse resp = new TextResponse(annotation, date, content);
		
		assertEquals(date, resp.getTimestamp());
	}

	public void testSetAnnotation() {
		String annotationBefore = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();
		Date date = new Date();
		TextResponse resp = new TextResponse(annotationBefore, date, content);
		
		String annotationAfter = UUID.randomUUID().toString();
		
		while(annotationAfter == annotationBefore) {
			 annotationAfter = UUID.randomUUID().toString();
		}
		
		resp.setAnnotation(annotationAfter);
		
		assertEquals(annotationAfter, resp.getAnnotation());
	}

	public void testSetContent() {
		String annotation = UUID.randomUUID().toString();
		String contentBefore = UUID.randomUUID().toString();
		Date date = new Date();
		TextResponse resp = new TextResponse(annotation, date, contentBefore);
		
		String contentAfter = UUID.randomUUID().toString();
		
		while(contentAfter == contentBefore) {
			contentAfter = UUID.randomUUID().toString();
		}
		
		resp.setContent(contentAfter);
		
		assertEquals(contentAfter, resp.getContent());
	}

	public void testSetTimestamp() {

		String annotation = UUID.randomUUID().toString();
		String content = UUID.randomUUID().toString();
		Date dateBefore = new Date();
		TextResponse resp = new TextResponse(annotation, dateBefore, content);
		
		Date dateAfter = new Date();
		
		resp.setTimestamp(dateAfter);		
		assertEquals(dateAfter, resp.getTimestamp());
	}
}

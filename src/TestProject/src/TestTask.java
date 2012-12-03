package TestProject.src;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.example.cmput301.model.Task;
import com.example.cmput301.model.response.PictureResponse;
import com.example.cmput301.model.response.Response;
import com.example.cmput301.model.response.TextResponse;

/*******************************************************************************
 * Copyright (c) 2012 Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 * Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu - initial API and
 * implementation
 ******************************************************************************/
import android.test.AndroidTestCase;

public class TestTask extends AndroidTestCase {

	public void testConstructors() {
		
	}
	
	public void testAddResponse() {
		
		String name = UUID.randomUUID().toString();
		String description = UUID.randomUUID().toString();
		String type = TextResponse.class.toString();

		Task t = new Task(name, description, type);
		
		String trAnnotation = UUID.randomUUID().toString();
		Date date = new Date();
		String trContent = UUID.randomUUID().toString();
		
		t.addResponse(new TextResponse(trAnnotation, date, trContent));
		
		assertEquals(1, t.getResponses().size());
		assertEquals(trContent, t.getResponses().get(0).getContent());
		
		String prAnnotation = UUID.randomUUID().toString();
		String prContent = UUID.randomUUID().toString();

		t.addResponse(new PictureResponse(prAnnotation, date, prContent));
		assertEquals(1, t.getResponses().size());
		assertEquals(trContent, t.getResponses().get(0).getContent());
	}

	public void testGetDescription() {
		String name = UUID.randomUUID().toString();
		String description = UUID.randomUUID().toString();
		String type = TextResponse.class.toString();

		Task t = new Task(name, description, type);
		
		assertEquals(description, t.getDescription());
	}

	public void testGetId() {
		String name = UUID.randomUUID().toString();
		String description = UUID.randomUUID().toString();
		String type = TextResponse.class.toString();
		String id =  UUID.randomUUID().toString();
		
		Task t = new Task(name, description, id, type);
		
		assertEquals(id, t.getId());
	}

	public void testGetName() {
		String name = UUID.randomUUID().toString();
		String description = UUID.randomUUID().toString();
		String type = TextResponse.class.toString();
		
		Task t = new Task(name, description, type);
		
		assertEquals(name, t.getName());
	}

	public void tetGetResponses() {

		String name = UUID.randomUUID().toString();
		String description = UUID.randomUUID().toString();
		String type = TextResponse.class.toString();

		Task t = new Task(name, description, type);
		
		String trAnnotation1 = UUID.randomUUID().toString();
		Date date1 = new Date();
		String trContent1 = UUID.randomUUID().toString();
		
		String trAnnotation2 = UUID.randomUUID().toString();
		Date date2 = new Date();
		String trContent2 = UUID.randomUUID().toString();
		
		t.addResponse(new TextResponse(trAnnotation1, date1, trContent1));
		t.addResponse(new TextResponse(trAnnotation2, date2, trContent2));

		assertEquals(2, t.getResponses().size());
		assertEquals(trContent1, t.getResponses().get(0).getContent());
		assertEquals(trContent2, t.getResponses().get(1).getContent());

	}

	public void testGetSetStatus() {
		String name = UUID.randomUUID().toString();
		String description = UUID.randomUUID().toString();
		String type = TextResponse.class.toString();
		
		Task t = new Task(name, description, type);
		t.setStatus(Task.STATUS_PRIVATE);
		
		assertEquals(Task.STATUS_PRIVATE, t.getStatus());
		
		t.setStatus(Task.STATUS_SHARED);
		
		assertEquals(Task.STATUS_SHARED, t.getStatus());
	}

	public void testGetType() {

		String name = UUID.randomUUID().toString();
		String description = UUID.randomUUID().toString();
		String type = TextResponse.class.toString();
		
		Task t = new Task(name, description, type);
		
		assertEquals(type, t.getType());
	}

	public void testGetVotes() {

		String name = UUID.randomUUID().toString();
		String description = UUID.randomUUID().toString();
		String type = TextResponse.class.toString();
		String id =  UUID.randomUUID().toString();


		Task t = new Task(name, description, id, Task.STATUS_PRIVATE,
				new ArrayList<Response>(), type, 5);
		
		assertEquals(5, t.getVotes());
	}

	public void testIncreaseVotes() {
		String name = UUID.randomUUID().toString();
		String description = UUID.randomUUID().toString();
		String type = TextResponse.class.toString();
		String id =  UUID.randomUUID().toString();


		Task t = new Task(name, description, id, Task.STATUS_PRIVATE,
				new ArrayList<Response>(), type, 0);

		t.increaseVotes();
		
		assertEquals(1, t.getVotes());

	}

	public void testSetId() {
		String name = UUID.randomUUID().toString();
		String description = UUID.randomUUID().toString();
		String type = TextResponse.class.toString();
		String id =  UUID.randomUUID().toString();


		Task t = new Task(name, description, id, Task.STATUS_PRIVATE,
				new ArrayList<Response>(), type, 0);

		String idAfter =  UUID.randomUUID().toString();
		while(id == idAfter) {
			idAfter =  UUID.randomUUID().toString();
		}
		
		t.setId(idAfter);
		
		assertEquals(idAfter, t.getId());
	}


}

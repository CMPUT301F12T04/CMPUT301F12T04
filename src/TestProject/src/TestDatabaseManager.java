package TestProject.src;

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

import android.test.InstrumentationTestCase;
import android.test.IsolatedContext;
import android.util.Log;

public class TestDatabaseManager extends InstrumentationTestCase {

	
	public void testDeleteLocalTask() {
	
		IsolatedContext ctxt = new IsolatedContext(null, getInstrumentation().getContext());
		Log.d("SHIT FUCK MOTHER FUCKER", "" + ctxt.getApplicationContext());
	}

	public void testGetLocalTask() {

		fail();
	}

	public void testGetLocalTaskList() {

		fail();
	}

	public void testGetRemoteTask() {

		fail();
	}

	public void testGetRemoteTaskList() {

		fail();
	}

	public void testHideTask() {

		fail();
	}

	public void testNukeAll() {

		fail();
	}

	public void testNukeLocal() {

		fail();
	}

	public void tetNukeRemote() {

		fail();
	}

	public void testPostLocal() {

		fail();
	}

	public void testPostRemote() {

		fail();
	}

	public void testUpdateTask() {

		fail();
	}
}

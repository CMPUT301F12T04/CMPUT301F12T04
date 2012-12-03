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
package com.example.cmput301.view;

import java.util.ArrayList;
import java.util.List;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.cmput301.model.*;
import com.example.cmput301.model.response.Response;
import com.example.cmput301.model.response.TextResponse;
import com.example.cmput301.controller.*;
import com.example.cmput301.R;

/**
 * This class is responsible for displaying the selected task that was selected
 * via MainActivity. The task title and description is displayed along with it's
 * responses (which is in a list form). There is an option to share the task and
 * to add a response to the task. An option to go back to MainActivity is also
 * available.
 * 
 * @author dyu2
 */
@TargetApi(15)
public class IndividualTaskView extends Activity {

	ProgressBar progressBar;
	private IndividualTaskController itController;
	Task t;
	int taskPos;

	/**
	 * Sets the main view of the selected task, the task is passed in and it's
	 * content is displayed on the screen
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_task_view);

		itController = new IndividualTaskController(this.getApplicationContext(), this);

		String taskTile;
		String taskDesc;

		// enable back button on action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Getting information from bundle passed from MainActivity
		Bundle bundle = getIntent().getExtras();
		t = (Task) bundle.getSerializable("task");
		taskTile = t.getName();
		taskDesc = t.getDescription();
		taskPos = bundle.getInt("id");

		// setting the description of the task
		TextView title = (TextView) findViewById(R.id.indvidual_des_view);
		title.setText("Votes: " + t.getVotes() + "\n\n" + taskDesc);

		// setting the title of the task to be the activity title
		setTitle(taskTile);

		ListView responses = (ListView) findViewById(R.id.individual_res_list);

		List<Response> r = itController.getTask(t.getId()).getResponses();

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<TextResponse> adapter = new ArrayAdapter<TextResponse>(
				this, android.R.layout.simple_list_item_1, (ArrayList) r);
		Log.d("RESPONSELIST", "" + r.size());

		responses.setAdapter(adapter);
	}

	/**
	 * Using custom options in action bar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.individual_task_view, menu);
		
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();

		t = itController.getTask(t.getId());
		List<Response> r = t.getResponses();

		ListView responses = (ListView) findViewById(R.id.individual_res_list);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<TextResponse> adapter = new ArrayAdapter<TextResponse>(
				this, android.R.layout.simple_list_item_1,
				(ArrayList) r);
		responses.setAdapter(adapter);

	}

	@Override
	public void onDestroy() {
		Log.d("REMOTE", "INDV TASK VIEW ENDING");
		super.onDestroy();
	}

	/**
	 * Method used to handle various user selected commands. These commands
	 * include uploading a task, responding to a task and Going back to the
	 * previous screen.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// do upload, currently set to just kill activity
		if (item.getItemId() == R.id.menu_upload) {
			itController.shareTask(t.getId());

			finish();
		}
		if (item.getItemId() == R.id.menu_respond) {
			// Should launch the respond to a task activity here.
			Intent in = new Intent(IndividualTaskView.this,
					TextResponseView.class);
			Bundle bundle = new Bundle();
			bundle.putInt("id", taskPos);
			bundle.putSerializable("task", t);
			in.putExtras(bundle);
			startActivity(in);
		}
		if (item.getItemId() == R.id.menu_vote) {
			itController.voteTask(t);
			TextView title = (TextView) findViewById(R.id.indvidual_des_view);
			title.setText("Votes: " + t.getVotes() + "\n\n"
					+ t.getDescription());
		}

		if (item.getItemId() == R.id.menu_delete) {
			itController.deleteTask(t.getId());
			finish();
		}
		
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem shareButton = menu.findItem(R.id.menu_upload);
		MenuItem deleteButton = menu.findItem(R.id.menu_delete);
	    if(t.getStatus() == Task.STATUS_SHARED)
	    {
	    	shareButton.setEnabled(false);
	    	shareButton.setVisible(false);	
	    	deleteButton.setEnabled(false);
	    	deleteButton.setVisible(false);	
	    }
			
		return true;
	}
}

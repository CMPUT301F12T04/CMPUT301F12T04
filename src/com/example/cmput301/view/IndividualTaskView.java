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

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.cmput301.model.*;
import com.example.cmput301.controller.*;
import com.example.cmput301.R;

/**
 * This class is responsible for displaying the selected task that was selected
 * via MainActivity. The task title and description is displayed along with it's
 * text responses (which is in a list form). There is an option to share the task and
 * to add a response to the task. An option to go back to MainActivity is also
 * available. Added options include upvoting the task.
 */
@TargetApi(15)
public class IndividualTaskView extends Activity {

	ProgressBar progressBar;
	private IndividualTaskController itController;
	Task aTask;
	int taskPos;

	/**
	 * Sets the main view of the selected task, the task is passed in and its
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
		aTask = (Task) bundle.getSerializable("task");
		taskTile = aTask.getName();
		taskDesc = aTask.getDescription();
		taskPos = bundle.getInt("id");

		// setting the description of the task

		TextView desc = (TextView) findViewById(R.id.textView2);
		desc.setText("Description: " + taskDesc);
		TextView votes = (TextView) findViewById(R.id.textView3);
		votes.setText("Votes: " + aTask.getVotes());
		

		// setting the title of the task to be the activity title
		setTitle(taskTile);

		ListView responses = (ListView) findViewById(R.id.individual_res_list);		
		responses.setAdapter(new TextAdapter(this, aTask));
	}

	/**
	 * Using custom options in action bar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.individual_task_view, menu);		
		return true;
	}

	/**
	 * Refresh the view on any newly added responses
	 */
	@Override
	public void onResume() {
		super.onResume();


		aTask = itController.getTask(aTask.getId());


		//update list view, reset adapter
		ListView responses = (ListView) findViewById(R.id.individual_res_list);
	
		responses.setAdapter(new TextAdapter(this, aTask));

	}

	@Override
	public void onDestroy() {
		Log.d("REMOTE", "INDV TASK VIEW ENDING");
		super.onDestroy();
	}

	/**
	 * Method used to handle various user selected commands. These commands
	 * include uploading a task, responding to a task and Going back to the
	 * previous screen ,up voting a task, and deleting a task.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		//Do upload
		if (item.getItemId() == R.id.menu_upload) {
			itController.shareTask(aTask.getId());
			finish();
		}
		//Launch the respond to a task activity
		if (item.getItemId() == R.id.menu_respond) {
			Intent in = new Intent(IndividualTaskView.this,
					TextResponseView.class);
			Bundle bundle = new Bundle();
			bundle.putInt("id", taskPos);
			bundle.putSerializable("task", aTask);
			in.putExtras(bundle);
			startActivity(in);
		}
		//Do an up vote
		if (item.getItemId() == R.id.menu_vote) {
			itController.voteTask(aTask);
			TextView votes = (TextView) findViewById(R.id.textView3);
			votes.setText("Votes: " + aTask.getVotes());
		}
		//Delete the task
		if (item.getItemId() == R.id.menu_delete) {
			itController.deleteTask(aTask.getId());
			finish();
		}
		//go back, kill activity
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}

	/**
	 * Overrided method used to check and see if task has been shared,
	 * if so then disable the share and delete button.
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem shareButton = menu.findItem(R.id.menu_upload);
		MenuItem deleteButton = menu.findItem(R.id.menu_delete);
	    if(aTask.getStatus() == Task.STATUS_SHARED)
	    {
	    	shareButton.setEnabled(false);
	    	shareButton.setVisible(false);	
	    	deleteButton.setEnabled(false);
	    	deleteButton.setVisible(false);	
	    }
			
		return true;
	}
	

	
}

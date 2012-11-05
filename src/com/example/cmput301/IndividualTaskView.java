package com.example.cmput301;


import java.util.ArrayList;
import java.util.Date;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * This class is responsible for displaying the selected task that was selected
 * via MainActivity. The task title and description is displayed along with it's
 * responses (which is in a list form). There is an option to share the task and
 * to add a response to the task. An option to go back to MainActivity is
 * also available.
 *   
 * @author dyu2
 */
@TargetApi(15)
public class IndividualTaskView extends Activity {

	Task t;
	int taskPos;
	/**
	 * Sets the main view of the selected task, the task is passed in 
	 * and it's content is displayed on the screen
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_task_view);
		
		String taskTile;
		String taskDesc;
		 //maybe useful for figuring out position of task in the list
		
		
		//enable back button on action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//Getting information from bundle passed from MainActivity
		Bundle bundle = getIntent().getExtras();
		t = (Task) bundle.getSerializable("task");
		taskTile = t.getName();
		taskDesc = t.getDescription();
		taskPos = bundle.getInt("id");
		
		//setting the description of the task
		TextView title = (TextView) findViewById(R.id.indvidual_des_view);
		title.setText(taskDesc);
		
		//setting the title of the task to be the activity title
		setTitle(taskTile);
		
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//A fake response added to the task. Should delete this when real responses can be added
		//Another custom adapter will be needed later on here.
        TextResponse tr = new TextResponse("response1", new Date(System.currentTimeMillis()));
        t.addResponse(tr);
		ListView responses = (ListView) findViewById(R.id.individual_res_list);
		ArrayAdapter<TextResponse> adapter = new ArrayAdapter<TextResponse>(this,
    			android.R.layout.simple_list_item_1, (ArrayList) t.getResponses());
		responses.setAdapter(adapter);
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


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
	 * Method used to handle various user selected commands.
	 * These commands include uploading a task, responding to a task and
	 * Going back to the previous screen.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        //do upload, currently set to just kill activity
		if(item.getItemId() == R.id.menu_upload) {
			finish();
		}
		if(item.getItemId() == R.id.menu_respond )
		{
			//Should launch the respond to a task activity here. 
			Intent in = new Intent(IndividualTaskView.this,TextResponseView.class);
			Bundle bundle = new Bundle();
			bundle.putInt("id", taskPos);
			bundle.putSerializable("task",t);
			in.putExtras(bundle);
			startActivity(in);	
		}
		
		//go back, kills activity
		if(item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}
}

package com.example.cmput301;


import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

@TargetApi(15)
public class TextResponseView extends ResponseView {

	String responseString;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_response_view);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		String taskTile;
		int taskPos; //maybe useful for figuring out position of task in the list

		
		//Getting information from bundle passed from MainActivity
		Task t;
		Bundle bundle = getIntent().getExtras();
		t = (Task) bundle.getSerializable("task");
		taskTile = t.getName();
		taskPos = bundle.getInt("id");

		//setting the title of the task to be the activity title
		setTitle(taskTile);


		
		//
		final EditText responseInput;
		responseInput = (EditText) findViewById(R.id.textResponseInput);
		
		
		Button postBut;
		postBut = (Button) findViewById(R.id.postResponseButton);
		postBut.setOnClickListener(new View.OnClickListener()
		{
			//Post has been clicked.
			public void onClick(View v) {
				
				//Can now add response to the task using this string
				responseString = responseInput.getText().toString();
				// create new response here etc.
			}     

		});
		
		//setting list of responses for task
		ListView responses = (ListView) findViewById(R.id.text_response_list);
		ArrayAdapter<TextResponse> adapter = new ArrayAdapter<TextResponse>(this,
    			android.R.layout.simple_list_item_1, (ArrayList) t.getResponses());
		responses.setAdapter(adapter);
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		//go back, kills activity
		if(item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}
	
	
}

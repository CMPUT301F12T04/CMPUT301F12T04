package com.example.cmput301;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

@TargetApi(11)
public class IndividualTaskView extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_task_view);
		
		//enable back button on actionbar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.individual_task_view, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {

        //do upload, currently set to just kill activity
		if(item.getItemId() == R.id.menu_upload) {
			finish();
		}
		//go back, kills activity
		if(item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}
}

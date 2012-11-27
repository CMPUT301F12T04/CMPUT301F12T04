package com.example.cmput301.view;

import com.example.cmput301.R;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

@TargetApi(15)
public class PictureSelectionView extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {	

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_select_view);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pic_select_view, menu);
		return true;
	}
	
    /**
     * Overrided method that just enables the back button to kill the activity
     */
	public boolean onOptionsItemSelected(MenuItem item) {

		//go back, kills activity
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		if (item.getItemId() == R.id.menu_camera_select) {
			finish();
		}
		if (item.getItemId() == R.id.menu_pic_respond) {
			finish();
		}
		
		return true;
		
}
}

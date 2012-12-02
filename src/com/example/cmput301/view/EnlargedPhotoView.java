package com.example.cmput301.view;

import com.example.cmput301.R;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

@TargetApi(15)
public class EnlargedPhotoView  extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enlarge_photo_view);
		
		// enable back button on action bar
	    ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
			
		//display photo on screen
		Bitmap photo = getIntent().getParcelableExtra("photo");
		ImageView photoView =  (ImageView) findViewById(R.id.en_large_photo);
		photoView.setImageBitmap(photo);
				
	}
	public boolean onOptionsItemSelected(MenuItem item) {

		//go back, kills activity
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}

}

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

import com.example.cmput301.R;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * Simple activity class that takes in a Bitmap photo from 
 * its parent class (PhotoResponseView) and displays it on the
 * screen in larger form.
 */
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

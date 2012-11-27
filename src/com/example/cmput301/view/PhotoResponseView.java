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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.cmput301.R;


@TargetApi(15)
public class PhotoResponseView extends ResponseView {


	@Override
	public void onCreate(Bundle savedInstanceState) {	

		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_response_view);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.photo_response_view, menu);
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
		if (item.getItemId() == R.id.menu_camera) {
			 Intent in = new Intent(PhotoResponseView.this, PictureSelectionView.class);
	          startActivity(in);
		}
			return true;
		}
}

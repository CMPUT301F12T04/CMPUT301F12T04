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
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cmput301.R;
import com.example.cmput301.model.Task;
import com.example.cmput301.model.response.PictureResponse;
import com.example.cmput301.model.response.factory.PictureResponseFactory;


@TargetApi(15)
public class PhotoResponseView extends ResponseView {


	Task t1;
	@Override
	public void onCreate(Bundle savedInstanceState) {	

		respFactory = new PictureResponseFactory();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_response_view);

		//set action bar to have back option
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//fake task and responses
		t1 = new Task("df","dsf",PictureResponse.class.toString());
		Bitmap p = null;
		PictureResponse pR = (PictureResponse) respFactory.createResponse("fdsa", p);
		t1.addResponse(pR);
		
		//set up the listview to use custom adapter
		ListView photoList = (ListView) findViewById(R.id.photo_response_list);
		pResponseListAdapter pRLA = new pResponseListAdapter(this);
		photoList.setAdapter(pRLA);
		
		//set title to be task title
		String taskTile;
		taskTile = t1.getName();
		setTitle(taskTile);

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
		//take a picture, go to selection view
		if (item.getItemId() == R.id.menu_camera) {
			 Intent in = new Intent(PhotoResponseView.this, PictureSelectionView.class);
	          startActivity(in);
		}
			return true;
		}
	
	class pResponseListAdapter extends BaseAdapter {
		private Context context;
		pResponseListAdapter(Context context) {
			this.context = context;
		}

		/**
		 * Overrided method that allows a photo response to display it's image, annotation and
		 * timestamp.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(R.layout.photo_response_entry, null);
			}

			//set the annotation of the photo response
			TextView annoView;
			annoView = (TextView) row.findViewById(R.id.p_entry_responseAno);
			annoView.setText(t1.getResponses().get(position).getAnnotation());

			//set the time stamp of the photo response
			TextView timeView;
			timeView = (TextView) row.findViewById(R.id.p_entry_responseTime);
			timeView.setText(t1.getResponses().get(position).getTimestamp().toString());

			//Sets the image for the response, all tasks are currently set for TEXT only
			ImageView taskTypeImg = (ImageView) row.findViewById(R.id.entry_responsePhoto);
			taskTypeImg.setImageResource(android.R.drawable.ic_menu_gallery);

			return row;
		}

		public int getCount() {
			return t1.getResponses().size();
		}

		public Object getItem(int position) {
			return t1.getResponses().get(position);
		}

		public long getItemId(int position) {
			return position;
		}
	}

}

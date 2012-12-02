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
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.cmput301.R;
import com.example.cmput301.controller.PhotoResponseController;
import com.example.cmput301.model.Task;
import com.example.cmput301.model.response.PictureResponse;
import com.example.cmput301.model.response.factory.PictureResponseFactory;

@TargetApi(15)
public class PhotoResponseView extends ResponseView {
	private static final int PHOTO_RESPONSES_CAMERA_CODE = 1;
	private PhotoResponseController prController;

	Task t1;
	pResponseListAdapter pRLA;
	@Override
	public void onCreate(Bundle savedInstanceState) {	

		respFactory = new PictureResponseFactory();
		super.onCreate(savedInstanceState);
		prController = new PhotoResponseController(this.getApplicationContext(), this);
		setContentView(R.layout.photo_response_view);

		//set action bar to have back option
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//fake task and responses
		Bundle bundle = getIntent().getExtras();
		t1 = (Task) bundle.getSerializable("task");	
		String taskTile = t1.getName();
		String taskDesc = t1.getDescription();

		//set title to be task title
		taskTile = t1.getName();
		setTitle(taskTile);

		// setting the description of the task
		TextView title = (TextView) findViewById(R.id.p_task_des_view);
		title.setText("Votes: " + t1.getVotes() + "\n\n" + taskDesc);
		
		
		
		//set up the listview to use custom adapter
		ListView photoList = (ListView) findViewById(R.id.photo_response_list);
		pRLA = new pResponseListAdapter(this);
		photoList.setAdapter(pRLA);
		photoList.setOnItemClickListener(new OnItemClickListener() {
	
			public void onItemClick(AdapterView<?> adp, View view,
					int pos, long id) {
				Intent in = new Intent(PhotoResponseView.this, EnlargedPhotoView.class);
				//passing task to enlarge photo view
				Bitmap b = (Bitmap) t1.getResponses().get(pos).getContent();
				in.putExtra("photo", b );
				startActivity(in); 
				
			}});
		
		
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
		if (item.getItemId() == R.id.menu_p_task_upload)
		{
			prController.shareTask(t1.getId());
			finish();
		}
		if (item.getItemId() == R.id.menu_vote) {
			prController.voteTask(t1);
			TextView title = (TextView) findViewById(R.id.p_task_des_view);
			title.setText("Votes: " + t1.getVotes() + "\n\n"
					+ t1.getDescription());
		}
		//take a picture, go to selection view
		if (item.getItemId() == R.id.menu_camera) {
			 Intent in = new Intent(PhotoResponseView.this, PictureSelectionView.class);
			 startActivityForResult(in,PHOTO_RESPONSES_CAMERA_CODE);
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
			taskTypeImg.setImageBitmap((Bitmap)t1.getResponses().get(position).getContent());

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
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if(requestCode == PHOTO_RESPONSES_CAMERA_CODE && resultCode == RESULT_OK ) {
			ArrayList<String> annoList = new ArrayList<String>();
			ArrayList<Bitmap> bitList = new ArrayList<Bitmap>();
					
			annoList = (ArrayList<String>) data.getSerializableExtra("annos");
			bitList = (ArrayList<Bitmap>) data.getSerializableExtra("photos");
			
			for(int i = 0; i<annoList.size();i++)
			{
				PictureResponse pR = (PictureResponse) respFactory.createResponse(
						annoList.get(i), bitList.get(i));
				
				prController.addResponse(t1, pR);
			}
			
			pRLA.notifyDataSetChanged();
			
		}
	}
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem shareButton = menu.findItem(R.id.menu_p_task_upload);
	    if(t1.getStatus() == Task.STATUS_SHARED)
	    {
	    	shareButton.setEnabled(false);
	    	shareButton.setVisible(false);	
	    }
			
		return true;
	}
			

}

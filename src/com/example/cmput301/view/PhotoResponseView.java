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

/**
 * 
 * View class used to display the photo type tasks and to show their
 * photo responses. Photo responses are shown as a listview, and their
 * individual entry consists of a photo, an annotation and a timestamp.
 * Several options are available such as upvoting, deleting, sharing tasks.
 *
 */
@TargetApi(15)
public class PhotoResponseView extends ResponseView {
	private static final int PHOTO_RESPONSES_CAMERA_CODE = 1;
	private PhotoResponseController prController;

	Task aTask;
	public pResponseListAdapter pRLA;
	@Override
	public void onCreate(Bundle savedInstanceState) {	

		respFactory = new PictureResponseFactory();
		super.onCreate(savedInstanceState);
		prController = new PhotoResponseController(this.getApplicationContext(), this);
		setContentView(R.layout.photo_response_view);

		//set action bar to have back option
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//Get task name and description
		Bundle bundle = getIntent().getExtras();
		aTask = (Task) bundle.getSerializable("task");	
		String taskTile = aTask.getName();
		String taskDesc = aTask.getDescription();

		//set title to be task title
		taskTile = aTask.getName();
		setTitle(taskTile);

		// setting the description of the task
		TextView title = (TextView) findViewById(R.id.p_task_des_view);
		title.setText("Votes: " + aTask.getVotes() + "\n\n" + taskDesc);
		
		
		
		//set up the listview to use custom adapter
		ListView photoList = (ListView) findViewById(R.id.photo_response_list);
		pRLA = new pResponseListAdapter(this);
		photoList.setAdapter(pRLA);
		photoList.setOnItemClickListener(new OnItemClickListener() {
	
			public void onItemClick(AdapterView<?> adp, View view,
					int pos, long id) {
				Intent in = new Intent(PhotoResponseView.this, EnlargedPhotoView.class);
				//passing task to enlarge photo view
				Bitmap b = (Bitmap) aTask.getResponses().get(pos).getContent();
				in.putExtra("photo", b );
				startActivity(in); 
				
			}});
		
		
	}
	
	/**
	 * Overrided method used to refresh the list view when resumed
	 */
	@Override
	public void onResume() {
		super.onResume();

		aTask = prController.getTask(aTask.getId());

		ListView photoList = (ListView) findViewById(R.id.photo_response_list);
		pRLA = new pResponseListAdapter(this);
		photoList.setAdapter(pRLA);

	}

	/**
	 * Set to use custom menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.photo_response_view, menu);
		return true;
	}

	/**
	 * Overrided method that listens on selected options on actionbar
	 */
	public boolean onOptionsItemSelected(MenuItem item) {

		//go back, kills activity
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		//delete the task
		if (item.getItemId() == R.id.menu_delete) {
			prController.deleteTask(aTask.getId());
			finish();
		}
		//upload the task
		if (item.getItemId() == R.id.menu_p_task_upload)
		{
			prController.shareTask(aTask.getId());
			finish();
		}
		//upvote the task
		if (item.getItemId() == R.id.menu_vote) {
			prController.voteTask(aTask);
			TextView title = (TextView) findViewById(R.id.p_task_des_view);
			title.setText("Votes: " + aTask.getVotes() + "\n\n"
					+ aTask.getDescription());
		}
		//take a picture, go to selection view
		if (item.getItemId() == R.id.menu_camera) {
			Intent in = new Intent(PhotoResponseView.this, PictureSelectionView.class);
			startActivityForResult(in,PHOTO_RESPONSES_CAMERA_CODE);
		}
		return true;
	}
	
	/**
	 * Custom adapter used to display photo responses as
	 * a listview entry
	 */
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
			View row = getRow(convertView);

			//set the annotation of the photo response
			getAnnoView(row,position);

			//set the time stamp of the photo response
			getTimeView(row,position);

			//Sets the image for the photo response
			setTaskTypeImg(row,position);
		
			return row;
		}

		/**
		 * Sets the image of the task entry to match its type
		 * @param row of the listview
		 * @param position the position in the list
		 */
		private void setTaskTypeImg(View row, int position)
		{

			ImageView taskTypeImg = (ImageView) row.findViewById(R.id.entry_responsePhoto);
			taskTypeImg.setImageBitmap((Bitmap)aTask.getResponses().get(position).getContent());
		}

		private View getRow(View row)
		{

			if (row == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(R.layout.photo_response_entry, null);
			}
			return row;
		}

		private TextView getTimeView(View row, int position)
		{
			
			TextView timeView = (TextView) row.findViewById(R.id.p_entry_responseTime);
			timeView.setText(aTask.getResponses().get(position).getTimestamp().toString());
			return timeView;
		}

		private void getAnnoView(View row, int position)
		{
			
			TextView annoView = (TextView) row.findViewById(R.id.p_entry_responseAno);
			annoView.setText(aTask.getResponses().get(position).getAnnotation());
		}

		public int getCount() {
			return aTask.getResponses().size();
		}

		public Object getItem(int position) {
			return aTask.getResponses().get(position);
		}
		
		public long getItemId(int position) {
			return position;
		}
	}
	/**
	 * Gets the returned image and annotation from the responses made by camera and adds it
	 * to the current task
	 */
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if(requestCode == PHOTO_RESPONSES_CAMERA_CODE && resultCode == RESULT_OK ) {
			//all the images and annotations
			ArrayList<String> annoList = new ArrayList<String>();
			ArrayList<Bitmap> bitList = new ArrayList<Bitmap>();
				
			//getting them
			annoList = (ArrayList<String>) data.getSerializableExtra("annos");
			bitList = (ArrayList<Bitmap>) data.getSerializableExtra("photos");
			
			//add them as responses to the task
			for(int i = 0; i<annoList.size();i++)
			{
				PictureResponse pR = (PictureResponse) respFactory.createResponse(
						annoList.get(i), bitList.get(i));
				prController.addResponse(aTask, pR);
			}
			pRLA.notifyDataSetChanged();
		}
	}
	
	/**
	 * Overrided method used to check and see if task has been shared,
	 * if so then disable the share and delete button.
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem shareButton = menu.findItem(R.id.menu_p_task_upload);
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

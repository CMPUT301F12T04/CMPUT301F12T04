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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.example.cmput301.R;
import com.example.cmput301.model.response.PictureResponse;
import com.example.cmput301.model.response.Response;
import com.example.cmput301.model.response.factory.PictureResponseFactory;
import com.example.cmput301.model.response.factory.ResponseFactory;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

@TargetApi(15)
public class PictureSelectionView extends Activity {


	MyArrayAdapter myArrayAdapter;
	private ArrayList<PictureResponse> pResponses = new ArrayList<PictureResponse>();
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private Uri fileUri;


	@Override
	public void onCreate(Bundle savedInstanceState) {	

		ResponseFactory respFactory = new PictureResponseFactory();

		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_select_view);

		//fake task, with fake picture response
		PictureResponse pR = (PictureResponse) respFactory.createResponse("hello", null);
		PictureResponse pR1 = (PictureResponse) respFactory.createResponse("hello1", null);
		PictureResponse pR2 = (PictureResponse) respFactory.createResponse("hello2", null);
		pResponses.add(pR);
		pResponses.add(pR1);
		pResponses.add(pR2);

		//set back button on actionbar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		ListView photoList = (ListView) findViewById(R.id.photo_selection_list);

		
		//set up array adapter
		myArrayAdapter = new MyArrayAdapter(this,
				R.layout.pic_response_entry,
				R.id.pic_annotation_entry,
				pResponses
				);

		photoList.setAdapter(myArrayAdapter);
		photoList.setOnItemClickListener(new OnItemClickListener() {
			
			//when item is clicked, it is checked
		  @Override
		  public void onItemClick(AdapterView<?> parent, View view, int position,
		    long id) {
		   myArrayAdapter.toggleChecked(position);
		  }});
		
				
		

	}
	 private class MyArrayAdapter extends ArrayAdapter<PictureResponse>{
	     
	     private HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();

	  public MyArrayAdapter(Context context, int resource,
	    int textViewResourceId, ArrayList<PictureResponse> list) {
	   super(context, resource, textViewResourceId, list);
	   
	   for(int i = 0; i < list.size(); i++){
	    myChecked.put(i, false);
	   }
	  }
	     
	  public void toggleChecked(int position){
	   if(myChecked.get(position)){
	    myChecked.put(position, false);
	   }else{
	    myChecked.put(position, true);
	   }
	   
	   notifyDataSetChanged();
	  }
	  
	  public List<Integer> getCheckedItemPositions(){
	   List<Integer> checkedItemPositions = new ArrayList<Integer>();
	   
	   for(int i = 0; i < myChecked.size(); i++){
	    if (myChecked.get(i)){
	     (checkedItemPositions).add(i);
	    }
	   }
	   
	   return checkedItemPositions;
	  }
	  
	  public List<Response> getCheckedItems(){
	   List<Response> checkedItems = new ArrayList<Response>();
	   
	   for(int i = 0; i < myChecked.size(); i++){
	    if (myChecked.get(i)){
	     (checkedItems).add(pResponses.get(i));
	    }
	   }
	   
	   return checkedItems;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	   View row = convertView;
	   
	   if(row==null){
	    LayoutInflater inflater=getLayoutInflater();
	    row=inflater.inflate(R.layout.pic_response_entry, parent, false);  
	   }
	   
	   //set the annotation for the picture response
	   CheckedTextView checkedTextView = (CheckedTextView)row.findViewById(R.id.pic_annotation_entry);
	   checkedTextView.setText(pResponses.get(position).getAnnotation());
	   
	   //set the image for the picture response 
	 //  ImageView respImage = (ImageView)row.findViewById(R.id.pic_response_entry);
	  // respImage.setImageResource(android.R.drawable.ic_menu_gallery);
	   
	   //check if the position is checked
	   Boolean checked = myChecked.get(position);
	   if (checked != null) {
		   checkedTextView.setChecked(checked);
	   }  
	   return row;
	  }
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
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		}
		if (item.getItemId() == R.id.menu_pic_respond) {
			String result = "";
		       
		    //getCheckedItems
		    List<Response> resultList = myArrayAdapter.getCheckedItems();
		    for(int i = 0; i < resultList.size(); i++){
		     result += String.valueOf(resultList.get(i).getAnnotation()) + "\n";
		    }
		    
		    //show selected items for now
		    Toast.makeText(
		      getApplicationContext(), 
		      result, 
		      Toast.LENGTH_LONG).show();
		}
		
		return true;
		
}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            
            //setting photo taken from camera
            ImageView respImage = (ImageView)findViewById(R.id.pic_response_entry);
            respImage.setImageBitmap(photo);
        }  
    } 
	
	
	
}

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
import java.util.HashMap;

import com.example.cmput301.R;
import com.example.cmput301.model.response.PictureResponse;
import com.example.cmput301.model.response.factory.PictureResponseFactory;
import com.example.cmput301.model.response.factory.ResponseFactory;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

/**
 * View class used to hold temporary images taken by the camera. This is a 
 * picture selection view, in which the user keeps on taking pictures and
 * eventually when they are done, will select the images they desire by using
 * checkboxes that are supplied within the list view and respond to the task
 * by pressing the respond button.
 */
@TargetApi(15)
public class PictureSelectionView extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int ANNOTATION_REQUEST_CODE = 2;
	private String annotation = "";
	private Bitmap photo;
	private ArrayList<PictureResponse> pResponses = new ArrayList<PictureResponse>();
	PhotoRespAdapter pRespAdapter;
	ResponseFactory respFactory = new PictureResponseFactory();

	/**
	 * Overrided method to set custom view of our picture response list,
	 * it contains a list of pictures with an annotation and a checkbox
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_select_view);

		//set back button on actionbar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		ListView photoList = (ListView) findViewById(R.id.photo_selection_list);
		
		//set up array adapter for photo responses
		pRespAdapter = new PhotoRespAdapter(this,
				R.layout.pic_response_entry,
				R.id.pic_annotation_entry,
				pResponses
				);
		photoList.setAdapter(pRespAdapter);
		photoList.setOnItemClickListener(new OnItemClickListener() {
			
			//when a photo response is clicked it is, the check box is checked
		  @Override
		  public void onItemClick(AdapterView<?> parent, View view, int position,
		    long id) {
		   pRespAdapter.toggleChecked(position);
		  }});
		
	}
	/**
	 * This class is a custom adapter created to handle displaying PhotoResponse
	 * objects. It takes the PhotoResponse object and displays the Bitmap in the form
	 * of an ImageView and it's annotation as a String. This is done for each photo
	 * response in the task that is given to the adapter
	 */
	 @SuppressLint("UseSparseArrays")
	private class PhotoRespAdapter extends ArrayAdapter<PictureResponse>{
	     
	     private HashMap<Integer, Boolean> itemsChecked = new HashMap<Integer, Boolean>();

	  public PhotoRespAdapter(Context context, int resource,
	    int textViewResourceId, ArrayList<PictureResponse> list){
		  super(context, resource, textViewResourceId, list);

	  }

	  /**
	   * Updates the checked array to match number of potential responses
	   */
	  public void resetcheckedSize()
	  {
		  for(int i = 0; i < pResponses.size(); i++){
			  itemsChecked.put(i, false);
		  }
		  return;
	  }

	  /**
	   * Marks the position as being checked or unchecked
	   * @param position the position of the item selected in the list
	   */
	  public void toggleChecked(int position){
		  if(itemsChecked.get(position)){
			  itemsChecked.put(position, false);
		  }else{
			  itemsChecked.put(position, true);
		  }
		  notifyDataSetChanged();
	  }
	  
	  /**
	   * Get the checked items that the user has selected.
	   * @return all the checked items in the form of an arraylist
	   */
	  public ArrayList<PictureResponse> getCheckedItems(){
		  ArrayList<PictureResponse> checkedItems = new ArrayList<PictureResponse>();

		  for(int i = 0; i < itemsChecked.size(); i++){
			  if (itemsChecked.get(i)){
				  (checkedItems).add(pResponses.get(i));
			  }
		  }
		  return checkedItems;
	  }

	  /**
	   * Overiided method to display custom view
	   */
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	   View row = convertView;
	   
	   if(row==null){
	    LayoutInflater inflater=getLayoutInflater();
	    row=inflater.inflate(R.layout.pic_response_entry, parent, false);  
	   }
	   
	   //set the annotation for temp picture response
	   CheckedTextView checkedTextView = (CheckedTextView)row.findViewById(R.id.pic_annotation_entry);
	   checkedTextView.setText(pResponses.get(position).getAnnotation());
	   
	   //set the image for temp picture response 
	   ImageView respImage = (ImageView)row.findViewById(R.id.pic_response_entry);
	   respImage.setImageBitmap((Bitmap) pResponses.get(position).getContent());
	   
	   //check if the position is checked
	   Boolean checked = itemsChecked.get(position);
	   if (checked != null) {
		   checkedTextView.setChecked(checked);
	   }  
	   return row;
	  }
	 }
	  
	 /**
	  * Populates the action bar with items set in menu xml
	  */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pic_select_view, menu);
		return true;
	}
	
    /**
     * Overrided method that listens on the clicks made on action bar icons
     */
	public boolean onOptionsItemSelected(MenuItem item) {

		//go back, kills activity
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		//camera is selected, go to camera, get a picture and annotation back
		//or nothing if user doesn't take a photo
		if (item.getItemId() == R.id.menu_camera_select) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		}
		//make a response with the selected items
		if (item.getItemId() == R.id.menu_pic_respond) {
			pRespAdapter.notifyDataSetChanged();
			
		    ArrayList<PictureResponse> resultList = pRespAdapter.getCheckedItems();
		    ArrayList<String> strList = new ArrayList<String>();
		    ArrayList<Bitmap> bitList = new ArrayList<Bitmap>();
		    
		    //Go through list and extract annotations and bitmaps
		   for(int i = 0; i < resultList.size(); i++){
			   strList.add(resultList.get(i).getAnnotation());
			   bitList.add((Bitmap)resultList.get(i).getContent());
		    }

		   //pass the extracted values back to parent activity
		    Intent i = new Intent();
    		i.putExtra("annos",strList);
    		i.putExtra("photos", bitList);
			setResult(Activity.RESULT_OK, i);
			finish();
		    
		}
		return true;
}

	/**
	 * Overrided method that checks if the camera returns a successful image, it also checks
	 * for the return of a user inputted annotation
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Annotation has been returned
		if(requestCode == ANNOTATION_REQUEST_CODE && resultCode == RESULT_OK ) {
			
			//create a new temporary photo response
			annotation =  data.getStringExtra("annotation");
			PictureResponse pR = (PictureResponse) respFactory.createResponse(annotation, photo);
			pResponses.add(pR);
			pRespAdapter.notifyDataSetChanged();
			pRespAdapter.resetcheckedSize(); //update items checked array size
		}
		//Image has been returned
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {  
            photo = (Bitmap) data.getExtras().get("data");
            
            //Get the annotation from user
            Intent in = new Intent(PictureSelectionView.this,
					AnnotationInputView.class);
			startActivityForResult(in, ANNOTATION_REQUEST_CODE);
        }
      
    } 
	
	
	
}

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

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.example.cmput301.controller.*;
import com.example.cmput301.model.response.AudioResponse;
import com.example.cmput301.model.response.PictureResponse;
import com.example.cmput301.model.response.TextResponse;
import com.example.cmput301.R;
import com.example.cmput301.application.*;




/**
 * The main screen of our application, which consists mainly of a list of tasks.
 * It consists of several available options available to the user. Which include
 * a drop down navigation, a search option, and an add new task option. The body
 * of this view is made up of the list of tasks. Each task can be clicked on for
 * further information and additional task related commands.
 *
 * @author dyu2
 *
 */
@TargetApi(15)
public class MainActivity extends Activity  implements  SearchView.OnQueryTextListener,
SearchView.OnCloseListener {

	private SearchView mSearchView;
    private MainController mainController;
    //setting up list view and using customAdapter for tasks
    ListView taskview;
    /**
     * Method is responsible for the creation of the view of the activity,
     * Things such as the actionbar and the list of tasks are set here.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  
        
        //Setup the main controller
        mainController = new MainController(this.getApplicationContext(), this);

        MainController.callBack = (MyCallback) new Callback();

        mainController.updateRemoteTasks();
        
        //Setting up the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
        OnNavigationListener mOnNavigationListener;

        //Using custom dropdown list with white color font
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.taskview_options,
                R.layout.spinner_dropdown_item);

        //When item is clicked, a toast is displayed for now
        mOnNavigationListener = new OnNavigationListener() {
            String[] choices = getResources().getStringArray(R.array.taskview_options);

            public boolean onNavigationItemSelected(int position, long itemId) {

                if (choices[position].equals("My Tasks")) {
                    mainController.checkoutPrivate();
                } else if (choices[position].equals("My Shared")) {
                    mainController.checkoutShared();
                } else if (choices[position].equals("Unanswered")) {
                    mainController.checkoutUnanswered();
                } else if (choices[position].equals("Other User's Tasks")) {
                    mainController.checkoutRemote();
                } else if (choices[position].equals("Random Tasks")) {
                    mainController.checkoutRandom();
                } else if (choices[position].equals("Popular")) {
                	mainController.checkoutPopular();
                }
                return true;
            }
        };
        
        
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);

  
        taskview = (ListView) findViewById(R.id.mainActivityList);
        taskview.setAdapter(mainController.getListAdapter());
        taskview.setOnItemClickListener(new OnItemClickListener() {
            //When item is clicked individual task view is opened,
            //the task is passed to individual task view
            public void onItemClick(AdapterView<?> adp, View view,
                    int pos, long id) {
            	

            	if(mainController.getList().get(pos).getType().equals(
            			PictureResponse.class.toString()))
            	{
            		Intent in = new Intent(MainActivity.this, PhotoResponseView.class);
            		//passing task to individual task view
            		Bundle bundle = new Bundle();
            		bundle.putInt("id", pos);
            		bundle.putSerializable("task", mainController.getList().get(pos));
            		in.putExtras(bundle);
            		startActivity(in); 
            	}
            	else
            	{

            		Intent in = new Intent(MainActivity.this, IndividualTaskView.class);
            		//passing task to individual task view
            		Bundle bundle = new Bundle();
            		bundle.putInt("id", pos);
            		bundle.putSerializable("task", mainController.getList().get(pos));
            		in.putExtras(bundle);
            		startActivity(in);
            	}

            }
        });
        
        
    }

	/**
     * Using own custom menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        return true;
    }

   
    

	@Override
    public void onResume() {
        super.onResume();
        int navIndex = getActionBar().getSelectedNavigationIndex();
        String[] choices = getResources().getStringArray(R.array.taskview_options);
        
        if (choices[navIndex].equals("My Tasks")) {
            mainController.checkoutPrivate();
        } else if (choices[navIndex].equals("My Shared")) {
            mainController.checkoutShared();
        } else if (choices[navIndex].equals("Unanswered")) {
            mainController.checkoutUnanswered();
        } else if (choices[navIndex].equals("Other User's Tasks")) {
            mainController.checkoutRemote();
        } else if (choices[navIndex].equals("Random Tasks")) {
            mainController.checkoutRandom();
        } else if (choices[navIndex].equals("Popular")) {
        	mainController.checkoutPopular();
        }       
    }

    /**
     * Overrided method, that checks if the add option was click. If so a dialog
     * box will appear and the task can be defined. The database will be updated
     * accordingly
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Adding a task has been selected
        if (item.getItemId() == R.id.menu_add) {

            //Defining dialog style and setting it up
            final Dialog dialog = new Dialog(this, R.style.dialogStyle);
            dialog.setContentView(R.layout.add_task_view);

            //Defining accept button
            Button acceptButton = (Button) dialog.findViewById(R.id.dialogButtonAccept);
            acceptButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    String titleInput;
                    String descInput;

                    //Get the title input
                    EditText titleText = (EditText) dialog.findViewById(R.id.title_Input);
                    titleInput = titleText.getText().toString();

                    //Get the description input
                    EditText descText = (EditText) dialog.findViewById(R.id.des_Input);
                    descInput = descText.getText().toString();

                    //Check which radio button was selected
                    RadioButton rPhoto = (RadioButton) dialog.findViewById(R.id.radioPhoto);
                    RadioButton rText = (RadioButton) dialog.findViewById(R.id.radioText);
                    RadioButton rAudio = (RadioButton) dialog.findViewById(R.id.radioAudio);

                    String type;
                    if (rPhoto.isChecked()) {
                        //set type of Task to be photo
                    	type = PictureResponse.class.toString(); 
                    } else if (rText.isChecked()) {
                        //set type of Task to be text
                    	type = TextResponse.class.toString();
                    } else if (rAudio.isChecked()) {
                        //set type of Task to be audio
                    	type = AudioResponse.class.toString();
                    }
                    else
                    {
                    	throw new IllegalStateException();
                    }

                    if(titleInput.equals(""))
                    {
                    	Toast.makeText(getApplicationContext(), 
                    			"New task requires a title!", Toast.LENGTH_SHORT).show();                	
                    }
                    else
                    {
                    	//Create new task here and add to database etc.
                    	mainController.addTask(titleInput, descInput, type);
                    	dialog.dismiss();
                    	getActionBar().setSelectedNavigationItem(0);
                    }
                    
                }
            });

            //Defining cancel button
            Button cancelButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
            cancelButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.setTitle("Adding a Task");
        }
        if(item.getItemId() == R.id.menu_refresh)
        {
        	mainController.updateRemoteTasks();
        }
        return true;
    }

    class Callback implements MyCallback {
    	   public void callbackCall() {
    	      Log.d("TEST","WORKED");
    	      onResume();
    	   }
    	}

	
    @Override
	public boolean onClose() {
    	mainController.restoreUnfiltered();
    	return false;
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		mainController.filter(arg0);
		
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		
		return onClose();
	}
}


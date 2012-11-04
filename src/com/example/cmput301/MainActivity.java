package com.example.cmput301;

import java.util.ArrayList;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


/**
 * The main screen of our application, which consists mainly of a list of tasks.
 * It consists of several available options available to the user. Which include a
 * drop down navigation, a search option, and an add new task option. The body
 * of this view is made up of the list of tasks. Each task can be clicked on for
 * further information and additional task related commands.
 * 
 * @author dyu2
 *
 */
@TargetApi(15)
public class MainActivity extends Activity {
	
	private MainController mainController;
	ArrayList<Task> tasks = new ArrayList<Task>();

    /**
     * Method is responsible for the creation of the view of the activity,
     * Things such as the actionbar and the list of tasks are set here.
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_main);
        
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
        		  Toast.makeText(getApplicationContext(), 
  						"You have selected " + choices[position],
  						Toast.LENGTH_SHORT).show();
        	   return true;
        	  }
         };
         actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);

         //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         //fake tasks for now, please delete when new tasks can be created and stored
         Task task1 = new Task("Hello","A random sentence to use for testing");
         tasks.add(task1);
         Task task2 = new Task("What is this?","A random sentence to use for testing more thigns");
         tasks.add(task2);
         Task task3 = new Task("Just another one","A random sentence to use for testing even more and more things");
         tasks.add(task3);
         //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        	
           	//setting up list view and using customAdapter for tasks
        	  ListView taskview;
        	  taskview = (ListView) findViewById(R.id.mainActivityList);
        	  CustomAdapter ca = new CustomAdapter();
        	  taskview.setAdapter(ca);
        	  taskview.setOnItemClickListener(new OnItemClickListener() {

        		  //When item is clicked individual task view is opened,
        		  //the task is passed to individual task view
        		  public void onItemClick(AdapterView<?> adp, View view,
        				  int pos, long id) {
        			  Intent in = new Intent(MainActivity.this,IndividualTaskView.class);
        			  
        			  //passing task to individual task view
        			  Bundle bundle = new Bundle();
        			  bundle.putInt("id", pos);
        			  bundle.putSerializable("task", tasks.get(pos));
        			  in.putExtras(bundle);
        			  startActivity(in);
        		  }
        	  });
    }

	/**
	 * Using own custom menu
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**
     * Overrided method, that checks if the add option was click. If so
     * a dialog box will appear and the task can be defined. The database
     * will be updated accordingly
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	//Adding a task has been selected
    	if(item.getItemId() == R.id.menu_add) {   		
    		
    		//Defining dialog style and setting it up
    		final Dialog dialog = new Dialog(this,R.style.dialogStyle);
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
    	    		
    	    		if(rPhoto.isChecked())
    	    		{
    	    			//set type of Task to be photo
    	    		}
    	    		else if (rText.isChecked())
    	    		{
    	    			//set type of Task to be text
    	    		}
    	    		else if(rAudio.isChecked())
    	    		{
    	    			//set type of Task to be audio
    	    		}
    	    		
    	    		//Create new task here and add to database etc.
    				mainController.addTask(titleInput, descInput);
    				dialog.dismiss();
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
    	return true;
    }
    
    
    /**
     * A custom adapter that is set up to take in a Task.
     * The adapter allows the title, description and type of task (in form of 
     * a picture) to be displayed by the list view
     * @author dyu2
     *
     */
    class CustomAdapter extends ArrayAdapter<Task>
    {
    	
    	CustomAdapter() {
    		//not the best way to set up the constructor, should fix this later on
    		super(MainActivity.this,android.R.layout.simple_list_item_1, tasks);	
    	}
    	
    	/**
    	 * Overrided method that allows a task to display it's title and
    	 * description. An image can also be included to indicate the type 
    	 * of task.
    	 */
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent)
    	{
    		View row = convertView;
    		
    		if(row == null) {
    		 LayoutInflater inflater = getLayoutInflater();
    		 row = inflater.inflate(R.layout.task_entry, null);
    		}
    		
    		//set the title of the task
    		TextView titleView;
    		titleView = (TextView) row.findViewById(R.id.TaskTitleListEntry);
    		titleView.setText(tasks.get(position).getName()); 
    		
    		//set the description of the task
    		TextView descView;
    		descView = (TextView) row.findViewById(R.id.TaskDescListEntry);
    		descView.setText(tasks.get(position).getDescription());
    		
    		return row;
    	}
    }
   
    
    
}

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

@TargetApi(11)
public class MainActivity extends Activity {
	
	private MainController mainController;
	ArrayList<Task> tasks = new ArrayList<Task>();

    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        //Setting up the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
        
        
        //Defining OnNavigationListener
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


         //fake tasks for now, please delete when new tasks can be created and stored
         Task task1 = new Task("Hello","A random sentence to use for testing");
         tasks.add(task1);
         Task task2 = new Task("What is this?","A random sentence to use for testing more thigns");
         tasks.add(task2);
         Task task3 = new Task("Just another one","A random sentence to use for testing even more and more things");
         tasks.add(task3);

        	
           	//setting up list view and using customAdapter for tasks
        	  ListView taskview;
        	  taskview = (ListView) findViewById(R.id.mainActivityList);
        	  CustomAdapter ca = new CustomAdapter();
        	  taskview.setAdapter(ca);
        	  taskview.setOnItemClickListener(new OnItemClickListener() {

        		  //When item is clicked individual task view is opened,
        		  //the task is passed to individualtaskview
        		  public void onItemClick(AdapterView<?> adp, View view,
        				  int pos, long id) {
        			  Intent in = new Intent(MainActivity.this,IndividualTaskView.class);
        			  Bundle bundle = new Bundle();
        			  bundle.putInt("id", pos);
        			  bundle.putSerializable("task", tasks.get(pos));
        			  in.putExtras(bundle);
        			  startActivity(in);
       
        		  }

        	  });


        	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	
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
    	    			//type of Task is photo
    	    		}
    	    		else if (rText.isChecked())
    	    		{
    	    			//type of Task is text
    	    		}
    	    		else if(rAudio.isChecked())
    	    		{
    	    			//type of Task is audio
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
    class CustomAdapter extends ArrayAdapter<Task>
    {
    	
    	CustomAdapter() {
    		super(MainActivity.this,android.R.layout.simple_list_item_1, tasks);
    		
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent)
    	{
    		View row = convertView;
    		
    		if(row == null) {
    		 LayoutInflater inflater = getLayoutInflater();
    		 row = inflater.inflate(R.layout.task_entry, null);
    		}
    		
    		TextView titleView;
    		titleView = (TextView) row.findViewById(R.id.TaskTitleListEntry);
    		titleView.setText(tasks.get(position).getName()); 
    		
    		TextView descView;
    		descView = (TextView) row.findViewById(R.id.TaskDescListEntry);
    		descView.setText(tasks.get(position).getDescription());
    		
    		return row;
    	}
    }
   
    
    
}

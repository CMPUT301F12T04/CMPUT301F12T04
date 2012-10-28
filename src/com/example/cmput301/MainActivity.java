package com.example.cmput301;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

@TargetApi(11)
public class MainActivity extends Activity {

    
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

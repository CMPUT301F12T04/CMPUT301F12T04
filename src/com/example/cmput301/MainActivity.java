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
 * It consists of several available options available to the user. Which include
 * a drop down navigation, a search option, and an add new task option. The body
 * of this view is made up of the list of tasks. Each task can be clicked on for
 * further information and additional task related commands.
 *
 * @author dyu2
 *
 */
@TargetApi(15)
public class MainActivity extends Activity {

    private MainController mainController;

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
                }

                return true;
            }
        };
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);

        //setting up list view and using customAdapter for tasks
        ListView taskview;
        taskview = (ListView) findViewById(R.id.mainActivityList);

        taskview.setAdapter(mainController.getListAdapter());
        taskview.setOnItemClickListener(new OnItemClickListener() {
            //When item is clicked individual task view is opened,
            //the task is passed to individual task view
            public void onItemClick(AdapterView<?> adp, View view,
                    int pos, long id) {
                Intent in = new Intent(MainActivity.this, IndividualTaskView.class);

                //passing task to individual task view
                Bundle bundle = new Bundle();
                bundle.putInt("id", pos);
                bundle.putSerializable("task", mainController.getList().get(pos));
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

    @Override
    public void onResume() {
        super.onResume();
        mainController = new MainController(this.getApplicationContext(), this);
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

                    if (rPhoto.isChecked()) {
                        //set type of Task to be photo
                    } else if (rText.isChecked()) {
                        //set type of Task to be text
                    } else if (rAudio.isChecked()) {
                        //set type of Task to be audio
                    }

                    //Create new task here and add to database etc.
                    mainController.addTask(titleInput, descInput, "Dont Care right now");

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
}

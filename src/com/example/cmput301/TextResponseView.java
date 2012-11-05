package com.example.cmput301;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.Date;

@TargetApi(15)
public class TextResponseView extends ResponseView {

    private MainController mainController;
    Task t;
    String responseString;
    Activity that;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        that = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_response_view);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mainController = new MainController(this.getApplicationContext(), this);
        String taskTile;
        
        //Getting information from bundle passed from MainActivity
        Bundle bundle = getIntent().getExtras();
        t = (Task) bundle.getSerializable("task");
        taskTile = t.getName();

        //setting the title of the task to be the activity title
        setTitle(taskTile);



        //
        final EditText responseInput;
        responseInput = (EditText) findViewById(R.id.textResponseInput);


        Button postBut;
        postBut = (Button) findViewById(R.id.postResponseButton);


        postBut.setOnClickListener(new View.OnClickListener() {
            //Post has been clicked.
            public void onClick(View v) {

                //Can now add response to the task using this string
                responseString = responseInput.getText().toString();
                TextResponse resp = new TextResponse(responseString, new Date());
                mainController.addResponse(t, resp);

                //setting list of responses for task
                ListView responses = (ListView) findViewById(R.id.text_response_list);
                @SuppressWarnings({ "rawtypes", "unchecked" })
				ArrayAdapter<TextResponse> adapter = new ArrayAdapter<TextResponse>(that,
                        android.R.layout.simple_list_item_1, (ArrayList) t.getResponses());
                responses.setAdapter(adapter);
            }
        });

        //setting list of responses for task
        ListView responses = (ListView) findViewById(R.id.text_response_list);
        @SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<TextResponse> adapter = new ArrayAdapter<TextResponse>(this,
                android.R.layout.simple_list_item_1, (ArrayList) t.getResponses());
        responses.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //go back, kills activity
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}

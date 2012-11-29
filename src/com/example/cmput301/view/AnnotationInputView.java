package com.example.cmput301.view;

import com.example.cmput301.R;
import com.example.cmput301.model.response.TextResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AnnotationInputView extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_annotation_view);
		
		    
		    final EditText annoInput;
			annoInput = (EditText) findViewById(R.id.photo_annotation);

			Button postBut;
			postBut = (Button) findViewById(R.id.postAnnoButton);


			postBut.setOnClickListener(new View.OnClickListener() {
				//Post has been clicked.
				public void onClick(View v) {
					String annotation;
					
					annotation = annoInput.getText().toString();	
					if(annotation.equals(""))
					{
						Toast.makeText(getApplicationContext(), 
								"Text input is required!", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Intent i = new Intent();
						i.putExtra("annotation", annotation);
						setResult(Activity.RESULT_OK, i);
						finish();
					}
					
				}
			});
	}

	

}

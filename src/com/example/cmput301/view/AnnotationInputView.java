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

import com.example.cmput301.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * View Class used to get annotation (as a String) from a user after a photo has been taken.
 * The view requires the user to give an annotation, before returning to the previous 
 * screen.
 */
public class AnnotationInputView extends Activity {

	/**
	 * Overrided method used to display a prompt to the user
	 * to get an annotation for the photo that was just taken
	 */
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

				//annotation was empty
				if(annotation.equals(""))
				{
					Toast.makeText(getApplicationContext(), 
							"Text input is required!", Toast.LENGTH_SHORT).show();
				}
				//get annotation and pass back to parent activity (PhotoResponseView)
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

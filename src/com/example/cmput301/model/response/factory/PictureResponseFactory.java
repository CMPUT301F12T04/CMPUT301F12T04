package com.example.cmput301.model.response.factory;

import java.util.Date;

import android.graphics.Picture;

import com.example.cmput301.model.response.PictureResponse;

public class PictureResponseFactory implements ResponseFactory {
	@Override
	public PictureResponse createResponse(String annotation, Object content) {
		//Can you fix this Andrew? -- Fixed =/
		if(content instanceof Picture) {
			 return createResponse(annotation, (Picture) content); //Missed a return statement =/
		}
		return null;		
	}
	
	public PictureResponse createResponse(String annotation, Picture content) {
		return new PictureResponse(annotation, new Date(), content);
	}
	
}

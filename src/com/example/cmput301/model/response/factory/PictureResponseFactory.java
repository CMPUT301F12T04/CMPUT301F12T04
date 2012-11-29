package com.example.cmput301.model.response.factory;

import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Picture;

import com.example.cmput301.model.response.PictureResponse;

public class PictureResponseFactory implements ResponseFactory {
	@Override
	public PictureResponse createResponse(String annotation, Object content) {
		//Can you fix this Andrew?
		/*if(content instanceof Picture) {
			 createResponse(annotation, (Picture) content);
		}
		return null;*/
		
		
		return new PictureResponse(annotation, new Date(),(Bitmap) content);
	}
	
	public PictureResponse createResponse(String annotation, Bitmap content) {
		return new PictureResponse(annotation, new Date(), content);
	}
	
}

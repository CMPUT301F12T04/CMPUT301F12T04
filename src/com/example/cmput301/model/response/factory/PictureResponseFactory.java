package com.example.cmput301.model.response.factory;

import java.util.Date;

import android.graphics.Bitmap;

import com.example.cmput301.model.response.PictureResponse;

public class PictureResponseFactory implements ResponseFactory {
	@Override
	public PictureResponse createResponse(String annotation, Object content) {

			 return createResponse(annotation, (Bitmap) content);
		
	}
	
	public PictureResponse createResponse(String annotation, Bitmap content) {
		return new PictureResponse(annotation, new Date(), content);
	}
	
}

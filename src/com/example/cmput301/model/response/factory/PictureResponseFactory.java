package com.example.cmput301.model.response.factory;

import java.util.Date;

import android.graphics.Bitmap;

import com.example.cmput301.model.response.PictureResponse;

public class PictureResponseFactory implements ResponseFactory {
	@Override
	public PictureResponse createResponse(String annotation, Object content) {
		if(content instanceof String) {
			return createResponse(annotation, (String) content);
		} else if(content instanceof Bitmap) {
			return createResponse(annotation, (Bitmap) content);
		} else {
			return null;
		}
	}
	
	public PictureResponse createResponse(String annotation, Bitmap content) {
		return new PictureResponse(annotation, new Date(), content);
	}
	
	public PictureResponse createResponse(String annotation, String content) {
		return new PictureResponse(annotation, new Date(), content);
	}
	
}

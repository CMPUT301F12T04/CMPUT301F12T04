package com.example.cmput301.model.response.factory;

import java.util.Date;

import com.example.cmput301.model.response.TextResponse;

public class TextResponseFactory implements ResponseFactory {

	@Override
	public TextResponse createResponse(String annotation, Object content) {
		if(content instanceof String) {
			return createResponse(annotation, (String) content);
		}
		return null;
	}
	
	public TextResponse createResponse(String annotation, String content) {
		return new TextResponse(annotation, new Date(), content);
	}
	
}

package com.example.cmput301.model.response.factory;

import com.example.cmput301.model.response.Response;

public interface ResponseFactory {
	
	public Response createResponse(String annotation, Object content);
	
}

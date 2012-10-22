package com.example.cmput301;

import java.util.Date;

import android.graphics.Picture;

public class PictureResponse extends Response
{
	private Picture picture;
	
	public PictureResponse(Picture picture, Date timestamp)
	{
		super.setTimestamp(timestamp);
		this.picture = picture;
	}
	
	public Picture getPicture()
	{
		return this.picture;
	}
}

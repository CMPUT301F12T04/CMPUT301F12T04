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
package com.example.cmput301.model.response;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class PictureResponse extends Response {
	private static final long serialVersionUID = 1L;

    public PictureResponse(String annotation, Date timestamp, Bitmap picture) {
        super(annotation, timestamp, serializeContent(picture));
    }
    
    public PictureResponse(String annotation, Date timestamp, String content) {
        super(annotation, timestamp, content);
    }
    
    public Bitmap getContent()
    {
    	byte[] bytes = stringToBytes((String) this.content);
    	return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public PictureResponse clone() {
        return null;
    }

	private static String serializeContent(Bitmap picture) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		picture.compress(Bitmap.CompressFormat.PNG, 100, baos);
	
		return  byteToString(baos.toByteArray());
	}
	
	private static String byteToString(byte[] bytes) {
		String byteString = "";
		for(int i = 0; i < bytes.length; i++) {
			byteString += Byte.toString(bytes[i]);
			
			if(i != bytes.length - 1) {
				byteString +=",";
			}
		}
		
		return byteString;
	}
	
	private static byte[] stringToBytes(String byteString) {
		String[] byteStringArray = byteString.split(",");
		byte byteArray[] = new byte[byteStringArray.length];
		
		for(int i = 0; i < byteArray.length; i++) {
			byteArray[i] = Byte.valueOf(byteStringArray[i]);
		}
		
		return byteArray;
	}

	@Override
	public String getSaveable() {
		return (String) this.content;
	}
}

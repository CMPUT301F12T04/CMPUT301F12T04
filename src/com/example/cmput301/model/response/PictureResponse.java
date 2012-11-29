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

import java.util.Date;

import android.graphics.Bitmap;


public class PictureResponse extends Response {
	private static final long serialVersionUID = 1L;

    public PictureResponse(String annotation, Date timestamp, Bitmap picture) {
        super(annotation, timestamp, picture);
    }
    
    public Bitmap getPicture()
    {
    	return (Bitmap) getContent();
    }

    public PictureResponse clone() {
        return null;

    }
}

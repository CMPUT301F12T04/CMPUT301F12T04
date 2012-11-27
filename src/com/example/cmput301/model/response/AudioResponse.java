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

import android.provider.MediaStore.Audio;

public class AudioResponse extends Response {
	private static final long serialVersionUID = 1L;

    public AudioResponse(String annotation, Date timestamp, Audio audio) {
        super(annotation, timestamp, audio);
    }

    public AudioResponse clone() {
        return null;
    }
}

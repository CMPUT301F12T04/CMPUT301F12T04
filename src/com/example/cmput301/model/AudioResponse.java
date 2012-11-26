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
package com.example.cmput301.model;

import java.util.Date;

import android.provider.MediaStore.Audio;

public class AudioResponse extends Response {
	private static final long serialVersionUID = 1L;
	private Audio audio;

    public AudioResponse(Audio audio, String annotation, Date timestamp) {
        super(annotation, timestamp);
        this.audio = audio;
    }

    public Audio getAudio() {
        return this.audio;
    }

    public Response clone() {
        return null;
    }
}

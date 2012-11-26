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

/**
 * This is a specific type of response which can be added to a class that
 * required this type of response.
 */
public class TextResponse extends Response {
	private static final long serialVersionUID = 1L;

	/**
     * The basic constructor for a textResponse
     *
     * @param content //Content for this type of response is a string.
     * @param timestamp
     */
    public TextResponse(String content, Date timestamp) {
        super(null, timestamp);
        this.content = content;
    }

    // ---- Getters / Setters ---- //
    public String getContent() {
        return (String) this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {

        return getContent() + " " + this.getTimestamp().toString();
    }

    @Override
    public Response clone() {
        Response clone = new TextResponse(((String) this.content).substring(0), (Date) this.timestamp.clone());
        clone.setAnnotation(this.annotation);

        return clone;
    }
}

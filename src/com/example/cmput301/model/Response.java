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

import java.io.Serializable;
import java.util.Date;
/**
 * This is the basic model for the responses which represent the responses given
 * to a posted "Task" There are multiple types of responses that can be added.
 */
public abstract class Response implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	protected Date timestamp;
    protected String annotation;
    protected Object content;

    /**
     * Basic Constructor for the response classes.
     * @param annotation    A Text annotation that will be added to the response
     * @param timestamp     A timestamp which should be added to the response.
     */
    public Response(String annotation, Date timestamp) {
        this.annotation = annotation;
        this.timestamp = timestamp;
    }
    
    // ---- Getters / Setters ---- //
    public Object getContent() {
        return this.content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getAnnotation() {
        return this.annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object another) {

        if (another instanceof Response) {
            Response anotherResp = (Response) another;

            return anotherResp.getContent().equals(this.getContent());
        } else {
            return false;
        }
    }
    
    @Override
    public abstract Response clone();
}

package com.example.cmput301;

import java.io.Serializable;
import java.util.Date;

public abstract class Response implements Serializable, Cloneable {

    protected Date timestamp;
    protected String annotation;
    protected Object content;

    public Response(String annotation, Date timestamp) {
        this.annotation = annotation;
        this.timestamp = timestamp;
    }

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

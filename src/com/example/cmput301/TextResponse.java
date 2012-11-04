package com.example.cmput301;

import java.util.Date;

public class TextResponse extends Response {

    public TextResponse(String content, Date timestamp) {
        super(null, timestamp);
        this.content = content;
    }

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
        Response clone = new TextResponse((String) this.content, this.timestamp);
        clone.setAnnotation(this.annotation);

        return clone;
    }
}

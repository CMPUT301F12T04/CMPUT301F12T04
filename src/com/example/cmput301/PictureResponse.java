package com.example.cmput301;

import java.util.Date;

import android.graphics.Picture;

public class PictureResponse extends Response {

    private Picture picture;

    public PictureResponse(Picture picture, String annotation, Date timestamp) {
        super(annotation, timestamp);
        this.picture = picture;
    }

    public Picture getPicture() {
        return this.picture;
    }

    public Response clone() {
        return null;

    }
}

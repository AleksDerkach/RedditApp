package com.example.redditapp;

import android.graphics.drawable.Drawable;

public class TopNews {

    private String mAuthor;
    private int mMillisecondsDate;
    private String mThumbnail;
    private int mComments;
    private String mImageUrl;

    public TopNews(String author, int dateCreated, String thumbnailUrl, int comments, String imageUrl) {
        mAuthor = author;
        mMillisecondsDate = dateCreated*1000;
        mThumbnail = thumbnailUrl;
        mComments = comments;
        mImageUrl = imageUrl;

    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getThumbnailUrl() {
        return mThumbnail;
    }

    public int getDateInMilliseconds() {
        return mMillisecondsDate;
    }

    public int getComments() {
        return mComments;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

}

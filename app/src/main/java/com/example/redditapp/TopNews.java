package com.example.redditapp;


public class TopNews {

    private String mAuthor;
    private long mMillisecondsDate;
    private String mThumbnail;
    private int mComments;
    private String mImageUrl;

    public TopNews(String author, long dateCreated, String thumbnailUrl, int comments, String imageUrl) {
        mAuthor = author;
        mMillisecondsDate = dateCreated;
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

    public long getDateInMilliseconds() {
        return mMillisecondsDate;
    }

    public int getComments() {
        return mComments;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

}

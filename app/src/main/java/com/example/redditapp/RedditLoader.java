package com.example.redditapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class RedditLoader extends AsyncTaskLoader<List<TopNews>> {
    private static final String LOG_TAG = RedditLoader.class.getSimpleName();
    private String mUrl;

    public RedditLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<TopNews> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<TopNews> topNews = QueryUtils.fetchEarthquakeData(mUrl);
        return topNews;
    }
}

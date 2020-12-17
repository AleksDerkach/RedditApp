package com.example.redditapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<TopNews>> {

    private  static  final int EARTHQUAKE_LOADER_ID = 1;
    private NewsAdapter mAdapter;
    private static final String USGS_REQUEST_URL = "https://www.reddit.com/top.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.mainList);
        mAdapter = new NewsAdapter(this, new ArrayList<TopNews>());
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TopNews currTopNews = mAdapter.getItem(position);
                Uri topNewsUri = Uri.parse(currTopNews.getImageUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, topNewsUri);
                startActivity(websiteIntent);
            }
        });

//        TopNewsAsyncTask task = new TopNewsAsyncTask();
//        task.execute(USGS_REQUEST_URL);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

    }

    @Override
    public Loader<List<TopNews>> onCreateLoader(int i, Bundle bundle) {
        return new RedditLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<TopNews>> loader, List<TopNews> topNews) {
        mAdapter.clear();
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        if (topNews != null && !topNews.isEmpty()) {
            mAdapter.addAll(topNews);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<TopNews>> loader) {
        mAdapter.clear();
    }

//    private class TopNewsAsyncTask extends AsyncTask<String, Void, List<TopNews>> {
//
//        @Override
//        protected List<TopNews> doInBackground(String... urls) {
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//
//            List<TopNews> result = QueryUtils.fetchEarthquakeData(urls[0]);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(List<TopNews> data) {
//            super.onPostExecute(data);
//
//            mAdapter.clear();
//
//            if (data != null && !data.isEmpty()) {
//                mAdapter.addAll(data);
//            }
//        }
//    }
}

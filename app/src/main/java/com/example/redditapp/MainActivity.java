package com.example.redditapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

        TopNewsAsyncTask task = new TopNewsAsyncTask();
        task.execute(USGS_REQUEST_URL);
    }

    private class TopNewsAsyncTask extends AsyncTask<String, Void, List<TopNews>> {

        @Override
        protected List<TopNews> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<TopNews> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<TopNews> data) {
            super.onPostExecute(data);

            mAdapter.clear();

            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}

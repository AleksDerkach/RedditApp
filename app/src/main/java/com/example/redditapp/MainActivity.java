package com.example.redditapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter mAdapter;
    private static final String USGS_REQUEST_URL = "https://www.reddit.com/top.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<TopNews> listNews = new ArrayList<>();
//        listNews.add(new TopNews("Author", "01/11/2020", ,"10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));
//        listNews.add(new TopNews("Author", "01/11/2020", "10"));

        ListView listView = findViewById(R.id.mainList);
        mAdapter = new NewsAdapter(this, listNews);
        listView.setAdapter(mAdapter);

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

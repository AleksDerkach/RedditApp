package com.example.redditapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
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

    //private ImageView mImageView;
    private NewsAdapter mAdapter;
    private static final String USGS_REQUEST_URL = "https://www.reddit.com/top.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this ,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1
        );

        ActivityCompat.requestPermissions(MainActivity.this ,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1
        );

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

        //mImageView = listView.findViewById(R.id.imageNews);

//        Button btnSaveToG = listView.findViewById(R.id.btnSave);
//
//        View.OnClickListener clickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveImageToGallery();
//            }
//        };
//        btnSaveToG.setOnClickListener(clickListener);
    }

//    private void saveImageToGallery() {
//        //to get the image from the ImageView (say iv)
//        BitmapDrawable draw = (BitmapDrawable) mImageView.getDrawable();
//        Bitmap bitmap = draw.getBitmap();
//
//        FileOutputStream outStream = null;
//        File sdCard = Environment.getExternalStorageDirectory();
//        File dir = new File(sdCard.getAbsolutePath() + "/MyPics");
//        dir.mkdirs();
//        String fileName = String.format("%d.jpg", System.currentTimeMillis());
//        File outFile = new File(dir, fileName);
//        try {
//            outStream = new FileOutputStream(outFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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

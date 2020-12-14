package com.example.redditapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class NewsAdapter extends ArrayAdapter<TopNews> {

    ImageView mImageView;
    View ListItemView;

    public NewsAdapter(@NonNull Context context, List<TopNews> topNewsObjects) {
        super(context, 0, topNewsObjects);
    }

//    ActivityCompat.requestPermissions((Activity) getContext(),
//                new String[]{
//        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1
//            );
//
//        ActivityCompat.requestPermissions((Activity) getContext(),
//                new String[]{
//        Manifest.permission.READ_EXTERNAL_STORAGE}, 1
//            );

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        ListItemView = convertView;
        if (ListItemView == null) {
            ListItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_news, parent, false);
        }

        TopNews currentTopNews = getItem(position);

        // Author
        TextView authorView = (TextView) ListItemView.findViewById(R.id.author);
        authorView.setText(currentTopNews.getAuthor());

        // date created
        Date dateObject = new Date(currentTopNews.getDateInMilliseconds());

        TextView dateAddView = (TextView) ListItemView.findViewById(R.id.date);
        String formatedDate = formatDate(dateObject);
        dateAddView.setText(formatedDate);

        //image
        mImageView = ListItemView.findViewById(R.id.imageNews);
        ImageManager.fetchImage(currentTopNews.getThumbnailUrl(), mImageView);

        //quantity comments
        TextView commentsView = (TextView) ListItemView.findViewById(R.id.comments);
        commentsView.setText(new Integer(currentTopNews.getComments()).toString());

        Button btnSaveToG = ListItemView.findViewById(R.id.btnSave);

        btnSaveToG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView selectImageView = ListItemView.findViewById(R.id.imageNews);
                saveImageToGallery(selectImageView);
            }
        });
        return ListItemView;
    }

    private void saveImageToGallery(ImageView pImageView) {

        //to get the image from the ImageView (say iv)
        BitmapDrawable draw = (BitmapDrawable) pImageView.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        FileOutputStream outStream = null;
        File sdCard = new File(Environment.getExternalStorageState());
        File dir = new File(sdCard.getAbsolutePath()); // + "/MyPics"
        dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return  dateFormat.format(dateObject);
    }
}

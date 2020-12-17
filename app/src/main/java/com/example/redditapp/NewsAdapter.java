package com.example.redditapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class NewsAdapter extends ArrayAdapter<TopNews> {

    ImageView mImageView;
    View ListItemView;
    Context mContext;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public NewsAdapter(@NonNull Context context, List<TopNews> topNewsObjects) {
        super(context, 0, topNewsObjects);
        mContext = context;
    }

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
        authorView.setText("Posted by "+currentTopNews.getAuthor());

        // date created
        long mls = currentTopNews.getDateInMilliseconds();

        TextView dateAddView = (TextView) ListItemView.findViewById(R.id.date);
        String formatedDate = getTimeAgo(mls);
        dateAddView.setText(formatedDate);

        //image
        mImageView = ListItemView.findViewById(R.id.imageNews);
        ImageManager.fetchImage(currentTopNews.getThumbnailUrl(), mImageView);

        //quantity comments
        TextView commentsView = (TextView) ListItemView.findViewById(R.id.comments);
        //int comments = currentTopNews.getComments();
        commentsView.setText(new Integer(currentTopNews.getComments()).toString());

        Button btnSaveToG = ListItemView.findViewById(R.id.btnSave);
        btnSaveToG.setTag(position);

        btnSaveToG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                boolean checkPermission = (permission == PackageManager.PERMISSION_GRANTED);

                if (checkPermission) {
                    ActivityCompat.requestPermissions(
                            (Activity) mContext ,
                            new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1
                    );
                    LinearLayout linLayout = (LinearLayout) view.getParent();
                    LinearLayout linLayout2 = (LinearLayout) linLayout.getParent();
                    ImageView currentImageView = (ImageView) linLayout2.getChildAt(0);
                    saveImageToGallery(currentImageView);
                } else {

                }
            }
        });
        return ListItemView;
    }

    private void saveImageToGallery(ImageView mImageView) {

        BitmapDrawable draw = (BitmapDrawable) mImageView.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "any_picture_name");
        values.put(MediaStore.Images.Media.BUCKET_ID, "test");
        values.put(MediaStore.Images.Media.DESCRIPTION, "test Image taken");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        ContentResolver resolver = mContext.getContentResolver();

        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        OutputStream outstream = null;
        try {
            outstream = resolver.openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outstream);
            outstream.close();
            Toast.makeText(mContext.getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext.getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void saveImageToGallery_level_less_29(ImageView pImageView) {

        //to get the image from the ImageView (say iv)
        BitmapDrawable draw = (BitmapDrawable) pImageView.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        File path = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "DemoPicture.jpg");
        try {

            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(mContext,
                    new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }

//    private String formatDate(Date dateObject) {
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
//        return  dateFormat.format(dateObject);
//    }

    public String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            //if timestamp given in seconds, convert to millis time *= 1000;
            time *= 1000;
        }

        Date nowTime = new Date();
        long now = nowTime.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize

        final long diff = now - time;

        if (diff < MINUTE_MILLIS) { return "just now"; }
        else if (diff < 2 * MINUTE_MILLIS) { return "a minute ago"; }
        else if (diff < 50 * MINUTE_MILLIS) { return diff / MINUTE_MILLIS + " minutes ago"; }
        else if (diff < 90 * MINUTE_MILLIS) { return "an hour ago"; }
        else if (diff < 24 * HOUR_MILLIS) { return diff / HOUR_MILLIS + " hours ago"; } else if (diff < 48 * HOUR_MILLIS) { return "yesterday"; }
        else { return diff / DAY_MILLIS + " days ago"; }
    }
}

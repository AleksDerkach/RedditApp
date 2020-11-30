package com.example.redditapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewsAdapter extends ArrayAdapter<TopNews> {

    public NewsAdapter(@NonNull Context context, @NonNull List<TopNews> topNewsObjects) {
        super(context, 0, topNewsObjects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View ListItemView = convertView;
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
        ImageView imageView = ListItemView.findViewById(R.id.imageNews);
        //imageView.setImageDrawable(Drawable.createFromPath(currentTopNews.getThumbnailUrl()));
        ImageManager.fetchImage(currentTopNews.getThumbnailUrl(), imageView);

        //quantity comments
        TextView commentsView = (TextView) ListItemView.findViewById(R.id.comments);
        commentsView.setText(new Integer(currentTopNews.getComments()).toString());

        return ListItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return  dateFormat.format(dateObject);
    }
}

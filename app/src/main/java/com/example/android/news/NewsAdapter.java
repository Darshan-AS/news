package com.example.android.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


class NewsAdapter extends ArrayAdapter<News> {

    NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        final News currentNews = getItem(position);

        TextView titleView = listItemView.findViewById(R.id.title);
        assert currentNews != null;
        titleView.setText(currentNews.getTitle());

        TextView descriptionView = listItemView.findViewById(R.id.description);
        if (currentNews.getDescription() == null)
            descriptionView.setVisibility(View.GONE);
        else
            descriptionView.setText(currentNews.getDescription());

        ImageView thumbnailView = listItemView.findViewById(R.id.thumbnail);
        Picasso.with(getContext())
                .load(currentNews.getUrlToImage())
                .placeholder(R.drawable.news_image_placeholder)
                .into(thumbnailView);

        TextView sourceView = listItemView.findViewById(R.id.source);
        String source = "Source: " + currentNews.getSource();
        sourceView.setText(source);

        TextView publishedAtView = listItemView.findViewById(R.id.published_at);
        publishedAtView.setText(formatPublishedAt(currentNews.getPublishedAt()));

        return listItemView;
    }

    private String formatPublishedAt(String publishedAt) {
        return publishedAt.substring(0, publishedAt.indexOf("T")) + " " + publishedAt.substring(publishedAt.indexOf("T") + 1, publishedAt.lastIndexOf(":"));
    }
}
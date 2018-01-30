package com.example.android.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String url;

    NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (url == null)
            return null;
        return NewsUtils.fetchNewsData(url);
    }
}

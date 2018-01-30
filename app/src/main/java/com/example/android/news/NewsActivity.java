package com.example.android.news;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    static final String LOG_TAG = NewsActivity.class.getName();
    private static final String API_KEY = "9236a3590930493594190fbd69e03d93";
    private static final String GOOGLE_NEWS_QUERY_URL = "https://newsapi.org/v2";
    private NewsAdapter newsAdapter;
    private TextView emptyTextView;
    private TextView headingTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        final ListView listView = findViewById(R.id.news_list);
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(newsAdapter);
        emptyTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyTextView);

        Intent searchIntent = getIntent();
        Bundle bundle = null;
        if (searchIntent != null && Intent.ACTION_SEARCH.equals(searchIntent.getAction())) {
            String query = searchIntent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SearchRecentNewsSuggestionProvider.AUTHORITY, SearchRecentNewsSuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            bundle = new Bundle();
            bundle.putString("query", query);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = newsAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(news.getUrl()));
                startActivity(intent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if (isConnected) {
            getLoaderManager().initLoader(0, bundle, this);
        } else {
            progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle bundle) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minNewsArticles = preferences.getString(getString(R.string.min_news_articles_key), getString(R.string.min_news_articles_default_value));
        String categorySelected = preferences.getString(getString(R.string.category_key), getString(R.string.category_default_value));

        Uri baseUri = Uri.parse(GOOGLE_NEWS_QUERY_URL);
        Uri.Builder builder = baseUri.buildUpon();
        if (bundle != null){
            builder.appendPath("everything");
            String query = bundle.getString("query");
            builder.appendQueryParameter("q", query);
            headingTextView = findViewById(R.id.heading);
            headingTextView.setText(query);
        } else {
            builder.appendPath("top-headlines");
            builder.appendQueryParameter("country", "us");
            builder.appendQueryParameter("category", categorySelected);
        }
        builder.appendQueryParameter("language", "en");
        builder.appendQueryParameter("pageSize", minNewsArticles);
        builder.appendQueryParameter("apiKey", API_KEY);
        return new NewsLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        emptyTextView = findViewById(R.id.empty_view);
        emptyTextView.setText(R.string.no_news_available);
        newsAdapter.clear();

        if (!newsList.isEmpty()) {
            headingTextView = findViewById(R.id.heading);
            headingTextView.setVisibility(View.VISIBLE);
            newsAdapter.addAll(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (id == R.id.search) {
            onSearchRequested();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested(@Nullable SearchEvent searchEvent) {
        return super.onSearchRequested(searchEvent);
    }
}


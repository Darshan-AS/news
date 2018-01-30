package com.example.android.news;

import android.content.SearchRecentSuggestionsProvider;

public class SearchRecentNewsSuggestionProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY ="com.example.SearchRecentNewsSuggestionProvider" ;
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchRecentNewsSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}

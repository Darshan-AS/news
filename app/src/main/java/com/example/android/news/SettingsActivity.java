package com.example.android.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference minNewsArticles = findPreference(getString(R.string.min_news_articles_key));
            bindPreferenceSummaryToValue(minNewsArticles);

            Preference category = findPreference(getString(R.string.category_key));
            bindPreferenceSummaryToValue(category);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String value = preferences.getString(preference.getKey(), "");
            preference.setOnPreferenceChangeListener(this);
            onPreferenceChange(preference, value);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String value = newValue.toString();
            preference.setSummary(value);
            return true;
        }
    }
}
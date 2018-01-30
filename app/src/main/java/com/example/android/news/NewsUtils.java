package com.example.android.news;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

class NewsUtils {

    public static ArrayList<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(NewsActivity.LOG_TAG, "Problem making the HTTP request", e);
        }
        return extractNews(jsonResponse);
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(NewsActivity.LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        if (url == null)
            return null;

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else
                Log.e(NewsActivity.LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
        } catch (IOException e) {
            Log.e(NewsActivity.LOG_TAG, "Problem retrieving the JSON result", e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return builder.toString();
    }

    private static ArrayList<News> extractNews(String jsonResponse) {
        ArrayList<News> newsList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonNewsArray = jsonObject.getJSONArray("articles");
            for (int i = 0; i < jsonNewsArray.length(); i++) {
                JSONObject jsonNewsObject = jsonNewsArray.getJSONObject(i);
                String title = jsonNewsObject.getString("title");
                String description = jsonNewsObject.getString("description");
                String author = jsonNewsObject.getString("author");
                String url = jsonNewsObject.getString("url");
                String urlToImage = jsonNewsObject.getString("urlToImage");
                String publishedAt = jsonNewsObject.getString("publishedAt");
                String source = jsonNewsObject.getJSONObject("source").getString("name");

                News news = new News(title, description, author, url, urlToImage, publishedAt, source);
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e(NewsActivity.LOG_TAG, "Error parsing JSON results", e);
        }
        return newsList;
    }
}

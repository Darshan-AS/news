package com.example.android.news;

class News {
    private String title, description, author, url, urlToImage, publishedAt, source;

    News(String title, String description, String author, String url, String urlToImage, String publishedAt, String source) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getSource() {
        return source;
    }

}

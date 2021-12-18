package com.example.landview.HomeFragmentSection.News;

public class NewItem {
    private int imageNews;
    private String textNews,linkNews;
    public NewItem() {
    }

    public NewItem(int imageNews, String textNews, String linkNews) {
        this.imageNews = imageNews;
        this.textNews = textNews;
        this.linkNews = linkNews;
    }

    public int getImageNews() {
        return imageNews;
    }

    public void setImageNews(int imageNews) {
        this.imageNews = imageNews;
    }

    public String getTextNews() {
        return textNews;
    }

    public void setTextNews(String textNews) {
        this.textNews = textNews;
    }

    public String getLinkNews() {
        return linkNews;
    }

    public void setLinkNews(String linkNews) {
        this.linkNews = linkNews;
    }
}

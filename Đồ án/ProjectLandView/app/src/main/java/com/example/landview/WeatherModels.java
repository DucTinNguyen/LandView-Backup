package com.example.landview;

public class WeatherModels {
    private String day,temp,description,urlIcon;

    public WeatherModels() {
    }

    public WeatherModels(String day, String temp, String description, String urlIcon) {
        this.day = day;
        this.temp = temp;
        this.description = description;
        this.urlIcon = urlIcon;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }
}
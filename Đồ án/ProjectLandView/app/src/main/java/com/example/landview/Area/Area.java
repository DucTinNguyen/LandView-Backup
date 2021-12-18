package com.example.landview.Area;

import java.io.Serializable;
import java.util.ArrayList;

public class Area implements Serializable {
    private String address;
    private String areaName;
    private String description;
    private String geohash;
    private double latitude;
    private double longitude;
    private String id;
    private ArrayList<String> images;
    private String name;
    private ArrayList<String> likes;

    // Constructor

    public Area(){ // Do not delete empty constructor

    }

    public Area(String address, String areaName, String description, String geohash,
                double latitude, double longitude, String id, ArrayList<String> images,
                String name, ArrayList<String> likes) {
        this.address = address;
        this.areaName = areaName;
        this.description = description;
        this.geohash = geohash;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.images = images;
        this.name = name;
        this.likes = likes;
    }

    /********************************************************************************/

    // Getter
    public String getAddress() {
        return address;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getDescription() {
        return description;
    }

    public String getGeohash() {
        return geohash;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    /********************************************************************************/

    // Setter

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    /********************************************************************************/

    @Override
    public String toString() {
        return "Area{" +
                "address='" + address + '\'' +
                ", areaName='" + areaName + '\'' +
                ", description='" + description + '\'' +
                ", geohash='" + geohash + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", id='" + id + '\'' +
                ", images=" + images +
                ", name='" + name + '\'' +
                ", likes=" + likes +
                '}';
    }
}

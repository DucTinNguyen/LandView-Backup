package com.example.landview.Place;

import java.io.Serializable;
import java.util.ArrayList;

public class Place implements Serializable {

    private String address;
    private String areaId;
    private String id;
    private ArrayList<String> images;
    // private ArrayList<String> likesList;
    private String areaName;
    private String name;
    private String type;
    private float rating; // tổng rating
    private int totalRate; // số lượng rate
    private double latitude;
    private double longitude;
    private String geohash;

    // Khi bạn nhận ra có thể lưu các DocumentReference dưới dạng string
    private String path;


    public Place() {
    }

    public Place(String address, String areaId, String id, ArrayList<String> images, String areaName,
                 String name, String type, float rating, int totalRate, double latitude, double longitude,
                 String geohash, String path) {
        this.address = address;
        this.areaId = areaId;
        this.id = id;
        this.images = images;
        this.areaName = areaName;
        this.name = name;
        this.type = type;
        this.rating = rating;
        this.totalRate = totalRate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geohash = geohash;
        this.path = path;
    }

    /******************************************************************************************************************/

    public String getAddress() {
        return address;
    }

    public String getAreaId() {
        return areaId;
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

    public String getType() {
        return type;
    }

    public float getRating() {
        return rating;
    }

    public int getTotalRate() {
        return totalRate;
    }

    public String getPath() {
        return path;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getGeohash() {
        return geohash;
    }

    public String getAreaName() {
        return areaName;
    }

    /*******************************************************************************************************/

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setTotalRate(int totalRate) {
        this.totalRate = totalRate;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Override
    public String toString() {
        return "Place{" +
                "address='" + address + '\'' +
                ", areaId='" + areaId + '\'' +
                ", id='" + id + '\'' +
                ", images=" + images +
                ", areaName='" + areaName + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", rating=" + rating +
                ", totalRate=" + totalRate +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", geohash='" + geohash + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}

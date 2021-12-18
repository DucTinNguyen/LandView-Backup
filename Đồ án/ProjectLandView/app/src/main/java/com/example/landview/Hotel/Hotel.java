package com.example.landview.Hotel;

import java.io.Serializable;
import java.util.ArrayList;

public class Hotel implements Serializable {

    /*
     *  Còn thiếu cái description đó
     *
     *
     * */

    private String address;
    private String areaId;
    private String geohash;
    private double latitude;
    private double longitude;
    private String id;
    private ArrayList<String> images;
    private ArrayList<String> likesList;
    private String name;
    private String type;
    private double price; // giá
    private float rating; // tổng rating
    private int totalRate; // số lượng rate

    private ArrayList<String> utilities; // bổ xung sau

    public Hotel() {
    }

    public Hotel(String address, String areaId, String geohash, double latitude, double longitude,
                 String id, ArrayList<String> images, ArrayList<String> likesList, String name,
                 String type,double price ,float rating, int totalRate) {
        this.address = address;
        this.areaId = areaId;
        this.geohash = geohash;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.images = images;
        this.likesList = likesList;
        this.name = name;
        this.type = type;
        this.price = price;
        this.rating = rating;
        this.totalRate = totalRate;
    }

    /******************************************************************************************************************/

    // getter

    public String getAddress() {
        return address;
    }

    public String getAreaId() {
        return areaId;
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

    public ArrayList<String> getLikesList() {
        return likesList;
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

    public double getPrice() {
        return price;
    }

    public int getTotalRate() {
        return totalRate;
    }

    /******************************************************************************************************************/

    // setter
    public void setAddress(String address) {
        this.address = address;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
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

    public void setLikesList(ArrayList<String> likesList) {
        this.likesList = likesList;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTotalRate(int totalRate) {
        this.totalRate = totalRate;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "address='" + address + '\'' +
                ", areaId='" + areaId + '\'' +
                ", geohash='" + geohash + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", id='" + id + '\'' +
                ", images=" + images +
                ", likesList=" + likesList +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                ", totalRate=" + totalRate +
                '}';
    }
}

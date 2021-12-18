package com.example.landview.Comment;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Comment implements Parcelable {
    String avatar;
    String username;
    String userId;
    String comment;
    Timestamp date;
    float rating;

    public Comment(Parcel in){
        this.avatar = in.readString();
        this.username = in.readString();
        this.userId = in.readString();
        this.comment = in.readString();
        this.date = in.readParcelable(Timestamp.class.getClassLoader());
        this.rating = in.readFloat();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(avatar);
        dest.writeString(username);
        dest.writeString(userId);
        dest.writeString(comment);
        dest.writeParcelable(date, flags);
        dest.writeFloat(rating);
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int i) {
            return new Comment[i];
        }
    };


    public Comment(){

    }

    public Comment(String avatar, String username, String userId, String comment, Timestamp date, float rating) {
        this.avatar = avatar;
        this.username = username;
        this.userId = userId;
        this.comment = comment;
        this.date = date;
        this.rating = rating;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getDate() {
        return date;
    }

    public float getRating() {
        return rating;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                ", comment='" + comment + '\'' +
                ", time=" + date +
                ", rating=" + rating +
                '}';
    }
}

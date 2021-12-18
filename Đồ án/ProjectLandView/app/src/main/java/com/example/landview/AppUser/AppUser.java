package com.example.landview.AppUser;

public class AppUser {

    private String username;
    private String avatar;
    private String UID;

    private static AppUser INSTANCE;

    private AppUser(){

    }

    public static AppUser getInstance(){
        if(INSTANCE == null){
            INSTANCE = new AppUser();
        } else {
            return INSTANCE;
        }
        return  INSTANCE;
    }


    public AppUser(String userName, String avatar, String UID) {
        this.username = userName;
        this.avatar = avatar;
        this.UID = UID;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUserName() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUID() {
        return UID;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", UID='" + UID + '\'' +
                '}';
    }
}

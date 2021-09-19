package com.example.audiostream.Chat;

public class UserModel {
    String username,email,image,uid,devicetoken;
    public UserModel()
    {}

    public UserModel(String username, String email, String image,String uid,String devicetoken) {
        this.username = username;
        this.email = email;
        this.image = image;
        this.uid=uid;
        this.devicetoken=devicetoken;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getUid() {
        return uid;
    }

    public String getDevicetoken() {
        return devicetoken;
    }
}

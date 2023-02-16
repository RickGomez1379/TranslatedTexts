package com.example.translatorapp;

public class User {

    private String email;
    private String profilePhoto;
    public User(){

    }

    public User(String email, String profilePhoto) {
        this.email = email;
        this.profilePhoto = profilePhoto;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}

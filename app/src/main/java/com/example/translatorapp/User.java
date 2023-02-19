package com.example.translatorapp;

public class User {

    private String email;
    private String profilePhoto;
    private String username;
    private int languageCode;
    public User(){

    }

    public int getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(int languageCode) {
        this.languageCode = languageCode;
    }

    public User(String email, String profilePhoto, String username, int language) {
        this.email = email;
        this.profilePhoto = profilePhoto;
        this.username = username;
        this.languageCode = language;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

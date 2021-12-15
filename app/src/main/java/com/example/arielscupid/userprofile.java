package com.example.arielscupid;

public class userprofile {

    private String username, UserUID,about,gender,wantedGender;

    public userprofile() {
    }

    public userprofile(String username, String userUID,String About,String Gender, String WantedGender) {
        this.username = username;
        this.UserUID = userUID;
        this.about=About;
        this.gender = Gender;
        this.wantedGender = WantedGender;


    }

    public String getWantedGender() {
        return wantedGender;
    }

    public void setWantedGender(String wantedGender) {
        this.wantedGender = wantedGender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAbout() {
        return about; }

    public void setAbout(String About) {
        this.about = About;
    }

    public String getGender() {
        return gender; }

    public void setGender(String Gender) {
        this.gender = Gender;
    }


    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        this.UserUID = userUID;
    }
}

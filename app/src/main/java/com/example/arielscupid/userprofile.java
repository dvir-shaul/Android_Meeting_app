package com.example.arielscupid;

public class userprofile {

    private String username, UserUID,about;

    public userprofile() {
    }

    public userprofile(String username, String userUID,String About) {
        this.username = username;
        this.UserUID = userUID;
        this.about=About;


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

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        this.UserUID = userUID;
    }
}

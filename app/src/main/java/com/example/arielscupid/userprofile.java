package com.example.arielscupid;

public class userprofile {

    private String username, UserUID;

    public userprofile() {
    }

    public userprofile(String username, String userUID) {
        this.username = username;
        this.UserUID = userUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        this.UserUID = userUID;
    }
}

package com.example.arielscupid;

public class firebasemodel {

    String name;
    String image;
    String uid;
    String status;
    String about;
    Long questions;


    public firebasemodel(String name, String image, String uid, String status,String about,Long questions) {
        this.name = name;
        this.image = image;
        this.uid = uid;
        this.status = status;
        this.about = about;
        this.questions = questions;
    }

    public firebasemodel() {
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Long getQuestions() {
        return questions;
    }

    public void setQuestions(Long questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

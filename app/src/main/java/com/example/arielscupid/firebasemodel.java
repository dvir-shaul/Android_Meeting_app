package com.example.arielscupid;

public class firebasemodel {

    String name;
    String image;
    String uid;
    String status;
    String about;
    Long questions;
    String gender;
    String WantGender;


    public firebasemodel(String name, String image, String uid, String status,String about,Long questions,String genDer,String WantGender) {
        this.name = name;
        this.image = image;
        this.uid = uid;
        this.status = status;
        this.about = about;
        this.questions = questions;
        this.gender = genDer;
        this.WantGender = WantGender;
    }

    public firebasemodel() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWantGender() {
        return WantGender;
    }

    public void setWantGender(String wantGender) {
        WantGender = wantGender;
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

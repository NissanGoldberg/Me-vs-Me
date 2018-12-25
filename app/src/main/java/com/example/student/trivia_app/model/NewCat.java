package com.example.student.trivia_app.model;

/**
 * Created by Student on 23/12/2018.
 */

public class NewCat {
    String name;
    String url;

    public NewCat() {
    }

    public NewCat(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

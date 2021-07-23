package com.example.audiostream;

public class uploadArticles {
    public String name;
    public String url;

    public uploadArticles(){

    }
    public uploadArticles(String name,String url){
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}

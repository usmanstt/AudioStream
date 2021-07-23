package com.example.audiostream;

public class uploadMusic {
    public String name;
    public String url;

    uploadMusic(){

    }

    public uploadMusic(String name,String url){
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

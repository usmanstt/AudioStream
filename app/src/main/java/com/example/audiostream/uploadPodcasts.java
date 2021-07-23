package com.example.audiostream;

public class uploadPodcasts {
    public String name;
    public String url;

    public uploadPodcasts(){

    }
    public uploadPodcasts(String name,String url){
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

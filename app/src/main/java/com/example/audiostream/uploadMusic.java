package com.example.audiostream;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class uploadMusic implements Parcelable {
    private String name;
    private String url;
    private int likes;
    private int count;

    public uploadMusic() {
    }

    public uploadMusic(String name,String url,int likes,int count){
        this.name = name;
        this.url = url;
        this.likes = likes;
        this.count=count;
    }

    protected uploadMusic(Parcel in) {
        name = in.readString();
        url = in.readString();
        likes = in.readInt();
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeInt(likes);
        dest.writeInt(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<uploadMusic> CREATOR = new Creator<uploadMusic>() {
        @Override
        public uploadMusic createFromParcel(Parcel in) {
            return new uploadMusic(in);
        }

        @Override
        public uploadMusic[] newArray(int size) {
            return new uploadMusic[size];
        }
    };

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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package com.example.audiostream;

import android.os.Parcel;
import android.os.Parcelable;

public class uploadPodcasts implements Parcelable {
    public String name;
    public String url;
    int like;
    int count;

    public uploadPodcasts(){

    }

    public uploadPodcasts(String name, String url, int like,int count) {
        this.name = name;
        this.url = url;
        this.like = like;
        this.count=count;
    }

    protected uploadPodcasts(Parcel in) {
        name = in.readString();
        url = in.readString();
        like = in.readInt();
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeInt(like);
        dest.writeInt(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<uploadPodcasts> CREATOR = new Creator<uploadPodcasts>() {
        @Override
        public uploadPodcasts createFromParcel(Parcel in) {
            return new uploadPodcasts(in);
        }

        @Override
        public uploadPodcasts[] newArray(int size) {
            return new uploadPodcasts[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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


    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}

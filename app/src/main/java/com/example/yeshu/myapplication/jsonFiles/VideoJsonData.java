package com.example.yeshu.myapplication.jsonFiles;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoJsonData implements Parcelable {
    private String id;
    private String key;
    private String name;

    public VideoJsonData(String id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    private VideoJsonData(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<VideoJsonData> CREATOR = new Creator<VideoJsonData>() {
        @Override
        public VideoJsonData createFromParcel(Parcel in) {
            return new VideoJsonData(in);
        }

        @Override
        public VideoJsonData[] newArray(int size) {
            return new VideoJsonData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
    }
}

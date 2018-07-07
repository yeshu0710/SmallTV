package com.example.yeshu.myapplication.jsonFiles;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewJsonData implements Parcelable{
    private String author;
    private String content;
    private String reviewUrl;

    public ReviewJsonData(String author, String content, String reviewUrl) {
        this.author = author;
        this.content = content;
        this.reviewUrl = reviewUrl;
    }

    private ReviewJsonData(Parcel in) {
        author = in.readString();
        content = in.readString();
        reviewUrl = in.readString();
    }

    public static final Creator<ReviewJsonData> CREATOR = new Creator<ReviewJsonData>() {
        @Override
        public ReviewJsonData createFromParcel(Parcel in) {
            return new ReviewJsonData(in);
        }

        @Override
        public ReviewJsonData[] newArray(int size) {
            return new ReviewJsonData[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(reviewUrl);
    }
}

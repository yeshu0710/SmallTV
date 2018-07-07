package com.example.yeshu.myapplication.jsonFiles;

import android.os.Parcel;
import android.os.Parcelable;

public class TvJsonData implements Parcelable {
    private int id;
    private String name;
    private String original_name;
    private String first_aired_date;
    private String backdrop_path;
    private String original_language;
    private String poster_path;
    private String overview;

    private double vote_average;

    public TvJsonData(int id, String name, String original_name, String first_aired_date, String backdrop_path, String original_language, String poster_path, String overview, double vote_average) {
        this.id = id;
        this.name = name;
        this.original_name = original_name;
        this.first_aired_date = first_aired_date;
        this.backdrop_path = backdrop_path;
        this.original_language = original_language;
        this.poster_path = poster_path;
        this.overview = overview;
        this.vote_average = vote_average;
    }

    public TvJsonData(String sID, String sName, String sOriginalName, String sPosterPath, String sBackdrop, String sRating, String sFirstAiredDate, String sOverview) {
        this.id= Integer.parseInt(sID);
        this.name=sName;
        this.original_name=sOriginalName;
        this.poster_path=sPosterPath;
        this.vote_average= Double.parseDouble(sRating);
        this.backdrop_path=sBackdrop;
        this.overview=sOverview;
        this.first_aired_date=sFirstAiredDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getFirst_aired_date() {
        return first_aired_date;
    }

    public void setFirst_aired_date(String first_aired_date) {
        this.first_aired_date = first_aired_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(original_name);
        parcel.writeString(first_aired_date);
        parcel.writeString(backdrop_path);
        parcel.writeString(original_language);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeDouble(vote_average);
    }

    private TvJsonData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        original_name = in.readString();
        first_aired_date = in.readString();
        backdrop_path = in.readString();
        original_language = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        vote_average = in.readDouble();
    }

    public static final Creator<TvJsonData> CREATOR = new Creator<TvJsonData>() {
        @Override
        public TvJsonData createFromParcel(Parcel in) {
            return new TvJsonData(in);
        }

        @Override
        public TvJsonData[] newArray(int size) {
            return new TvJsonData[size];
        }
    };
}

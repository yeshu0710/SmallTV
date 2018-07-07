package com.example.yeshu.myapplication;

import android.net.Uri;
import android.provider.BaseColumns;

public class MyFavoriteContact {
    public final static String Authority="com.example.yeshu.myapplication";
    final static Uri Base_URI= Uri.parse("content://"+Authority);
    public final static String Path_table="favoritesList";

    public static final class FavShowDetails implements BaseColumns{
        public static final Uri Content_URI=Base_URI.buildUpon().appendPath(Path_table).build();

        public static final String Table_name="favoritesList";
        public static final String Column_ShowID="ShowID";
        public static final String Column_Name="Name";
        public static final String Column_OriginalName="OriginalName";
        public static final String Column_Poster_Path="PosterPath";
        public static final String Column_Backdrop="Backdrop";
        public static final String Column_Rating="Rating";
        public static final String Column_First_Aired_Date="FirstAiredDate";
        public static final String Column_Overview="Overview";
    }
}

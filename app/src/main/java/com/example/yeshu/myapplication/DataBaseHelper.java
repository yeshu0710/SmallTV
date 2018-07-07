package com.example.yeshu.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int  version=1;

    DataBaseHelper(Context context) {
        super(context, MyFavoriteContact.FavShowDetails.Table_name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String Show_Table = "CREATE TABLE " + MyFavoriteContact.FavShowDetails.Table_name + "("
                + MyFavoriteContact.FavShowDetails.Column_ShowID + " INTEGER  PRIMARY KEY " + ","
                + MyFavoriteContact.FavShowDetails.Column_Name + " TEXT" + ","
                + MyFavoriteContact.FavShowDetails.Column_OriginalName + " TEXT" + ","
                + MyFavoriteContact.FavShowDetails.Column_Poster_Path + " TEXT  " + ","
                + MyFavoriteContact.FavShowDetails.Column_Backdrop + " TEXT" + ","
                + MyFavoriteContact.FavShowDetails.Column_Rating + " TEXT" + ","
                + MyFavoriteContact.FavShowDetails.Column_Overview + " TEXT" + ","
                + MyFavoriteContact.FavShowDetails.Column_First_Aired_Date + " TEXT" + ")";

        sqLiteDatabase.execSQL(Show_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + MyFavoriteContact.FavShowDetails.Table_name);
    }

    public void showFavoriteShows(){
        String queryselected = "SELECT * FROM " + MyFavoriteContact.FavShowDetails.Table_name;
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        @SuppressLint("Recycle") Cursor showDetails =sqLiteDatabase.rawQuery(queryselected,null);
        String[] data =new  String[10];
        String string="";
        if (showDetails.moveToFirst()){
            do {

                data[0] = String.valueOf(showDetails.getInt(0));
                data[1] = showDetails.getString(1);
                data[2] = showDetails.getString(2);
                data[3] = showDetails.getString(3);
                data[4] = showDetails.getString(4);
                data[5] = showDetails.getString(5);
                data[6] = showDetails.getString(6);
                data[7] = showDetails.getString(7);
                string = string + data[1] + "\n" + data[2] + "\n" + data[3] + "\n" + data[4] + "\n" + data[5] + "\n" + data[6] + "\n" + data[7] + "\n";
            }while (showDetails.moveToNext());
        }
        sqLiteDatabase.close();
    }
}

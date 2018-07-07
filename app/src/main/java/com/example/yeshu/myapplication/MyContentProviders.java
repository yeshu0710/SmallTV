package com.example.yeshu.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyContentProviders extends ContentProvider {

    private DataBaseHelper dataBaseHelper;
    private static final int FAV_SHOW_DB=70;
    private static final int FAV_SHOW_DB_ID=120;
    private static final UriMatcher uriMatcher=buildUriMatcher();

    public MyContentProviders(){

    }

    private static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MyFavoriteContact.Authority,MyFavoriteContact.Path_table,FAV_SHOW_DB);
        uriMatcher.addURI(MyFavoriteContact.Authority, MyFavoriteContact.Path_table + "/*", FAV_SHOW_DB_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context =getContext();
        dataBaseHelper=new DataBaseHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case FAV_SHOW_DB:
                cursor = db.query(MyFavoriteContact.FavShowDetails.Table_name, strings, s, strings1, null, null, s1);
                break;
            case FAV_SHOW_DB_ID:
                cursor = db.query(MyFavoriteContact.FavShowDetails.Table_name, strings, MyFavoriteContact.FavShowDetails.Column_ShowID + "=" + s, null, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        long id;
        int match = uriMatcher.match(uri);
        Uri uriMatched = null;
        switch (match) {
            case FAV_SHOW_DB:
                id = db.insert(MyFavoriteContact.FavShowDetails.Table_name, null, contentValues);
                if (id > 0) {
                    uriMatched = ContentUris.withAppendedId(MyFavoriteContact.FavShowDetails.Content_URI, id);
                    Log.i("urimatch",MyFavoriteContact.FavShowDetails.Content_URI.toString());
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriMatched;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase sqDatabase = dataBaseHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int favMoviedeleted = 0;
        if (s == null) {
            s = "1";
        }
        switch (match) {
            case FAV_SHOW_DB:
                favMoviedeleted = sqDatabase.delete(MyFavoriteContact.FavShowDetails.Table_name, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        if (favMoviedeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favMoviedeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

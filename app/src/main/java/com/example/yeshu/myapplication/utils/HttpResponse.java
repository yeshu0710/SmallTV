package com.example.yeshu.myapplication.utils;

import android.net.Uri;

import com.example.yeshu.myapplication.BuildConfig;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpResponse {
    private final static String image_url="https://image.tmdb.org/t/p/w300";

    public URL bulidImgUrl(String path){
        String urlImage;
        urlImage = image_url+""+path;
        URL url=null;
        try {
            url=new URL(urlImage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public URL buildUrl(String path, String videoId, String purpuse){
        String API_key = BuildConfig.myAPI_key;
        Uri uri=Uri.parse(path+videoId).buildUpon().appendPath(purpuse).appendQueryParameter("api_key",API_key).build();
        URL url=null;
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  url;
    }

    public URL bulid(String img_url){
        Uri uri=Uri.parse(img_url);
        URL url=null;
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public String getResponseFromHttpUrl(URL url) throws IOException {
        String responseHttp;
        HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        InputStream inputStream=new BufferedInputStream(httpURLConnection.getInputStream());
        responseHttp=streamConversion(inputStream);
        return responseHttp;
    }
    private static String streamConversion(InputStream inputStream){
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder=new StringBuilder();
        String string;
        try {
            while ((string=bufferedReader.readLine())!=null){
                stringBuilder.append(string).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  stringBuilder.toString();
    }
}


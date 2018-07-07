package com.example.yeshu.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeshu.myapplication.Adopters.ReviewAdapter;
import com.example.yeshu.myapplication.Adopters.VideoAdapter;
import com.example.yeshu.myapplication.jsonFiles.ReviewJsonData;
import com.example.yeshu.myapplication.jsonFiles.VideoJsonData;
import com.example.yeshu.myapplication.utils.HttpResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowInfo extends AppCompatActivity {

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.id_toolbar) Toolbar toolbar;
    @BindView(R.id.background_poster) ImageView backgroudPoster;
    @BindView(R.id.poster_of_the_show) ImageView poster;
    @BindView(R.id.show_title) TextView title;
    @BindView(R.id.rating_tv) TextView rating;
    @BindView(R.id.first_aired_date_tv) TextView date;
    @BindView(R.id.overview_tv) TextView overview;
    @BindView(R.id.favImage) ImageView favImage;
    @BindView(R.id.vidoesRecycleView)RecyclerView videoRecycleView;
    @BindView(R.id.reviewRecycleView) RecyclerView reviewRecycleView;
    @BindView(R.id.ShowInfoVideo) TextView noVideo;
    @BindView(R.id.ShowInfoReviews) TextView noReview;
    ContentValues contentValues;
    int favdisplay=0;

    String showID;
    String baseURL="https://api.themoviedb.org/3/tv/";
    String[] myShowDetails;

    String s="No Reviews";

    HttpResponse httpResponse=new HttpResponse();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        myShowDetails = getIntent().getStringArrayExtra("details");

        collapsingToolbarLayout.setTitle(myShowDetails[1]);

        Picasso.with(this).load("https://image.tmdb.org/t/p/w300/" + myShowDetails[4]).into(backgroudPoster);
        Picasso.with(this).load("https://image.tmdb.org/t/p/w300/" + myShowDetails[6]).into(poster);

        title.setText(myShowDetails[2]);
        rating.setText(myShowDetails[8]);
        date.setText(myShowDetails[3]);
        if (!myShowDetails[7].equals(""))
            overview.setText(String.format("\t\t%s", myShowDetails[7]));
        else
            overview.setText(R.string.no_overview);

        showID = myShowDetails[0];

        Uri uri=Uri.parse(MyFavoriteContact.FavShowDetails.Content_URI + "/*");
        Cursor cursor=getContentResolver().query(uri,null,showID,null,null);
        Log.i("cursor", String.valueOf(cursor));
        if (cursor.getCount()>0)
        {
            favImage.setImageResource(R.mipmap.ic_menu_favorite);
            favdisplay=1;
        }
        else
        {
            favImage.setImageResource(R.mipmap.ic_menu_not_favorite);
            favdisplay=0;
        }

        favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favdisplay==1) {
                    favdisplay=0;
                    getContentResolver().delete(MyFavoriteContact.FavShowDetails.Content_URI,MyFavoriteContact.FavShowDetails.Column_ShowID + " =? ",new String[]{showID});
                    favImage.setImageResource(R.mipmap.ic_menu_not_favorite);
                    Toast.makeText(ShowInfo.this, R.string.remove_from_fav, Toast.LENGTH_SHORT).show();
                } else {
                    contentValues=new ContentValues();
                    contentValues.put(MyFavoriteContact.FavShowDetails.Column_ShowID,myShowDetails[0]);
                    contentValues.put(MyFavoriteContact.FavShowDetails.Column_Name,myShowDetails[1]);
                    contentValues.put(MyFavoriteContact.FavShowDetails.Column_OriginalName,myShowDetails[2]);
                    contentValues.put(MyFavoriteContact.FavShowDetails.Column_Poster_Path,myShowDetails[6]);
                    contentValues.put(MyFavoriteContact.FavShowDetails.Column_Backdrop,myShowDetails[4]);
                    contentValues.put(MyFavoriteContact.FavShowDetails.Column_Rating,myShowDetails[8]);
                    contentValues.put(MyFavoriteContact.FavShowDetails.Column_Overview,myShowDetails[7]);
                    contentValues.put(MyFavoriteContact.FavShowDetails.Column_First_Aired_Date,myShowDetails[3]);
                    Uri uri= getContentResolver().insert(Uri.parse(MyFavoriteContact.FavShowDetails.Content_URI+ ""),contentValues);
                    DataBaseHelper dataBaseHelper=new DataBaseHelper(ShowInfo.this);
                    dataBaseHelper.showFavoriteShows();
                    if (uri!=null) {
                        favImage.setImageResource(R.mipmap.ic_menu_favorite);
                        Toast.makeText(ShowInfo.this, R.string.f, Toast.LENGTH_SHORT).show();
                    }
                    favdisplay=1;
                }
            }
        });
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWiFi= null;
        if (connectivityManager != null) {
            networkInfoWiFi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo networkInfoData=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((networkInfoWiFi!=null & networkInfoData!=null)&&(networkInfoWiFi.isConnected() | networkInfoData.isConnected())) {
                new VideoDetails().execute("videos");
                new ReviewDetails().execute("reviews");
            }else {
                Toast.makeText(this, R.string.no, Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    public class VideoDetails extends AsyncTask<String,Void,Void>{

        ArrayList<VideoJsonData> videoJsonDataArrayList=new ArrayList<>();
        JSONArray jsonArray;

        @Override
        protected Void doInBackground(String... strings) {
            URL url=httpResponse.buildUrl(baseURL,showID,strings[0]);
            String response=null;
            try {
                response=httpResponse.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String id = jsonObject1.getString("id");
                    String key = jsonObject1.getString("key");
                    String name = jsonObject1.getString("name");
                    videoJsonDataArrayList.add(new VideoJsonData(id, key, name));
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (jsonArray.length() != 0) {
                videoRecycleView.setAdapter(new VideoAdapter(ShowInfo.this, videoJsonDataArrayList));
                videoRecycleView.setLayoutManager(new LinearLayoutManager(ShowInfo.this));
            }else {
                noVideo.setText(R.string.noReview);
            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    public class ReviewDetails extends AsyncTask<String,Void,Void>{

        ArrayList<ReviewJsonData> reviewJsonDataArrayList=new ArrayList<>();
        JSONArray jsonArray;
        @Override
        protected Void doInBackground(String... strings) {
            URL url1=httpResponse.buildUrl(baseURL,showID,strings[0]);
            String response=null;
            try {
                response=httpResponse.getResponseFromHttpUrl(url1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject=new JSONObject(response);
                jsonArray=jsonObject.getJSONArray("results");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String author=jsonObject1.getString("author");
                    String content=jsonObject1.getString("content");
                    String url=jsonObject1.getString("url");
                    reviewJsonDataArrayList.add(new ReviewJsonData(author,content,url));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (jsonArray.length()!=0) {
                reviewRecycleView.setLayoutManager(new LinearLayoutManager(ShowInfo.this));
                reviewRecycleView.setAdapter(new ReviewAdapter(ShowInfo.this, reviewJsonDataArrayList));
            }else {
                noReview.setText(s);
            }
        }
    }
}

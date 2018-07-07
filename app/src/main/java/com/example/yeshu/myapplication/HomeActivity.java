package com.example.yeshu.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridLayout;

import com.example.yeshu.myapplication.Adopters.TVShowAdapter;
import com.example.yeshu.myapplication.jsonFiles.TvJsonData;
import com.example.yeshu.myapplication.utils.HttpResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.home_RecycleView) RecyclerView home_recycleView;
    ArrayList<TvJsonData> tvJsonDataArrayList;
    private static final String API_key= BuildConfig.myAPI_key;
    String baseURL="https://api.themoviedb.org/3/tv/popular?api_key="+API_key;
    GridLayout gridLayout;
    GridLayoutManager gridLayoutManager;
    AdView mAdView;
    Cursor cursorData;
    int showID,showName,showOriginalName,showPosterPath,showBackdrop,showFirstAiredDate,showRating,showOverview;
    String bundleValue="myBundleValue";
    String bundleKey="myBundleKey";
    final int LoaderID=11;
    int scrollPosition=0;
    final static String okay="Ok";
    final static String s="No Favorites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        gridLayout=findViewById(R.id.home_GridLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdView=findViewById(R.id.banner);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfoWiFi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo networkInfoData = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((networkInfoWiFi != null & networkInfoData != null) && (networkInfoWiFi.isConnected() | networkInfoData.isConnected()))
            {
                if (savedInstanceState != null){
                    scrollPosition=savedInstanceState.getInt("Scroll");
                    bundleValue=savedInstanceState.getString(bundleKey);
                    //baseURL="https://api.themoviedb.org/3/tv/popular?api_key="+API_key;
                    //new TVDetails().execute();
                    if (savedInstanceState.getString(bundleKey)==getString(R.string.popular)){
                        bundleValue=savedInstanceState.getString(bundleKey);
                        baseURL="https://api.themoviedb.org/3/tv/popular?api_key="+API_key;
                        new TVDetails().execute();
                    }else if (savedInstanceState.getString(bundleKey)==getString(R.string.top_rated)){
                        bundleValue=getString(R.string.top_rated);
                        baseURL="https://api.themoviedb.org/3/tv/top_rated?api_key="+API_key;
                        new TVDetails().execute();
                    }else if (savedInstanceState.getString(bundleKey)==getString(R.string.on_the_air)){
                        bundleValue=getString(R.string.on_the_air);
                        baseURL="https://api.themoviedb.org/3/tv/on_the_air?api_key="+API_key;
                        new TVDetails().execute();
                    }
                    else if (savedInstanceState.getString(bundleKey)==getString(R.string.aired_today)){
                        bundleValue=getString(R.string.aired_today);
                        baseURL="https://api.themoviedb.org/3/tv/airing_today?api_key="+API_key;
                        new TVDetails().execute();
                    }else if (savedInstanceState.getString(bundleKey) == getString(R.string.favorites)) {
                        bundleValue = getString(R.string.favorites);
                        getSupportLoaderManager().restartLoader(LoaderID, null, this);
                    }
                }else {
                    baseURL="https://api.themoviedb.org/3/tv/popular?api_key="+API_key;
                    new TVDetails().execute();
                }
            }
            else {
                new AlertDialog.Builder(HomeActivity.this).setTitle(R.string.app_name).setMessage(R.string.no_internet_see_favourites)
                        .setPositiveButton(okay, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        }
       /* if (savedInstanceState!=null){
            scrollPosition=savedInstanceState.getInt("Scroll");
        }*/
        MobileAds.initialize(this,"ca-app-pub-6336523906622902~6817819328");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("C3528C0925D3AB8424433F9070BD2A5F")
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfoWiFi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo networkInfoData = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((networkInfoWiFi != null & networkInfoData != null) && (networkInfoWiFi.isConnected() | networkInfoData.isConnected())) {
                if (id == R.id.nav_fav) {
                    bundleValue = getString(R.string.favorites);
                    getSupportLoaderManager().restartLoader(LoaderID, null, this);
                } else if (id == R.id.popularity) {
                    bundleValue = getString(R.string.popular);
                    baseURL = "https://api.themoviedb.org/3/tv/popular?api_key=" + API_key;
                    new TVDetails().execute();
                } else if (id == R.id.rating) {
                    bundleValue = getString(R.string.top_rated);
                    baseURL = "https://api.themoviedb.org/3/tv/top_rated?api_key=" + API_key;
                    new TVDetails().execute();
                } else if (id == R.id.on_the_air) {
                    bundleValue = getString(R.string.on_the_air);
                    baseURL = "https://api.themoviedb.org/3/tv/on_the_air?api_key=" + API_key;
                    new TVDetails().execute();
                } else if (id == R.id.aired_today) {
                    bundleValue = getString(R.string.aired_today);
                    baseURL = "https://api.themoviedb.org/3/tv/airing_today?api_key=" + API_key;
                    new TVDetails().execute();
                }
            }
            else {
                if(!(id==R.id.nav_fav)) {
                    new AlertDialog.Builder(HomeActivity.this).setTitle(R.string.app_name).setMessage(R.string.no_internet_see_favourites)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getSupportLoaderManager().restartLoader(LoaderID, null, HomeActivity.this);
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }else {
                    bundleValue = getString(R.string.favorites);
                    getSupportLoaderManager().restartLoader(LoaderID, null, HomeActivity.this);
                }
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (bundleValue==getString(R.string.favorites))
            getSupportLoaderManager().restartLoader(LoaderID,null,this);
        else if (bundleValue==getString(R.string.popular)){
            baseURL="https://api.themoviedb.org/3/tv/popular?api_key="+API_key;
            new TVDetails().execute();
        }else if (bundleValue==getString(R.string.top_rated)){
            baseURL="https://api.themoviedb.org/3/tv/top_rated?api_key="+API_key;
            new TVDetails().execute();
        }else if (bundleValue==getString(R.string.on_the_air)){
            baseURL="https://api.themoviedb.org/3/tv/on_the_air?api_key="+API_key;
            new TVDetails().execute();
        }else if (bundleValue==getString(R.string.aired_today)){
            baseURL="https://api.themoviedb.org/3/tv/airing_today?api_key="+API_key;
            new TVDetails().execute();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(bundleKey, bundleValue);
        if(gridLayoutManager!=null){
            scrollPosition=gridLayoutManager.findFirstVisibleItemPosition();
            outState.putInt("Scroll",scrollPosition);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class TVDetails extends AsyncTask<Void,Void,String>{
        HttpResponse httpResponse=new HttpResponse();
        @Override
        protected String doInBackground(Void... voids) {
            tvJsonDataArrayList=new ArrayList<>();
            URL url= httpResponse.bulid(baseURL);
            String response = null;
            try {
                response=httpResponse.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONArray jsonArray=jsonObject.getJSONArray("results");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject showDetails=jsonArray.getJSONObject(i);
                    int id=showDetails.getInt("id");
                    String name=showDetails.getString("name");
                    String original_name=showDetails.getString("original_name");
                    String first_air_date=showDetails.getString("first_air_date");
                    String backdrop_path=showDetails.getString("backdrop_path");
                    String original_language=showDetails.getString("original_language");
                    String poster_path=showDetails.getString("poster_path");
                    String overview=showDetails.getString("overview");
                    double vote_average=showDetails.getDouble("vote_average");
                    tvJsonDataArrayList.add(new TvJsonData(id,name,original_name,first_air_date,backdrop_path,original_language,poster_path,overview,vote_average));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            gridLayoutManager =new GridLayoutManager(HomeActivity.this,2);
            home_recycleView.setLayoutManager(gridLayoutManager);
            home_recycleView.setAdapter(new TVShowAdapter(HomeActivity.this,tvJsonDataArrayList));
            home_recycleView.scrollToPosition(scrollPosition);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            public Cursor loadInBackground() {
                ContentResolver contentResolver=getContentResolver();
                return contentResolver.query(MyFavoriteContact.FavShowDetails.Content_URI,
                        null,null,null,null);
            }
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        final ArrayList<TvJsonData> tvJsonData=new ArrayList<>();
        cursorData=cursor;
        showID=cursorData.getColumnIndex(MyFavoriteContact.FavShowDetails.Column_ShowID);
        showName=cursorData.getColumnIndex(MyFavoriteContact.FavShowDetails.Column_Name);
        showOriginalName=cursorData.getColumnIndex(MyFavoriteContact.FavShowDetails.Column_OriginalName);
        showPosterPath=cursorData.getColumnIndex(MyFavoriteContact.FavShowDetails.Column_Poster_Path);
        showBackdrop=cursorData.getColumnIndex(MyFavoriteContact.FavShowDetails.Column_Backdrop);
        showRating=cursorData.getColumnIndex(MyFavoriteContact.FavShowDetails.Column_Rating);
        showFirstAiredDate=cursorData.getColumnIndex(MyFavoriteContact.FavShowDetails.Column_First_Aired_Date);
        showOverview=cursorData.getColumnIndex(MyFavoriteContact.FavShowDetails.Column_Overview);
        while (cursor.moveToNext()){
            String sID=cursorData.getString(showID);
            String sName=cursorData.getString(showName);
            String sOriginalName=cursorData.getString(showOriginalName);
            String sPosterPath=cursorData.getString(showPosterPath);
            String sBackdrop=cursorData.getString(showBackdrop);
            String sRating=cursorData.getString(showRating);
            String sFirstAiredDate=cursorData.getString(showFirstAiredDate);
            String sOverview=cursorData.getString(showOverview);
            tvJsonData.add(new TvJsonData(sID,sName,sOriginalName,sPosterPath,sBackdrop,sRating,sFirstAiredDate,sOverview));
        }
        cursorData.close();

        if (tvJsonData.isEmpty()){
            String ok=getString(R.string.ok);
            new AlertDialog.Builder(HomeActivity.this).setTitle(R.string.app_name).setMessage(s)
                    .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }else {
            gridLayoutManager =new GridLayoutManager(HomeActivity.this,2);
            home_recycleView.setLayoutManager(gridLayoutManager);
            home_recycleView.setAdapter(new TVShowAdapter(HomeActivity.this,tvJsonData));
            home_recycleView.scrollToPosition(scrollPosition);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}

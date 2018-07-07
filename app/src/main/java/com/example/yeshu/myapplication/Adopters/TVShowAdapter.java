package com.example.yeshu.myapplication.Adopters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yeshu.myapplication.R;
import com.example.yeshu.myapplication.ShowInfo;
import com.example.yeshu.myapplication.jsonFiles.TvJsonData;
import com.example.yeshu.myapplication.utils.HttpResponse;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.MyTVshowHolder>{

    private Context context;
    private ArrayList<TvJsonData> tvJsonData;
    private String[] showDetails=new String[15];
    private HttpResponse httpResponse=new HttpResponse();

    public TVShowAdapter(Context context, ArrayList<TvJsonData> tvJsonData) {
        this.context = context;
        this.tvJsonData = tvJsonData;
    }


    @NonNull
    @Override
    public TVShowAdapter.MyTVshowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.homeactivity_recycleview,parent,false);
        return new MyTVshowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowAdapter.MyTVshowHolder holder, int position) {
        if (tvJsonData.get(position).getPoster_path() != null) {
            URL url=httpResponse.bulidImgUrl(tvJsonData.get(position).getPoster_path());
            Picasso.with(context).load(url.toString()).into(holder.imageView);
        }else
            Picasso.with(context).load(R.drawable.image_not_available_1).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(tvJsonData==null)
            return 0;
        return tvJsonData.size();
    }

    class MyTVshowHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        MyTVshowHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.home_posterImage);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getLayoutPosition();
                    showDetails[0]= String.valueOf(tvJsonData.get(position).getId());
                    showDetails[1]=tvJsonData.get(position).getName();
                    showDetails[2]=tvJsonData.get(position).getOriginal_name();
                    showDetails[3]=tvJsonData.get(position).getFirst_aired_date();
                    showDetails[4]=tvJsonData.get(position).getBackdrop_path();
                    showDetails[5]=tvJsonData.get(position).getOriginal_language();
                    showDetails[6]=tvJsonData.get(position).getPoster_path();
                    showDetails[7]=tvJsonData.get(position).getOverview();
                    showDetails[8]= String.valueOf(tvJsonData.get(position).getVote_average());

                    Intent intent=new Intent(context, ShowInfo.class);
                    intent.putExtra("details",showDetails);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}

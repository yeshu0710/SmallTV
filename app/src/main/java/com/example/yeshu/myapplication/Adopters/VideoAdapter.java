package com.example.yeshu.myapplication.Adopters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yeshu.myapplication.R;
import com.example.yeshu.myapplication.Youtube;
import com.example.yeshu.myapplication.jsonFiles.VideoJsonData;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyVideoAdapter> {
    private Context context;
    private ArrayList<VideoJsonData> videoJsonDataArrayList;
    final static String noVideos="No Videos";

    public VideoAdapter(Context context, ArrayList<VideoJsonData> videoJsonDataArrayList) {
        this.context = context;
        this.videoJsonDataArrayList = videoJsonDataArrayList;
    }

    @NonNull
    @Override
    public VideoAdapter.MyVideoAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.video_view,parent,false);
        return new MyVideoAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoAdapter.MyVideoAdapter holder, int position) {
        final VideoJsonData videoJsonData=videoJsonDataArrayList.get(position);
        String key=videoJsonData.getKey();
        if (key.equals("")){
            holder.textView.setText(noVideos);
        }else {
            holder.textView.setText(videoJsonData.getName());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, Youtube.class);
                    intent.putExtra("youtube",videoJsonData.getKey());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return videoJsonDataArrayList.size();
    }

    class MyVideoAdapter extends RecyclerView.ViewHolder {
        TextView textView;
        CardView cardView;
        MyVideoAdapter(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.vidoeName);
            cardView=itemView.findViewById(R.id.videoCardView);
        }
    }
}

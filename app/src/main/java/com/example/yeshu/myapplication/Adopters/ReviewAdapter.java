package com.example.yeshu.myapplication.Adopters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yeshu.myapplication.R;
import com.example.yeshu.myapplication.jsonFiles.ReviewJsonData;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyReviewHolder> {
    private Context context;
    private ArrayList<ReviewJsonData> reviewJsonDataArrayList;

    public ReviewAdapter(Context context, ArrayList<ReviewJsonData> reviewJsonDataArrayList) {
        this.context = context;
        this.reviewJsonDataArrayList = reviewJsonDataArrayList;
    }
    @NonNull
    @Override
    public ReviewAdapter.MyReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.review_view,parent,false);
        return new MyReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyReviewHolder holder, int position) {
        final ReviewJsonData reviewJsonData=reviewJsonDataArrayList.get(position);
        holder.textView.setText(String.format("\u2022\tAuthor : \t\t%s\n\u2022\tContent :\n\t\t%s\n\u2022\turl :\t%s", reviewJsonData.getAuthor(), reviewJsonData.getContent(), reviewJsonData.getReviewUrl()));
    }

    @Override
    public int getItemCount() {
        return reviewJsonDataArrayList.size();
    }

    class MyReviewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        MyReviewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.reviewsTextview);
        }
    }
}

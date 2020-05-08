package com.example.groupy.HomeFragments.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groupy.R;
import com.example.groupy.Tools.img;

import java.util.List;

public class PhotosRecyclerView extends RecyclerView.Adapter<PhotosRecyclerView.ViewHolder> {

    Context mContext;
    List<img> mData;


    public PhotosRecyclerView(Context mContext, List<img> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.staggeredphoto,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //get the image in list
 
        Glide.with(mContext).load(mData.get(position).getImage()).into(holder.photos);



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView photos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photos =itemView.findViewById(R.id.staggeredphotos);
        }
    }
}

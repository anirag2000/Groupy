package com.example.groupy.Home;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groupy.R;
import com.example.groupy.calling.Apps;

import java.util.ArrayList;

import xyz.schwaab.avvylib.AvatarView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    int index;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> uids = new ArrayList<>();
    private Context mContext;
    Main main_context;

    public RecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls,ArrayList<String>uid, Main main) {
        mNames = names;
        mImageUrls = imageUrls;
        mContext = context;
        this.uids=uid;
        this.main_context=main;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext).load(mImageUrls.get(position))
                .into(holder.image);
String [] names=mNames.get(position).split(" ");
        holder.name.setText(names[0]);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
index=position;
notifyDataSetChanged();


            }
        });
        if(index==position){
            holder.image.setBorderColor(R.drawable.red);
            holder.image.setBorderThickness(15);
            holder.image.setAnimating(true);
           holder.image.setHighlightBorderColor(Color.GREEN);
            holder.image.setHighlightBorderColorEnd(Color.CYAN);holder.image.setNumberOfArches(10);
            holder.image.setTotalArchesDegreeArea(360);

            Apps.position=uids.get(position);

       main_context.setAdapter();
        }
        else
        {

            holder.image.setBorderThickness(-1);
        }
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        AvatarView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }
}
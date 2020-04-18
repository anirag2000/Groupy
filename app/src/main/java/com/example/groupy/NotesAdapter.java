package com.example.groupy;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import android.graphics.Color;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> description = new ArrayList<>();
    private Context mContext;

    public NotesAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls) {
        title = names;
        description = imageUrls;
        mContext = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notes_layout, viewGroup, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {

        Random rnd = new Random();
        int k=rnd.nextInt(4)+1;
        switch (k)
        {
            case 1:
                viewHolder.parent.setBackgroundResource(R.drawable.notes_layout);
                break;
            case 2:
                viewHolder.parent.setBackgroundResource(R.drawable.notes_layout1);
                break;
            case 3:
                viewHolder.parent.setBackgroundResource(R.drawable.notes_layout2);
                break;
            case 4:
                viewHolder.parent.setBackgroundResource(R.drawable.notes_layout4);
                break;
            default:
                viewHolder.parent.setBackgroundResource(R.drawable.notes_layout5);
                break;


        }

        viewHolder.title_tv.setText(title.get(i));
        viewHolder.description_tv.setText(description.get(i));


    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv,description_tv;
       ConstraintLayout parent;
        public MyViewHolder(View itemView) {
            super(itemView);
            parent=itemView.findViewById(R.id.parent);
            title_tv=itemView.findViewById(R.id.name);
            description_tv=itemView.findViewById(R.id.age);
        }
    }
}

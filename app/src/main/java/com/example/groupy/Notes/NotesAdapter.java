package com.example.groupy.Notes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.groupy.R;

import java.util.ArrayList;
import java.util.Random;

//Notes Recycler

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> description = new ArrayList<>();
    private ArrayList<String> user = new ArrayList<>();
    private Context mContext;

    public NotesAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls,ArrayList<String> users) {
        title = names;
        description = imageUrls;
        user=users;
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
                viewHolder.title_tv.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.description_tv.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.user_tv.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case 3:
                viewHolder.parent.setBackgroundResource(R.drawable.notes_layout2);
                break;
            case 4:
                viewHolder.parent.setBackgroundResource(R.drawable.notes_layout4);
                viewHolder.title_tv.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.description_tv.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.user_tv.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            default:
                viewHolder.parent.setBackgroundResource(R.drawable.notes_layout5);
                break;


        }

        viewHolder.title_tv.setText(title.get(i));
        viewHolder.description_tv.setText(description.get(i));
        viewHolder.user_tv.setText((user.get(i)));


    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv,description_tv,user_tv;
       ConstraintLayout parent;
        public MyViewHolder(View itemView) {
            super(itemView);
            parent=itemView.findViewById(R.id.parent);
            title_tv=itemView.findViewById(R.id.name);
            description_tv=itemView.findViewById(R.id.age);
            user_tv=itemView.findViewById(R.id.user);
        }
    }
}

package com.example.groupy;


import android.content.Context;

import android.graphics.Color;
import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groupy.Home.RecyclerViewAdapter;
import com.example.groupy.Messenger.MessagingActivity;
import com.example.groupy.calling.Apps;

import java.util.ArrayList;

        import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 2/12/2018.
 */

public class ColourAdapter extends RecyclerView.Adapter<ColourAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars

    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    public ColourAdapter(Context context, ArrayList<String> imageUrls) {

        mImageUrls = imageUrls;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
if(mImageUrls.get(position).equals("A"))
{
 holder.image.setImageResource(R.drawable.doodle);
}
else if(mImageUrls.get(position).equals("B")){
    holder.image.setImageResource(R.drawable.text);
}
else {
    holder.image.setColorFilter(Color.parseColor(mImageUrls.get(position)));
}
     holder.image.setBackgroundColor(Color.parseColor("#ECECEC"));



        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mImageUrls.get(position).length()>3) {
                    Apps.colour = mImageUrls.get(position);
                    ((MessagingActivity) mContext).change_colour();
                }
                else
                {
                    if(mImageUrls.get(position).equals("A")){
                        ((MessagingActivity) mContext).open_draw();
                    }
                    else if(mImageUrls.get(position).equals("B")){
                    holder.image.setImageResource(R.drawable.italic);
                        ((MessagingActivity) mContext).change_style();
                    }
                }
        }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);

        }
    }
}
package com.example.groupy.Messenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.groupy.R;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewholder> {


    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private Context mContext;
    private List<Chat> texts;
    private String imageurl;

    FirebaseUser firebaseUser;

    public ChatAdapter(Context mContext,List<Chat> texts,String imageurl){
        this.mContext=mContext;
        this.texts=texts;
        this.imageurl=imageurl;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapter.viewholder(view);
        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapter.viewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        final Chat chat = texts.get(position);

        holder.textmessage.setText(chat.getMessage());

        //doubt???? where is the imageurl coming from ?? possibly the constructor

        if(imageurl.equals("default")){
            holder.image.setImageResource(R.drawable.ic_launcher_foreground);
        }
        else {
            Glide.with(mContext).load(imageurl).into(holder.image);
        }

    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        public CircleImageView image;
        public TextView textmessage;

        public viewholder(View itemView) {
            super(itemView);
            textmessage = itemView.findViewById(R.id.textmessage);
            image = itemView.findViewById(R.id.circleimage);
        }

    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(texts.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }


    }
}

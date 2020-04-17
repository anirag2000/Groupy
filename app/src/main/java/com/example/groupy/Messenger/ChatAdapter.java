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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewholder> {


    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private Context mContext;
    private List<Chat> texts;
    private String imageurl;
    private String currentuser;

    FirebaseUser firebaseUser;

    public ChatAdapter(Context mContext,List<Chat> texts,String imageurl,String currentuser){
        this.mContext=mContext;
        this.texts=texts;
        this.imageurl=imageurl;
        this.currentuser=currentuser;
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

      DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(currentuser);
      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              String currentuserphoto=dataSnapshot.child("photourl").getValue(String.class);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });


        int type=getItemViewType(position);

        if(type==MSG_TYPE_RIGHT){
            Glide.with(mContext).load(currentuser).into(holder.rimage);
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
        public CircleImageView rimage;
        public TextView textmessage;

        public viewholder(View itemView) {
            super(itemView);
            textmessage = itemView.findViewById(R.id.textmessage);
            image = itemView.findViewById(R.id.circleimage);
            rimage=itemView.findViewById(R.id.rimage);
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

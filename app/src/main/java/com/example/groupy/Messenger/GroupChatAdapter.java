package com.example.groupy.Messenger;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groupy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    String currentuserphoto;
    Context mContext;
    List<GroupChat> texts;
    String imageurl;
    String currentuser;

    FirebaseUser firebaseUser;

    public GroupChatAdapter(Context mContext, List<GroupChat> texts, String imageurl, String currentuser) {
        this.mContext = mContext;
        this.texts = texts;
        this.imageurl = imageurl;
        this.currentuser = currentuser;
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new GroupChatAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new GroupChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final GroupChat chat = texts.get(position);

        holder.textmessage.setText(chat.getMessage().trim());

        String time_unformatted = chat.getDate().split(" ")[1];
        String time_formatted[] = time_unformatted.split(":");
        holder.date.setText(time_formatted[0] + ":" + time_formatted[1]);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentuser);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentuserphoto = dataSnapshot.child("photourl").getValue(String.class);


                int type = getItemViewType(position);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("DP").child(currentuser);

                Log.e("This is right image", currentuserphoto);

                if (type == MSG_TYPE_RIGHT && MessagingActivity.right == 0) {
                    MessagingActivity.right = MessagingActivity.right + 1;
                    MessagingActivity.left = 0;
                    //Glide.with(mContext).load(currentuserphoto).into(holder.rimage);

                } else if (type == MSG_TYPE_LEFT && MessagingActivity.left == 0) {

                    MessagingActivity.left = MessagingActivity.left + 1;
                    MessagingActivity.right = 0;


                    Glide.with(mContext).load(imageurl).into(holder.image);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.date.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.date.setVisibility(View.GONE);
                    }
                }, 1000);


            }
        });



    }

    @Override
    public int getItemCount() {
        return texts.size();
    }
    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (texts.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView image;
        public CircleImageView rimage;
        public TextView textmessage;
        public TextView date;
        

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textmessage = itemView.findViewById(R.id.textmessage);
            image = itemView.findViewById(R.id.circleimage);
            //image=itemView.findViewById(R.id.rimage);
            date = itemView.findViewById(R.id.date);
            date.setVisibility(View.GONE);



        }
    }
}

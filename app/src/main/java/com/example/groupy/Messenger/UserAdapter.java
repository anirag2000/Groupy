package com.example.groupy.Messenger;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groupy.R;

import com.bumptech.glide.Glide;
import com.example.groupy.User_details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{


    private Context mContext;
    private List<User_details> mUsers;
    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

    public UserAdapter(Context mContext,List<User_details> mUsers ){
        this.mContext=mContext;
        this.mUsers=mUsers;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User_details user = mUsers.get(position);
        holder.username.setText(user.getName());
        if(user.getPhotourl().equals("default")){
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(mContext).load(user.getPhotourl()).into(holder.image);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, MessagingActivity.class);
                Pair[] pairs=new Pair[2];

                pairs[0]=new Pair<View,String>(holder.image,"imageTransition");
                pairs[1]=new Pair<View,String>(holder.username,"textTransition");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,pairs);

                intent.putExtra("userid",user.getUid());
                mContext.startActivity(intent,options.toBundle());


            }
        });
    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView image;
        public TextView username;
        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username);
            image=itemView.findViewById(R.id.rightimage);
        }

    }
}

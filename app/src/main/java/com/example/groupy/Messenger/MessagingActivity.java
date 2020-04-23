package com.example.groupy.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.groupy.Home;
import com.example.groupy.Service.MyFirebaseIdService;
import com.example.groupy.Service.Token;
import com.example.groupy.User_details;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.groupy.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessagingActivity extends AppCompatActivity {
    static int left;
    static int right;
    CircleImageView rimage;
    CircleImageView image;
    TextView text;
    Intent intent;
    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DatabaseReference temp;
    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

    ImageButton send;
    EditText message;
    String token;

    ChatAdapter messageAdapter;
    RecyclerView recyclerView;
    List<Chat> texts = new ArrayList<>();

    public void updateToken(String token){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token obj = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(obj);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        left = 0;
        right = 0;


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MessagingActivity.this, instanceIdResult -> {
            token = instanceIdResult.getToken();
            Log.e("This is the token",token);
            updateToken(token);
        });





        //initialize all the components on screen
        //rimage=findViewById(R.id.rimage);
        text = findViewById(R.id.username);
        send = findViewById(R.id.send);
        message = findViewById(R.id.typed_message);
        //image=findViewById(R.id.circleimage);


        //whats the receiver's details for the page load
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();







        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        //loading the page
        final String userid = intent.getStringExtra("userid");
        reference = database.getReference("Users");
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_details user = dataSnapshot.child(userid).getValue(User_details.class);
                //User_details user = dataSnapshot.child(currentuser).getValue(User_details.class);
                //  Log.e("this is walter",userid);
                // Log.e("this is the me",ruser.getName());

                //for the receiver
                image = findViewById(R.id.circleimage);
                text.setText(user.getName());

                Glide.with(MessagingActivity.this).load(user.getPhotourl()).into(image);


                //for the sender
                // Log.e("this is the photourl",user.getPhotourl());
                //Glide.with(MessagingActivity.this).load(user.getPhotourl()).into(rimage);

                messageAdapter = new ChatAdapter(MessagingActivity.this, texts, user.getPhotourl(), currentuser);
                recyclerView.setAdapter(messageAdapter);

                readmessages(firebaseUser.getUid(), userid, user.getPhotourl(), currentuser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        reference.child("AddDetails").child(currentuser).child(userid).child("Typing").setValue("0");
        reference.child("AddDetails").child(currentuser).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Typing").getValue(String.class).equals("1"))
                {
                    TextView typing=findViewById(R.id.typing);
                    typing.setVisibility(View.VISIBLE);
                }
                if(dataSnapshot.child("Typing").getValue(String.class).equals("0"))
                {
                    TextView typing=findViewById(R.id.typing);
                    typing.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        LinearLayout linearLayout=findViewById(R.id.linearLayout2);
        linearLayout.setBackgroundResource(R.drawable.red);
         reference=FirebaseDatabase.getInstance().getReference().child("online_statuses");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userid))
                {
                    if(dataSnapshot.child(userid).getValue(String.class).equals("online"))
                    {
                        linearLayout.setBackgroundResource(R.drawable.green);
                    }
                    else
                    {
                        linearLayout.setBackgroundResource(R.drawable.red);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        KeyboardVisibilityEvent.setEventListener(
               this,
                (KeyboardVisibilityEventListener) isOpen -> {
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());


                });


DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
message.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        databaseReference.child("AddDetails").child(userid).child(currentuser).child("Typing").setValue("1");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                databaseReference.child("AddDetails").child(userid).child(currentuser).child("Typing").setValue("0");
            }
        }, 4000);

    }

    @Override
    public void afterTextChanged(Editable s) {



    }
});
        //send the message
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedmessage = message.getText().toString();
                if (!typedmessage.isEmpty()) {
                    sendmessage(userid, firebaseUser.getUid(), typedmessage);

                    //keyboard closing after send

                } else {
                    Toast.makeText(MessagingActivity.this, "Oops, you can't send empty messages!", Toast.LENGTH_SHORT).show();
                }
                message.setText("");

            }
        });


    }


    void sendmessage(String reciever, String from, String message) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String date_string = dateFormat.format(date); //2016/11/16 12:08:43
        reference = database.getReference();
        message.trim();
        if (message.trim().isEmpty()) {
            return;
        }


        Chat chat = new Chat(reciever, from, message, date_string);

        reference.child("Chats").push().setValue(chat);
    }

    private void readmessages(final String sender, final String receiver, final String imageurl, String currentuser) {
        recyclerView.setAdapter(messageAdapter);
        reference = database.getReference("Chats");
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);


                //if the receiver and sender are the same people we are talking to
                Log.e("sample message is ", "im here");

                if (chat.getSender().equals(sender) && chat.getReciever().equals(receiver)
                        || chat.getSender().equals(receiver) && chat.getReciever().equals(sender)) {
                    texts.add(chat);
                   //
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());






                }



                //The RecyclerView is a new ViewGroup that is prepared to render any adapter-based view in a similar way.
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }
        @Override
    public void onBackPressed()
        {
            super.onBackPressed();
            supportFinishAfterTransition();
            ActivityCompat.postponeEnterTransition(this);
            ActivityCompat.startPostponedEnterTransition(MessagingActivity.this);
        }


    }




class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewholder> {


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    String currentuserphoto;
    private Context mContext;
    private List<Chat> texts;
    private String imageurl;
    private String currentuser;

    FirebaseUser firebaseUser;


    public ChatAdapter(Context mContext, List<Chat> texts, String imageurl, String currentuser) {
        this.mContext = mContext;
        this.texts = texts;
        this.imageurl = imageurl;
        this.currentuser = currentuser;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapter.viewholder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapter.viewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        final Chat chat = texts.get(position);

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

    public class viewholder extends RecyclerView.ViewHolder {
        public CircleImageView image;
        public CircleImageView rimage;
        public TextView textmessage;
        public TextView date;

        public viewholder(View itemView) {
            super(itemView);
            textmessage = itemView.findViewById(R.id.textmessage);
            image = itemView.findViewById(R.id.circleimage);
            //image=itemView.findViewById(R.id.rimage);
            date = itemView.findViewById(R.id.date);
            date.setVisibility(View.GONE);

        }

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
}




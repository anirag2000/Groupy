package com.example.groupy.Messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.groupy.R;
import com.example.groupy.User_details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {
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
    FirebaseUser firebaseUser;

    String currentuserphoto;
    String currentusername;

    String mygroup;

    ImageButton send;
    EditText message;


    GroupChatAdapter messageAdapter;
    RecyclerView recyclerView;
    List<GroupChat> texts = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference checker = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("group_id");
        checker.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mygroup=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        left = 0;
        right = 0;

        //initialise the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
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

        //loading the messages

        recyclerView = findViewById(R.id.grecyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //loading the page
        final String userid = intent.getStringExtra("userid");
        reference = database.getReference("Users");
        //String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

                Glide.with(getApplicationContext()).load(user.getPhotourl()).into(image);


                //for the sender
                // Log.e("this is the photourl",user.getPhotourl());
                //Glide.with(MessagingActivity.this).load(user.getPhotourl()).into(rimage);


                readmessages(firebaseUser.getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference temp=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentuserphoto=dataSnapshot.child("photourl").getValue(String.class);
                currentusername=dataSnapshot.child("name").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String typedmessage = message.getText().toString();
                        if (!typedmessage.isEmpty()) {
                            sendmessage(userid, firebaseUser.getUid(), typedmessage);

                            //keyboard closing after send
                            //InputMethodManager inputManager = (InputMethodManager) MessagingActivity.this.getSystemService(MessagingActivity.this.INPUT_METHOD_SERVICE);
                            //inputManager.hideSoftInputFromWindow(MessagingActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        } else {
                            Toast.makeText(getApplicationContext(), "Oops, you can't send empty messages!", Toast.LENGTH_SHORT).show();
                        }
                        message.setText("");

                    }
                });

            }
        }, 1000);


        //send the message



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


        GroupChat sendingmessage=new GroupChat();
        //GroupChat sendingmessage = new GroupChat(mygroup, from,currentuserphoto,message,currentusername,date_string);


        sendingmessage.setGroup(mygroup);
        sendingmessage.setSenderphoto(currentuserphoto);
        sendingmessage.setMessage(message);
        sendingmessage.setDate(date_string);
        sendingmessage.setSender(from);
        sendingmessage.setSentByName(currentusername);

        reference.child("GroupChat").push().setValue(sendingmessage);

    }

    private void readmessages(String currentuser) {
        reference = database.getReference("GroupChat");
       // texts.clear();

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                texts.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){



                        GroupChat alltexts = snapshot.getValue(GroupChat.class);
                       // imageurl=snapshot.child("senderphoto").getValue(String.class);



                        if(alltexts.getGroup().equals(mygroup)) {
                            texts.add(alltexts);


                        }



                }
                messageAdapter = new GroupChatAdapter(GroupChatActivity.this, texts, "example", currentuser);
                recyclerView.setAdapter(messageAdapter);
             //   texts.clear();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });








    }









}

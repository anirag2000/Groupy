package com.example.groupy.Messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import  androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.groupy.User_details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.groupy.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessagingActivity extends AppCompatActivity {

    CircleImageView rimage;
    CircleImageView image;
    TextView text;
    Intent intent;
    FirebaseAuth auth;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DatabaseReference temp;
    FirebaseUser firebaseUser;

    ImageButton send;
    EditText message;



    ChatAdapter messageAdapter;
    RecyclerView recyclerView;
    List<Chat> texts= new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        //initialise the toolbar
        Toolbar toolbar=findViewById(R.id.toolbar);
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
        rimage=findViewById(R.id.rimage);
        text=findViewById(R.id.username);
        send=findViewById(R.id.send);
        message=findViewById(R.id.typed_message);
        image=findViewById(R.id.circleimage);


        //whats the receiver's details for the page load
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        intent=getIntent();

        //loading the messages

        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=  new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //loading the page
        final String userid=intent.getStringExtra("userid");
        reference=database.getReference("Users");
        String currentuser=FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_details user = dataSnapshot.child(userid).getValue(User_details.class);
                //User_details user = dataSnapshot.child(currentuser).getValue(User_details.class);
              //  Log.e("this is walter",userid);
               // Log.e("this is the me",ruser.getName());

                //for the receiver
                text.setText(user.getName());

                Glide.with(MessagingActivity.this).load(user.getPhotourl()).into(image);


                //for the sender
                   // Log.e("this is the photourl",user.getPhotourl());
                    //Glide.with(MessagingActivity.this).load(user.getPhotourl()).into(rimage);


                readmessages(firebaseUser.getUid(),userid,user.getPhotourl(),currentuser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        //send the message
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedmessage=message.getText().toString();
                if(!message.equals("")){
                    sendmessage(userid,firebaseUser.getUid(),typedmessage);

                    //keyboard closing after send
                    InputMethodManager inputManager = (InputMethodManager) MessagingActivity.this.getSystemService(MessagingActivity.this.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(MessagingActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                else{
                    Toast.makeText(MessagingActivity.this,"Oops, you can't send empty messages!",Toast.LENGTH_SHORT).show();
                }
                message.setText("");

            }
        });





    }



    void sendmessage(String reciever, String from, String message){

        reference=database.getReference();

        HashMap<String,Object>hashMap= new HashMap<>();
        hashMap.put("reciever",reciever);
        hashMap.put("sender",from);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readmessages(final String sender, final String receiver, final String imageurl,String currentuser){
        reference=database.getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                texts.clear();
                //texts= new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    //if the receiver and sender are the same people we are talking to
                    Log.e("sample message is ","im here");
                    if(chat.getSender().equals(sender) && chat.getReciever().equals(receiver)
                    || chat.getSender().equals(receiver) && chat.getReciever().equals(sender)){
                       texts.add(chat);
                    }
                    //The RecyclerView is a new ViewGroup that is prepared to render any adapter-based view in a similar way.

                }

                messageAdapter=new ChatAdapter(MessagingActivity.this,texts,imageurl,currentuser);
                recyclerView.setAdapter(messageAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}



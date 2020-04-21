package com.example.groupy;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groupy.Messenger.GroupChatActivity;
import com.example.groupy.Messenger.MessagingActivity;
import com.example.groupy.Messenger.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dms extends Fragment { //Nothing But displaying all users


    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User_details> mUsers= new ArrayList<>();
    ConstraintLayout groupchat;
    String group_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_dms, container, false);


        groupchat=view.findViewById(R.id.groupchat);
        FirebaseUser firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
        String user=firebaseUser.getUid();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(user);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                group_id=dataSnapshot.child("group_id").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       // groupchat=view.findViewById(R.id.groupchat);
        groupchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go= new Intent(getContext(), GroupChatActivity.class);
                go.putExtra("userid",group_id);

                startActivity(go);
            }
        });

        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));



        readUsers();


        return view;
    }

    void readUsers(){

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference;

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        reference=database.getReference("Users");



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentuser=firebaseUser.getUid();
                String groupcode= dataSnapshot.child(currentuser).child("group_id").getValue(String.class);
                mUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    User_details user = snapshot.getValue(User_details.class);
                    assert user!=null;
                    assert firebaseUser!=null;
                    if(user.getGroup_id().equals(groupcode)){
                        if(!user.getUid().equals(firebaseUser.getUid())) {
                            mUsers.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
package com.example.groupy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    List<User_details> mUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dms, container, false);

        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers= new ArrayList<>();

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
package com.example.groupy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Main extends Fragment {

    String uid;

    String group_id;
    RecyclerView recyclerView;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();

        uid=currentuser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                group_id=snapshot.child("group_id").getValue(String.class);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(layoutManager);
                getInfo();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });



        ImageButton messages=view.findViewById(R.id.messages);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Home)getActivity()).selectIndex(2);
            }
        });


    }

    private void getInfo() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("group").child("group_code").child(group_id).child("ids").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mNames.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Toast.makeText(Home.this,"this"+postSnapshot.getValue(String.class),Toast.LENGTH_LONG).show();

                    mNames.add(postSnapshot.child("name").getValue(String.class));
                    mImageUrls.add(postSnapshot.child("photourl").getValue(String.class));
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


        // initRecyclerView();

    }





}

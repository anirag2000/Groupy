package com.example.groupy.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.groupy.EditDetails;
import com.example.groupy.Notes.NotesMain;
import com.example.groupy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


//Horizontal Recycler View




public class Main extends Fragment {

    public String uid;

    public String group_id;
    public RecyclerView recyclerView;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    AlertDialog dialog;


    public Main() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Button button3=view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), EditDetails.class);
                intent.putExtra("group_code",group_id);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });





        Button button2=view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String muid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(muid).child("group_id");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String group=dataSnapshot.getValue(String.class);
                        Toast.makeText(getContext(),group,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });




        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
       dialog= builder.create();
        FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();

        uid=currentuser.getUid();
        Button button=view.findViewById(R.id.notes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), NotesMain.class);
                intent.putExtra("group_code",group_id);
                startActivity(intent);
            }
        });

        Log.w("about to  workkkk","");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentFirebaseUser!= null) {
            uid=currentFirebaseUser.getUid();
            Log.w("workkkk",uid);
        } else {
//            Intent intent = new Intent(getContext(), MainActivity.class);
//            startActivity(intent);
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        dialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.hide();;
//
//            }
//        },2000);
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
        super.onViewCreated(view, savedInstanceState);

    }

    private void getInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("group").child("group_code").child(group_id).child("ids").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mNames.clear();
                mImageUrls.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Toast.makeText(Home.this,"this"+postSnapshot.getValue(String.class),Toast.LENGTH_LONG).show();

                    String uid=(postSnapshot.getKey());
                    if(uid.length()>7) {
                        mNames.add(postSnapshot.child("name").getValue(String.class));
                        mImageUrls.add(postSnapshot.child("photourl").getValue(String.class));
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        // initRecyclerView();
    }
    public void getuid(View view )
    {



    }






}

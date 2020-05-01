package com.example.groupy.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.groupy.EditDetails;
import com.example.groupy.HomeFragments.FragmentAdapter;
import com.example.groupy.Notes.NotesMain;
import com.example.groupy.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


//Horizontal Recycler View




public class Main extends Fragment  {

    public String uid;
    ViewPager viewPager;
    public String group_id;
    public RecyclerView recyclerView;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String >uids=new ArrayList<>();
    AlertDialog dialog;
TabLayout tabLayout;

    public Main() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

       viewPager =view.findViewById(R.id.viewPager);
        FragmentAdapter fragmentAdapter=new FragmentAdapter(getActivity(),getChildFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        fragmentAdapter.notifyDataSetChanged();
        tabLayout=view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.profile_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.notes);
        tabLayout.getTabAt(2).setIcon(R.drawable.loaction);
        tabLayout.getTabAt(3).setIcon(R.drawable.photos);
        tabLayout.getTabAt(4).setIcon(R.drawable.documents );

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



/////for toasting group id//////////////////

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
        /////for toasting group id//////////////////




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

        ref.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

group_id=snapshot.child("group_id").getValue(String.class);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(layoutManager);
                mNames.add("All");
                mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/groupy-8f7ec.appspot.com/o/388-3882701_distributor-login-orange-group-icon-clipart.png?alt=media&token=61aa7c97-f16a-41ec-b1d8-82e749b43129");
                uids.add("0");
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
                uids.clear();
                mNames.add("All");
                uids.add("0");
                mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/groupy-8f7ec.appspot.com/o/388-3882701_distributor-login-orange-group-icon-clipart.png?alt=media&token=61aa7c97-f16a-41ec-b1d8-82e749b43129");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Toast.makeText(Home.this,"this"+postSnapshot.getValue(String.class),Toast.LENGTH_LONG).show();

                    String uid=(postSnapshot.getKey());
                    if(uid.length()>7) {
                        uids.add(postSnapshot.child("uid").getValue(String.class));
                        mNames.add(postSnapshot.child("name").getValue(String.class));
                        mImageUrls.add(postSnapshot.child("photourl").getValue(String.class));
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls,uids, Main.this);
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



    void setAdapter() {
        int position=viewPager.getCurrentItem();
        FragmentAdapter pagerAdapter = new FragmentAdapter(getActivity(),getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        // when notify then set manually current position.v

        viewPager.setCurrentItem(position);
        pagerAdapter.notifyDataSetChanged();
        tabLayout.getTabAt(0).setIcon(R.drawable.profile_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.notes);
        tabLayout.getTabAt(2).setIcon(R.drawable.loaction);
        tabLayout.getTabAt(3).setIcon(R.drawable.photos);
        tabLayout.getTabAt(4).setIcon(R.drawable.documents );
    }




}

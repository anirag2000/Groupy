package com.example.groupy.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.groupy.FusedLocation;
import com.example.groupy.HomeFragments.FragmentAdapter;
import com.example.groupy.R;
import com.example.groupy.SigningIn.MainActivity;
import com.example.groupy.calling.Apps;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


//Horizontal Recycler View


public class Main extends Fragment {

    public String uid;
    ViewPager viewPager;
    public String group_id;
    public RecyclerView recyclerView;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> uids_list = new ArrayList<>();
    AlertDialog dialog;


    Double lat;
    Double lon;


    TabLayout tabLayout;

    public Main() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        //adding the person's recent location
        FusedLocation fusedLocation = new FusedLocation(getContext(), location -> {
            //Do as you wish with location here
            lat = location.getLatitude();
            lon = location.getLongitude();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RecentLocation");
            reference.child(Apps.uid).child("latitude").setValue(lat);
            reference.child(Apps.uid).child("longitude").setValue(lon);
            Toast.makeText(getContext(),"Location is updated",Toast.LENGTH_SHORT).show();
        });
        //get the location now
        //Toast.makeText(getContext(),"Location is updated",Toast.LENGTH_SHORT).show();

        fusedLocation.getCurrentLocation(3); // 3 times for accuracy


        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        viewPager = view.findViewById(R.id.viewPager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity(), getChildFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        fragmentAdapter.notifyDataSetChanged();
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.profile_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.notes);
        tabLayout.getTabAt(2).setIcon(R.drawable.loaction);
        tabLayout.getTabAt(3).setIcon(R.drawable.photos);
        tabLayout.getTabAt(4).setIcon(R.drawable.documents);










        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();



        ///for notes/////////////////////////////////////////
//        Button button = view.findViewById(R.id.notes);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), NotesMain.class);
//                intent.putExtra("group_code", group_id);
//                startActivity(intent);
//            }
//        });


        ////if user id is empty go to mainactivity for registration


        if (Apps.uid.trim().isEmpty()) {
            Intent intent=new Intent(getContext(), MainActivity.class);
            startActivity(intent);

        }
        else
        {
            uid=Apps.uid;
        }
        //////////
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                group_id = snapshot.child("group_id").getValue(String.class);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(layoutManager);
                mNames.add("All");
                mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/groupy-8f7ec.appspot.com/o/388-3882701_distributor-login-orange-group-icon-clipart.png?alt=media&token=590055a5-4646-4fd5-930c-bfbd095b1121");
                uids_list.add("0");
                getInfo();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


//////////for sliding into dms xD////////////////////
        ImageButton messages = view.findViewById(R.id.messages);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Home) getActivity()).selectIndex(2);
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
                uids_list.clear();
                mNames.add("All");
                uids_list.add("0");
                mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/groupy-8f7ec.appspot.com/o/388-3882701_distributor-login-orange-group-icon-clipart.png?alt=media&token=590055a5-4646-4fd5-930c-bfbd095b1121");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Toast.makeText(Home.this,"this"+postSnapshot.getValue(String.class),Toast.LENGTH_LONG).show();

                    String current_uid = (postSnapshot.getKey());
                    if (current_uid.length() > 7) {
                        uids_list.add(postSnapshot.child("uid").getValue(String.class));
                        mNames.add(postSnapshot.child("name").getValue(String.class));
                        mImageUrls.add(postSnapshot.child("photourl").getValue(String.class));
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls, uids_list, Main.this);
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
        int position = viewPager.getCurrentItem();
        FragmentAdapter pagerAdapter = new FragmentAdapter(getActivity(), getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        // when notify then set manually current position.v


        viewPager.setCurrentItem(position);
        pagerAdapter.notifyDataSetChanged();
        tabLayout.getTabAt(0).setIcon(R.drawable.profile_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.notes);
        tabLayout.getTabAt(2).setIcon(R.drawable.loaction);
        tabLayout.getTabAt(3).setIcon(R.drawable.photos);
        tabLayout.getTabAt(4).setIcon(R.drawable.documents);
    }


}

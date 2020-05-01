package com.example.groupy.HomeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.groupy.FusedLocation;
import com.example.groupy.R;
import com.example.groupy.UserLocation;
import com.example.groupy.calling.Apps;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class Location extends Fragment implements OnMapReadyCallback {

    TextView textView;
    LatLng sydney;
    private GoogleMap mMap;
    Double lat;
    Double lon;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RecentLocation");
    FirebaseUser currentuser= FirebaseAuth.getInstance().getCurrentUser();
    String user;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //textView.setText("HELLLO,  Location\n"+ Apps.position);

        if(!Apps.position.equals('0')) {

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }





    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }




        return inflater.inflate(R.layout.fragment_location, container, false);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera



        user=Apps.position;


//
//        FusedLocation fusedLocation = new FusedLocation(getContext(), location -> {
//            //Do as you wish with location here
//            lat=location.getLatitude();
//            lon=location.getLongitude();
//            Log.e("seting location",lon.toString()+lat.toString());
//            LatLng sydney = new LatLng(lat, lon);
//            reference.child(user).child("latitude").setValue(lat);
//            reference.child(currentuser.getUid()).child("longitude").setValue(lon);
//
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        });
//        fusedLocation.getCurrentLocation(3);



        reference.child(Apps.position).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lat=dataSnapshot.child("latitude").getValue(Double.class);
                lon=dataSnapshot.child("longitude").getValue(Double.class);
                sydney= new LatLng(lat, lon);
                //Toast.makeText(getContext(),lat.toString()+lon.toString(),Toast.LENGTH_LONG).show();
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                Log.e("the pointer","is here");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                Log.e("the pointer","is here");


            }
        }, 3000);



    }




}




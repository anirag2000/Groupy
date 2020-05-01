package com.example.groupy.HomeFragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.groupy.R;
import com.example.groupy.calling.Apps;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Profile extends Fragment {
    String group_id;
    String uid;
    String name;
    String email;
    String photourl;
    Uri downloadUrl;
    String final_uri;
    ImageView photo;
    StorageReference mStorageRef;
    Dialog dialog;

    public Profile() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(!Apps.position.equals("0")) {
            try {
                mStorageRef = FirebaseStorage.getInstance().getReference();
                // Toast.makeText(EditDetails.this,uid,Toast.LENGTH_LONG).show();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(isAdded()) {
                            name = dataSnapshot.child(Apps.position).child("name").getValue(String.class);
                            //Toast.makeText(EditDetails.this,name,Toast.LENGTH_LONG).show();
                            email = dataSnapshot.child(Apps.position).child("email").getValue(String.class);
                            photourl = dataSnapshot.child(Apps.position).child("photourl").getValue(String.class);
                            TextView name_tv = view.findViewById(R.id.textView8);
                            TextView email_tv = view.findViewById(R.id.textView12);
                            name_tv.setText(name);
                            email_tv.setText(email);
                            ImageView photo = view.findViewById(R.id.imageView3);
                            Glide.with(getContext()).load(photourl).into(photo);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            catch (Exception e)
            {
                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}

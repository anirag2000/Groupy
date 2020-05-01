package com.example.groupy.HomeFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.groupy.R;
import com.example.groupy.calling.Apps;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class Profile extends Fragment {
    String group_id;
    String uid;
    String uidd;
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
        ImageView photo = view.findViewById(R.id.imageView3);
        TextView name_tv = view.findViewById(R.id.textView8);
        TextView email_tv = view.findViewById(R.id.textView12);
        if(!Apps.position.equals("0")) {

            if(uidd.equals(Apps.position))
            {

                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(getActivity());
                    }
                });


                ConstraintLayout constraintLayout=view.findViewById(R.id.constraintLayout2);
                constraintLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final EditText taskEditText = new EditText(getContext());
                        AlertDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle("Enter Your Name")

                                .setView(taskEditText)
                                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String name = String.valueOf(taskEditText.getText());

                                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                                        reference.child("Users").child(uid).child("name").setValue(name);
                                        reference.child("group").child("group_code").child(group_id).child("ids").child(uid).child("name").setValue(name);
                                        TextView textView=view.findViewById(R.id.textView8);
                                        textView.setText(name);



                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .create();
                        dialog.show();








                    }
                });

                ConstraintLayout constraintLayout2=view.findViewById(R.id.constraint);
                constraintLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText taskEditText = new EditText(getContext());
                        AlertDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle("Enter Your Email")

                                .setView(taskEditText)
                                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String email = String.valueOf(taskEditText.getText());

                                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                                        reference.child("Users").child(uid).child("email").setValue(email);
                                        reference.child("group").child("group_code").child(group_id).child("ids").child(uid).child("email").setValue(email);
                                        TextView textView=view.findViewById(R.id.textView12);
                                        textView.setText(email);



                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .create();
                        dialog.show();








                    }
                });


            }


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

                            name_tv.setText(name);
                            email_tv.setText(email);

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
        View view;
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
         uidd=firebaseUser.getUid();
        if(uidd.equals(Apps.position))
        {
           view=inflater.inflate(R.layout.activity_edit_details, container, false);
        }
        else {
            view= inflater.inflate(R.layout.fragment_profile, container, false);
        }
        return view;
    }




}

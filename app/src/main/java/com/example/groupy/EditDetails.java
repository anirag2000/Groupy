package com.example.groupy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class EditDetails extends AppCompatActivity {
String group_id;
String uid;
String name;
String email;
String photourl;
Uri downloadUrl;
String final_uri;
    ImageView photo;
    StorageReference  mStorageRef;
    Dialog dialog;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
        photo=findViewById(R.id.imageView3);

        Intent intent=getIntent();
        AlertDialog.Builder builder = new AlertDialog.Builder(EditDetails.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog= builder.create();
       group_id= intent.getStringExtra("group_code");
        uid=intent.getStringExtra("uid");
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(EditDetails.this);
            }
        });


        ConstraintLayout constraintLayout=findViewById(R.id.constraintLayout2);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText taskEditText = new EditText(EditDetails.this);
                AlertDialog dialog = new AlertDialog.Builder(EditDetails.this)
                        .setTitle("Enter Your Name")

                        .setView(taskEditText)
                        .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String name = String.valueOf(taskEditText.getText());

                                DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                                reference.child("Users").child(uid).child("name").setValue(name);
                                reference.child("group").child("group_code").child(group_id).child("ids").child(uid).child("name").setValue(name);
                                TextView textView=findViewById(R.id.textView8);
                                textView.setText(name);



                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();








            }
        });

        ConstraintLayout constraintLayout2=findViewById(R.id.constraint);
        constraintLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(EditDetails.this);
                AlertDialog dialog = new AlertDialog.Builder(EditDetails.this)
                        .setTitle("Enter Your Email")

                        .setView(taskEditText)
                        .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String email = String.valueOf(taskEditText.getText());

                                DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                                reference.child("Users").child(uid).child("email").setValue(email);
                                reference.child("group").child("group_code").child(group_id).child("ids").child(uid).child("email").setValue(email);
                                TextView textView=findViewById(R.id.textView12);
                                textView.setText(email);



                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();








            }
        });




       mStorageRef= FirebaseStorage.getInstance().getReference();
       // Toast.makeText(EditDetails.this,uid,Toast.LENGTH_LONG).show();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name=dataSnapshot.child(uid).child("name").getValue(String.class);
                //Toast.makeText(EditDetails.this,name,Toast.LENGTH_LONG).show();
                email=dataSnapshot.child(uid).child("email").getValue(String.class);
                photourl=dataSnapshot.child(uid).child("photourl").getValue(String.class);
                TextView name_tv=findViewById(R.id.textView8);
                TextView email_tv=findViewById(R.id.textView12);
                name_tv.setText(name);
                email_tv.setText(email);
                ImageView photo=findViewById(R.id.imageView3);
                Glide.with(EditDetails.this).load(photourl).into(photo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
     dialog.show();
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri Result_uri = result.getUri();

                final StorageReference ref = mStorageRef.child(uid).child("picture.jpg");
                ref.putFile(Result_uri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = uri;
                    final_uri = uri.toString();

                    if (data != null) {
                        photo.setImageURI(Result_uri);
                       dialog.hide();
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                        reference.child("Users").child(uid).child("photourl").setValue(final_uri);
                        reference.child("group").child("group_code").child(group_id).child("ids").child(uid).child("photourl").setValue(final_uri);




                    }
                })).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.hide();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                    dialog.hide();
                    }
                });
            }
            else
            {
                dialog.hide();
            }
        }
else
        {
            dialog.hide();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

package com.example.groupy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Timer;
import java.util.TimerTask;

public class First_time extends AppCompatActivity {
    String uid;
    FirebaseAuth mAuth;
    String group_id_string;
    boolean group_exists = false;
    boolean fields_valid = false;
    boolean group_code_exists = false;
    ImageView imageview;
    private StorageReference mStorageRef;
    String final_uri;
    ImageView profilepic;
    Uri downloadUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);
        imageview = findViewById(R.id.imageView4);
        imageview.setVisibility(View.INVISIBLE);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        profilepic = findViewById(R.id.profilepic);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(First_time.this);
            }
        });


        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentFirebaseUser == null) {
            Intent intent = new Intent(First_time.this, MainActivity.class);
            startActivity(intent);
        } else {
            uid = currentFirebaseUser.getUid();
        }

        EditText group_id = findViewById(R.id.group_code);
        group_id.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            //for the group entered if correct
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.child("group").child("group_code").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(s.toString()).exists()) {

                            group_exists = true;
                            imageview.setVisibility(View.VISIBLE);
                            imageview.setImageResource(R.drawable.yes);
                        } else {
                            group_exists = false;

                            imageview.setVisibility(View.VISIBLE);
                            imageview.setImageResource(R.drawable.no);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }


    //storing the image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(First_time.this, "Bella ciao", Toast.LENGTH_LONG).show();
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri Result_uri = result.getUri();

                final StorageReference ref = mStorageRef.child(uid).child("picture.jpg");
                ref.putFile(Result_uri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = uri;
                    final_uri = uri.toString();
                    Toast.makeText(First_time.this, "bella ciao part2", Toast.LENGTH_LONG).show();
                    if (data != null) {
                        profilepic.setImageURI(Result_uri);
                    }
                }));
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    //signout

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(First_time.this, "stopped", Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();

    }

//make the user
    public void register(View view) {
        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.date);
        @SuppressLint("CutPasteId") EditText date = findViewById(R.id.date);
        EditText group_id = findViewById(R.id.group_code);

        String name_string = name.getText().toString();
        String date_string = date.getText().toString();
        String email_string = email.getText().toString();
        group_id_string = group_id.getText().toString();

        if (!name_string.isEmpty() && !date_string.isEmpty() && !email_string.isEmpty()) {
            //checking is all are entered
            fields_valid = true;
        }
        if (!group_id_string.isEmpty()) {
            group_code_exists = true;

        }


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        if (fields_valid) {
            Toast.makeText(First_time.this, "gonee", Toast.LENGTH_LONG).show();
            if (group_code_exists) {
                if (group_exists) {

                    //if the group entered was correct

                    User_details user_details;
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                    if (final_uri == null) {
                        user_details = new User_details(name_string, date_string, email_string, group_id_string, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQiuMo9XLPR_Zt5tk2Bytb6yfTpF7mFLP_C2aSdRqNKTnWwHTUj&usqp=CAU", uid);
                    } else {
                        user_details = new User_details(name_string, date_string, email_string, group_id_string, final_uri, uid);
                    }

                    myRef.setValue(user_details);

                    rootRef.child("group").child("group_code").child(group_id_string).child("ids").child(uid).setValue(user_details);
                    SharedPreferences.Editor editor = getSharedPreferences("group", MODE_PRIVATE).edit();
                    editor.putString("group_code", group_id_string);

                    editor.apply();


                    Intent intent = new Intent(First_time.this, MainActivity.class);


                    startActivity(intent);


                } else {
                    Toast.makeText(First_time.this, "Group Doesnt exists", Toast.LENGTH_LONG).show();
                }
            } else {

                //for a new user without a group


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users").child(uid); //the created users fields in firebase
                RandomString randomString = new RandomString();
                group_id_string = randomString.generate(); //the group id the user will have

                //making the group as an User

                User_details group= new User_details("My Group",date_string,"groupemaildoesntexist@email.com",group_id_string
                        ,"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQiuMo9XLPR_Zt5tk2Bytb6yfTpF7mFLP_C2aSdRqNKTnWwHTUj&usqp=CAU",group_id_string);








                User_details user_details;
                if (final_uri == null) {
                    //if the photo wasnt added
                    user_details = new User_details(name_string, date_string, email_string, group_id_string,
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQiuMo9XLPR_Zt5tk2Bytb6yfTpF7mFLP_C2aSdRqNKTnWwHTUj&usqp=CAU", uid);
                } else {
                    //the photo was added
                    user_details = new User_details(name_string, date_string, email_string, group_id_string, final_uri, uid);
                }

                myRef.setValue(user_details);

                Toast.makeText(First_time.this, group_id_string, Toast.LENGTH_LONG).show();

                //adding user and group
                rootRef.child("group").child("group_code").child(group_id_string).child("ids").child(uid).setValue(user_details);
                rootRef.child("group").child("group_code").child(group_id_string).child("ids").child(group_id_string).setValue(group);
                rootRef.child("Users").child(group_id_string).setValue(group);
                rootRef.child("Users").child(uid).setValue(user_details);




                SharedPreferences.Editor editor = getSharedPreferences("group", MODE_PRIVATE).edit();
                editor.putString("group_code", group_id_string);
                editor.apply();
                Intent intent = new Intent(First_time.this, MainActivity.class);
                startActivity(intent);


            }

        } else {
            Toast.makeText(First_time.this, "PLease check your fields", Toast.LENGTH_LONG).show();
        }


    }
}


class RandomString {

    // function to generate a random string of length n
    static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    String generate() {

        // Get the size n
        int n = 6;

        // Get and display the alphanumeric string
        return (RandomString
                .getAlphaNumericString(n));
    }
}


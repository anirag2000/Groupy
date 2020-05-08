package com.example.groupy.HomeFragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.groupy.Home.Home;
import com.example.groupy.HomeFragments.RecyclerView.PhotosRecyclerView;
import com.example.groupy.R;
import com.example.groupy.SigningIn.First_time;

import com.example.groupy.Tools.RandomString;
import com.example.groupy.Tools.img;
import com.example.groupy.calling.Apps;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Photos extends Fragment {

    //adding pictures
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    Uri downloadUrl;
    String final_uri;
    private static int RESULT_LOAD_IMAGE = 1;
    CircleImageView addpichos;
    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();


    //making the adapters
    private RecyclerView staggeredrv;
    private PhotosRecyclerView adapter;
    private StaggeredGridLayoutManager manager;

    //displaying pictures
    DatabaseReference display =FirebaseDatabase.getInstance().getReference();
    List<img> images;







    public Photos() {
        // Required empty public constructor
    }


    boolean addPhoto(){


        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i,RESULT_LOAD_IMAGE);

        return true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);







    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment





        View view = inflater.inflate(R.layout.fragment_photos, container, false);



        addpichos=view.findViewById(R.id.addpichos);
        addpichos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto();
            }
        });

        images = new ArrayList<>();
        staggeredrv=view.findViewById(R.id.recyclerviewphotos);
        //manager= new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        manager= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        staggeredrv.setLayoutManager(manager);
        staggeredrv.setHasFixedSize(true);
        staggeredrv.setItemViewCacheSize(20);
        staggeredrv.setDrawingCacheEnabled(true);
        staggeredrv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter= new PhotosRecyclerView(getContext(),images);
        staggeredrv.setAdapter(adapter);


        //load images onto the recycler view
        display.child("UserPictures").child(Apps.position).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               // images=dataSnapshot.getValue(String.class);

                    String imgurl = dataSnapshot.child("images").getValue(String.class);
                    Log.e("the image urls",imgurl);
                    images.add(new img(imgurl));
                    adapter.notifyDataSetChanged();
                }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //staggeredrv.setHasFixedSize(true);








//        addpichos=view.findViewById(R.id.addpichos);
//        addpichos.setOnClickListener(v -> {
//            addPhoto();
//        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //making sure we are coming back load image activity
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            Log.e("the url of the photo is",selectedImage.toString());


            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Log.e("the picture path is",picturePath);



            StorageReference ref = mStorageRef.child(currentuser.getUid()).child(RandomString.generate()+".jpg");
            ref.putFile(selectedImage).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                downloadUrl = uri;
                final_uri = uri.toString();


                reference.child("UserPictures").child(currentuser.getUid()).child(RandomString.generate()).child("images").setValue(final_uri);





            })).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    //
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //
                }
            });

        }


    }


}

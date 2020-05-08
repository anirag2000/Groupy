package com.example.groupy.HomeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupy.Notes.NotesAdapter;
import com.example.groupy.Notes.NotesMain;
import com.example.groupy.Notes.NotesModal;
import com.example.groupy.R;
import com.example.groupy.calling.Apps;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class Notes extends Fragment {
    String group_code;
    RecyclerView recyclerView;


    public Notes() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        ArrayList<String> title = new ArrayList<>();
//        ArrayList<String> users= new ArrayList<>();
//        ArrayList<String> mdescription = new ArrayList<>();
//        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("group").child("group_code").child(group_code).child("notes").child(Apps.position);
//
//
//
//
//        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
//
//                    NotesModal notesModal=dsp.getValue(NotesModal.class);
//                    title.add(notesModal.title);
//                    mdescription.add(notesModal.description);
//
//                    users.add(notesModal.uid);
//                    NotesAdapter adapter = new NotesAdapter(getContext(),title,mdescription,users);
//
//                    recyclerView.setAdapter(adapter);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//        recyclerView = view.findViewById(R.id.recyclerview);
//        int numberOfColumns = 2;
//        int spaceInPixels = 1;
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
//
//
//        NotesAdapter adapter = new NotesAdapter(getContext(),title,mdescription,users);
//
//        recyclerView.setAdapter(adapter);

    }


}

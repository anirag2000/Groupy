package com.example.groupy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class NotesMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);

     ArrayList<String> title = new ArrayList<>();
     ArrayList<String> mdescription = new ArrayList<>();
     title.add("HELLOOO");
 mdescription.add("hsfugdeufgeudgfudehjfsh,kjfhujsegfygesy");
        title.add("HELLOOO");
        mdescription.add("hsfugdeufgeudgfudehjfsh,kjfhujsegfygesfjshdgjhrdjfghbjdhgkjdhngkjndrkgndkrngkdrngkndrkgny");
        title.add("HELLOOO");
        mdescription.add("hsfugdeufgeudgfudehjfsh,kjfhujsegfygesy");
        title.add("HELLOOO");
        mdescription.add("hsfugdeufgeudgfudehjfshfgjdbjgbdjrbgjbdrjgbjrdbgjbdrjghkjdrhfgjkifhgirhighirhgirhikgjnkrhngkrhkghrkggbkjdr,xgjhgbumjrgjrujrubrjdgbhjxdbrgjbjrdxbg,kjfhujsegfygesy");
        title.add("HELLOOO");
        mdescription.add("hsfugdeufgeudgfudehjfsh,kjfhujsegfygesy");
        title.add("HELLOOO");
        mdescription.add("hsfugdeufgeudgfudehjfshfgjdbjgbdjrbgjbdrjgbjrdbgjbdrjghkjdrhfgjkifhgirhighirhgirhikgjnkrhngkrhkghrkggbkjdr,xgjhgbumjrgjrujrubrjdgbhjxdbrgjbjrdxbg,kjfhujsegfygesy");
        title.add("HELLOOO");
        mdescription.add("hsfugdeufgeudgfudehjfsh,kjfhujsegfygesy");
        title.add("HELLOOO");
        mdescription.add("hsfugdeufgeudgfudehjfshfgjdbjgbdjrbgjbdrjgbjrdbgjbdrjghkjdrhfgjkifhgirhighirhgirhikgjnkrhngkrhkghrkggbkjdr,xgjhgbumjrgjrujrubrjdgbhjxdbrgjbjrdxbg,kjfhujsegfygesy");
        title.add("HELLOOO");
        mdescription.add("hsfugdeufgeudgfudehjfsh,kjfhujsegfygesy");




        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        int numberOfColumns = 2;
        int spaceInPixels = 1;
        recyclerView.addItemDecoration(new RecyclerViewItemDecorator(spaceInPixels));
        recyclerView.setLayoutManager(new GridLayoutManager(NotesMain.this, numberOfColumns));


        NotesAdapter adapter = new NotesAdapter(NotesMain.this,title,mdescription);

        recyclerView.setAdapter(adapter);
    }
}

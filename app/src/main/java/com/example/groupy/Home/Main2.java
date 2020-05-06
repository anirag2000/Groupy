package com.example.groupy.Home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupy.R;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class Main2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        DrawableView drawableView = findViewById(R.id.paintView);
        DrawableViewConfig config = new DrawableViewConfig();
        config.setStrokeColor(getResources().getColor(android.R.color.black));
        config.setShowCanvasBounds(true); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config.setStrokeWidth(10.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(3.0f);
        config.setCanvasHeight(352);
        config.setCanvasWidth(416);
        drawableView.setConfig(config);
        Button button = findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = drawableView.obtainBitmap();
                ImageView image = findViewById(R.id.imageView12);
                image.setImageBitmap(bitmap);

            }
        });


    }
}

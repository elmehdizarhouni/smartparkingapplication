package com.example.parkingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class WelcomeActivity extends AppCompatActivity {

    private Button buttonGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ImageView imageViewWelcome = findViewById(R.id.imageViewWelcome);
        // Load and resize the image using Glide
        Glide.with(this)
                .load(R.drawable.sedan_car)
                .override(600, 400) // Resize the image to 600x400 pixels
                .into(imageViewWelcome);

        buttonGetStarted = findViewById(R.id.buttonGetStarted);
        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        });
    }
}
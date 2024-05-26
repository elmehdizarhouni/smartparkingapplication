package com.example.parkingapp;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewClients;
    private ImageView imageViewReservations;
    private ImageView imageViewParkings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewClients = findViewById(R.id.imageView7);
        imageViewReservations = findViewById(R.id.imageView9);
        imageViewParkings = findViewById(R.id.imageView11);

        // Ajouter un écouteur de clic à l'image des clients
        imageViewClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité UsersActivity
                Intent intent = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(intent);
            }
        });

        imageViewReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité UsersActivity
                Intent intent = new Intent(MainActivity.this, ReservationsActivity.class);
                startActivity(intent);
            }
        });

        imageViewParkings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité UsersActivity
                Intent intent = new Intent(MainActivity.this, ParkingsActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.parkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class SlotsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListenerRegistration registration1, registration2, registration3, registration4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots);

        // Initialise Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve ImageViews from the layout
        ImageView image1 = findViewById(R.id.carImageA11);
        ImageView image2 = findViewById(R.id.carImageA10);
        ImageView image3 = findViewById(R.id.carImageA12);
        ImageView image4 = findViewById(R.id.carImageA14);

        // Check the status of each parking slot
        registration1 = checkSlotStatus("parking-1", image1);
        registration2 = checkSlotStatus("parking-2", image2);
        registration3 = checkSlotStatus("parking-3", image3);
        registration4 = checkSlotStatus("parking-4", image4);
    }

    private ListenerRegistration checkSlotStatus(String parkingId, ImageView imageView) {
        return db.collection("parking").document(parkingId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(SlotsActivity.this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Boolean isTaken = snapshot.getBoolean("taken");
                        if (isTaken != null && isTaken) {
                            // If the slot is taken, set the image color to red
                            imageView.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_IN);
                        } else {
                            // If the slot is not taken, set the image color to green
                            imageView.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
                        }
                    } else {
                        Toast.makeText(SlotsActivity.this, "Le parking " + parkingId + " n'existe pas dans la base de données", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
package com.example.parkingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

import model.ReservationModel;

public class Reservations extends AppCompatActivity {

    private FloatingActionButton addReservation;
    RecyclerView myrecycler;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    LinkedList<ReservationModel> reservations;
    LinearLayout solde,profile,reservationss,logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);
        profile = findViewById(R.id.nav_profile);
        solde = findViewById(R.id.nav_solde);
        reservationss =findViewById(R.id.nav_reservations);
        logout=findViewById(R.id.nav_logout);


        logout.setOnClickListener((v) -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();

            // Start LoginActivity and finish current activity
            Intent loginIntent = new Intent(Reservations.this, AuthActivity.class);
            startActivity(loginIntent);
            finish();
        });
        reservationss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();

            }
        });
        solde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Reservations.this, SoldeActivity.class);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Reservations.this, ProfileUserActivity.class);
            }
        });

        addReservation = (FloatingActionButton) findViewById(R.id.fab);
        db = FirebaseFirestore.getInstance();
        reservations = new LinkedList<>();
        myrecycler = (RecyclerView) findViewById(R.id.recycler);
        ReservationsAdapter Myadapter = new ReservationsAdapter(reservations, Reservations.this);
        addReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reservations.this, AddReservation.class);
                startActivity(intent);
            }
        });

        getReservations();
        myrecycler.setAdapter(Myadapter);

    }


    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    private void getReservations(){
        mAuth = FirebaseAuth.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();
        db.collection("reservation").whereEqualTo("client", userEmail).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Process each document
                                ReservationModel reser = new ReservationModel(document.getString("client"), document.getString("parking"), document.getId(), document.getBoolean("payed"), document.getString("startHour"), document.getString("endHour"), document.getString("startMinutes"), document.getString("endMinutes"), document.getString("day"), document.getString("month"), document.getString("year"));
                                reservations.add(reser);
                                Log.d("Document ID", document.getId());
                            }
                            myrecycler.setHasFixedSize(true);
                            // use a linear layout manager
                            LinearLayoutManager layoutManager = new LinearLayoutManager(Reservations.this);
                            myrecycler.setLayoutManager(layoutManager);
                            // specify an adapter (see also next example)
                            ReservationsAdapter myAdapter = new ReservationsAdapter(reservations, Reservations.this);
                            myrecycler.setAdapter(myAdapter);

                        } else {
                            Toast.makeText(Reservations.this, "Erreur lors de la récupération des réservations", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

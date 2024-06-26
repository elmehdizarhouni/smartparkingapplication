package com.example.parkingapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import model.User;
public class SoldeActivity extends AppCompatActivity{
    private TextView soldetext;
    private Button recherger_solde;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    LinearLayout solde,profile,reservationss,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solde);
        profile = findViewById(R.id.nav_profile);
        solde = findViewById(R.id.nav_solde);
        reservationss =findViewById(R.id.nav_reservations);
        logout=findViewById(R.id.nav_logout);
        logout.setOnClickListener((v) -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();

            // Start LoginActivity and finish current activity
            Intent loginIntent = new Intent(SoldeActivity.this, AuthActivity.class);
            startActivity(loginIntent);
            finish();
        });
        solde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();

            }
        });
        reservationss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(SoldeActivity.this, Reservations.class);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(SoldeActivity.this, ProfileUserActivity.class);
            }
        });

        soldetext = findViewById(R.id.votre_solde);


        recherger_solde = findViewById(R.id.recharge);


        // Initialize Firebase Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Load the user profile
        loadUserSolde();

        // Set the save button click listener
        recherger_solde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SoldeActivity.this, RechargeActivity.class);
                startActivity(intent);
            }
        });
    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    private void loadUserSolde() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                db.collection("user").document(userEmail).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful() && task.getResult() != null) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        int solde = document.getLong("solde").intValue();
                                        soldetext.setText(solde + " dh");


                                        Log.d(TAG, "solde loaded ");
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
            }
        }
    }


}

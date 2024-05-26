package com.example.parkingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import model.User;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "UpdateProfileActivity";

    private TextView editProfileName,  editProfileNumber;
    private Button btnSaveProfile;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    DrawerLayout drawerLayout;
    ImageView menu, logout;
    LinearLayout menunav , profile, clients,reservations,parkings, logoutnav ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu);
        logout = findViewById(R.id.logout);
        menunav = findViewById(R.id.nav_menu);
        profile = findViewById(R.id.nav_profile);
        clients = findViewById(R.id.nav_clients);
        reservations=findViewById(R.id.nav_reservations);
        parkings=findViewById(R.id.nav_parking);
        logoutnav=findViewById(R.id.nav_logout);


        logout.setOnClickListener((v) -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();

            // Start LoginActivity and finish current activity
            Intent loginIntent = new Intent(ProfileActivity.this, AuthActivity.class);
            startActivity(loginIntent);
            finish();
        });




        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);

            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();

            }
        });

        menunav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, MainActivity.class);
            }
        });

        parkings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, ParkingsActivity.class);

            }
        });
        reservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, ReservationsActivity.class);
            }
        });

        clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, UsersActivity.class);
            }
        });
        logoutnav.setOnClickListener((v) -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();

            // Start LoginActivity and finish current activity
            Intent loginIntent = new Intent(ProfileActivity.this, AuthActivity.class);
            startActivity(loginIntent);
            finish();
        });

        editProfileName = findViewById(R.id.profile_name);

        editProfileNumber = findViewById(R.id.profile_number);
        btnSaveProfile = findViewById(R.id.update);
        progressBar = findViewById(R.id.progressbar);

        // Initialize Firebase Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Load the user profile
        loadUserProfile();

        // Set the save button click listener
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });
    }
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }



    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    private void loadUserProfile() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                db.collection("user").document(userEmail).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful() && task.getResult() != null) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String nom = document.getString("nom");
                                        String prenom = document.getString("prenom");
                                        String tel = document.getString("tel");

                                        editProfileName.setText(nom);

                                        editProfileNumber.setText(tel);

                                        Log.d(TAG, "User profile loaded: " + nom + " " + prenom);
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

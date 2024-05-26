package com.example.parkingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class UpdateProfileActivity extends AppCompatActivity {

    private static final String TAG = "UpdateProfileActivity";

    private EditText editProfileName,  editProfileNumber;
    private Button btnSaveProfile;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

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
                saveUserProfile();
            }
        });
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

    private void saveUserProfile() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                String nom = editProfileName.getText().toString();

                String tel = editProfileNumber.getText().toString();

                User updatedUser = new User(nom, tel);

                db.collection("user").document(userEmail).set(updatedUser)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated successfully");
                                    Toast.makeText(UpdateProfileActivity.this, "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                    // Redirection vers ProfileActivity
                                    Intent intent = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                                    startActivity(intent);

                                    // (Optionnel) Terminer l'activité actuelle si vous ne voulez pas permettre un retour à UpdateProfileActivity
                                    finish();
                                } else {
                                    Log.d(TAG, "Error updating profile", task.getException());
                                    Toast.makeText(UpdateProfileActivity.this, "Erreur de mise à jour du profil", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
}

package com.example.parkingapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RechargeActivity extends AppCompatActivity {
    private EditText solderecharge;
    private Button btnrecharge;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        solderecharge = findViewById(R.id.montant);
        btnrecharge = findViewById(R.id.recharge);

        // Initialize Firebase Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Set the recharge button click listener
        btnrecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechargeSolde();
            }
        });
    }

    private void rechargeSolde() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            final String userEmail = currentUser.getEmail();

            if (userEmail != null) {
                String soldeRechargeStr = solderecharge.getText().toString();

                if (soldeRechargeStr.isEmpty()) {
                    Toast.makeText(RechargeActivity.this, "Veuillez saisir un montant", Toast.LENGTH_SHORT).show();
                    return;
                }

                final int soldeRecharge = Integer.parseInt(soldeRechargeStr);

                DocumentReference userRef = db.collection("user").document(userEmail);

                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                int solde = document.getLong("solde").intValue(); // Récupérer le solde depuis Firestore
                                int nouveauSolde = solde + soldeRecharge;

                                userRef.update("solde", nouveauSolde)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Solde mis à jour avec succès");
                                                    Toast.makeText(RechargeActivity.this, "Solde mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                                    // Rediriger vers SoldeActivity
                                                    Intent intent = new Intent(RechargeActivity.this, SoldeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Log.e(TAG, "Erreur lors de la mise à jour du solde", task.getException());
                                                    Toast.makeText(RechargeActivity.this, "Erreur lors de la mise à jour du solde", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Log.d(TAG, "Le document n'existe pas");
                                Toast.makeText(RechargeActivity.this, "Utilisateur introuvable", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Échec de la récupération du document", task.getException());
                            Toast.makeText(RechargeActivity.this, "Échec de la récupération de l'utilisateur", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

}

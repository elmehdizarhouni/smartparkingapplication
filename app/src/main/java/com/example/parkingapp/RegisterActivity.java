package com.example.parkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nomEditText;

    private EditText prenomEditText;
    private EditText emailEditText;
    private EditText telEditText;
    private EditText passwordEditText;
    ProgressBar progressBar;
    private Button registerButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.purple_200));

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar =findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        nomEditText = findViewById(R.id.name);

        emailEditText = findViewById(R.id.email_reg);
        telEditText = findViewById(R.id.phone_reg);
        passwordEditText = findViewById(R.id.password_reg);
        registerButton = findViewById(R.id.reg_btn);

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_btn) {
            registerUser();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void registerUser() {
        final String nom = nomEditText.getText().toString().trim();

        final String email = emailEditText.getText().toString().trim();
        final String tel = telEditText.getText().toString().trim();
        final int solde = 0;
        String password = passwordEditText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserToFirestore( nom, email, tel,solde);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void saveUserToFirestore(String nom,  String email, String tel, int solde) {
        Map<String, Object> user = new HashMap<>();
        user.put("nom", nom);

        user.put("tel", tel);
        user.put("solde", 0);


        db.collection("user").document(email)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("RegisterActivity", "User document successfully written!");
                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        finish(); // Finishing the activity after successful registration
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("RegisterActivity", "Error writing document", e);
                        Toast.makeText(RegisterActivity.this, "Error writing document", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

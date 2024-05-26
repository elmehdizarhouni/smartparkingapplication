// UsersActivity.java
package com.example.parkingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private FirebaseFirestore db;
    DrawerLayout drawerLayout;
    ImageView menu, logout;
    LinearLayout menunav, profile, clients, reservations, parkings, logoutnav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu);
        logout = findViewById(R.id.logout);
        menunav = findViewById(R.id.nav_menu);
        profile = findViewById(R.id.nav_profile);
        clients = findViewById(R.id.nav_clients);
        reservations = findViewById(R.id.nav_reservations);
        parkings = findViewById(R.id.nav_parking);
        logoutnav = findViewById(R.id.nav_logout);

        logout.setOnClickListener((v) -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();

            // Start LoginActivity and finish current activity
            Intent loginIntent = new Intent(UsersActivity.this, AuthActivity.class);
            startActivity(loginIntent);
            finish();
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);

            }
        });

        clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();

            }
        });

        menunav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(UsersActivity.this, MainActivity.class);
            }
        });

        parkings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(UsersActivity.this, ParkingsActivity.class);

            }
        });
        reservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(UsersActivity.this, ReservationsActivity.class);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(UsersActivity.this, ProfileActivity.class);
            }
        });
        logoutnav.setOnClickListener((v) -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();

            // Start LoginActivity and finish current activity
            Intent loginIntent = new Intent(UsersActivity.this, AuthActivity.class);
            startActivity(loginIntent);
            finish();
        });

        recyclerView = findViewById(R.id.recycler_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);

        db = FirebaseFirestore.getInstance();

        // Retrieve the list of users from Firestore
        getUsersFromFirestore();
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    private void getUsersFromFirestore() {
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.getString("nom");
                                Double solde = document.getDouble("solde");
                                String tel = document.getString("tel");

                                // Check if solde is not null
                                double soldeValue = (solde != null) ? solde : 0.0;

                                User user = new User(nom, soldeValue, tel);
                                userList.add(user);
                            }
                            userAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            Toast.makeText(UsersActivity.this, "Failed to retrieve users", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

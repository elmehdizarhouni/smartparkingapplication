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

public class ParkingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParkingAdapter parkingAdapter;
    private List<Parking> parkingList;
    private FirebaseFirestore db;
    DrawerLayout drawerLayout;
    ImageView menu, logout;
    LinearLayout menunav , profile, clients,reservations,parkings, logoutnav ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkings);

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
            Intent loginIntent = new Intent(ParkingsActivity.this, AuthActivity.class);
            startActivity(loginIntent);
            finish();
        });




        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);

            }
        });



        parkings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();

            }
        });

        menunav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ParkingsActivity.this, MainActivity.class);
            }
        });

        clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ParkingsActivity.this, UsersActivity.class);

            }
        });
        reservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ParkingsActivity.this, ReservationsActivity.class);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ParkingsActivity.this, ProfileActivity.class);
            }
        });
        logoutnav.setOnClickListener((v) -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();

            // Start LoginActivity and finish current activity
            Intent loginIntent = new Intent(ParkingsActivity.this, AuthActivity.class);
            startActivity(loginIntent);
            finish();
        });




        recyclerView = findViewById(R.id.recycler_view_parkings);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        parkingList = new ArrayList<>();
        parkingAdapter = new ParkingAdapter(this, parkingList);
        recyclerView.setAdapter(parkingAdapter);

        db = FirebaseFirestore.getInstance();

        // Récupérer la liste des parkings depuis Firestore
        getParkingsFromFirestore();
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

    private void getParkingsFromFirestore() {
        db.collection("parking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                Boolean taken = document.getBoolean("taken");
                                String update = document.getString("update");

                                Parking parking = new Parking(name, taken, update);
                                parkingList.add(parking);
                            }
                            parkingAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            Toast.makeText(ParkingsActivity.this, "Failed to retrieve parkings", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

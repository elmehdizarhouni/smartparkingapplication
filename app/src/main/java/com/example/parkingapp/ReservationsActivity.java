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

public class ReservationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter;
    private List<Reservation> reservationList;
    private FirebaseFirestore db;
    DrawerLayout drawerLayout;
    ImageView menu, logout;
    LinearLayout menunav , profile, clients,reservations,parkings, logoutnav ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);
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
            Intent loginIntent = new Intent(ReservationsActivity.this, AuthActivity.class);
            startActivity(loginIntent);
            finish();
        });




        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);

            }
        });



        reservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();

            }
        });

        menunav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ReservationsActivity.this, MainActivity.class);
            }
        });

        parkings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ReservationsActivity.this, ParkingsActivity.class);

            }
        });
        clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ReservationsActivity.this, UsersActivity.class);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ReservationsActivity.this, ProfileActivity.class);
            }
        });
        logoutnav.setOnClickListener((v) -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();

            // Start LoginActivity and finish current activity
            Intent loginIntent = new Intent(ReservationsActivity.this, AuthActivity.class);
            startActivity(loginIntent);
            finish();
        });


        recyclerView = findViewById(R.id.recycler_view_reservations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reservationList = new ArrayList<>();
        reservationAdapter = new ReservationAdapter(this, reservationList);
        recyclerView.setAdapter(reservationAdapter);

        db = FirebaseFirestore.getInstance();

        // Récupérer la liste des utilisateurs depuis Firestore
        getReservationsFromFirestore();
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
    private void getReservationsFromFirestore() {
        db.collection("reservation")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String client = document.getString("client");
                                String parking = document.getString("parking");  // Utilisation de getDouble pour récupérer solde
                                Boolean payed = document.getBoolean("payed");

                                // Vérification si solde n'est pas null

                                Reservation reservation = new Reservation(client, parking, payed);
                                reservationList.add(reservation);
                            }
                            reservationAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            Toast.makeText(ReservationsActivity.this, "Failed to retrieve users", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}

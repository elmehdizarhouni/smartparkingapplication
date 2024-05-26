package com.example.parkingapp;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

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

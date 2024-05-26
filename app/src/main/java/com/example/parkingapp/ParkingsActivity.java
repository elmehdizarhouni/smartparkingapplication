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

public class ParkingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParkingAdapter parkingAdapter;
    private List<Parking> parkingList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkings);

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

package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;

import model.ReservationModel;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.MyViewHolder>{

    LinkedList<ReservationModel> reservations = new LinkedList<ReservationModel>();

    private Context context;
    private FirebaseFirestore db;

    public ReservationsAdapter(LinkedList<ReservationModel> reservations, Context context){
        this.context = context;
        this.reservations = reservations;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// create a new view

        View itemLayoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item_layout,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(itemLayoutView);
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.parking.setText("Parking - " + reservations.get(position).getParking());
        String d = reservations.get(position).getDay() +"/" + reservations.get(position).getMonth() + "/" + reservations.get(position).getYear() + " "  + reservations.get(position).getStartHour() + ":" + reservations.get(position).getStartMinute() + " - " + reservations.get(position).getEndHour() + ":" + reservations.get(position).getEndMinute();
        holder.date.setText(d);
        if(reservations.get(position).getPayed()){
            holder.payed.setText("Payé");
        } else {
            holder.payed.setText("No payé");
        }
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        db = FirebaseFirestore.getInstance();
                        if (item.getItemId() == R.id.update) {
                            // handle menu1 click
                            Intent intent = new Intent(context, UpdateReservation.class);
                            // Put the selected task as an extra in the intent
                            intent.putExtra("reservation", reservations.get(position));
                            // Start the activity
                            context.startActivity(intent);
                            return true;
                        } else if (item.getItemId() == R.id.delete) {
                            // handle menu2 click
                            db.collection("reservation").document(reservations.get(position).getId()).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(context, "deleted successfuly", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, Reservations.class);
                                            context.startActivity(intent);
                                        }
                                    });
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

        // Reference to an image file in Cloud Storage
        //StorageReference storageReference =
        // FirebaseStorage.getInstance().getReferenceFromUrl(filteredTaches.get(position).getImg());
        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)

        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When an item is clicked, create an intent to start TaskDetailsActivity
                Intent intent = new Intent(context, TaskActivity.class);
                // Put the selected task as an extra in the intent
                intent.putExtra("task", filteredTaches.get(position));
                // Start the activity
                context.startActivity(intent);
            }
        });

         */
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return reservations.size();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        public TextView parking;
        public TextView date;
        public TextView payed;
        public  TextView menu;
        // Context is a reference to the activity that contain the the recycler view
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            menu = itemLayoutView.findViewById(R.id.textViewOptions);
            parking =itemLayoutView.findViewById(R.id.parkingPlace);
            date = itemLayoutView.findViewById(R.id.date);
            payed = itemLayoutView.findViewById(R.id.payed);
            itemLayoutView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {


        }
    }

}

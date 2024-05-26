package com.example.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private Context mContext;
    private List<Reservation> mReservationList;

    public ReservationAdapter(Context context, List<Reservation> reservationList) {
        mContext = context;
        mReservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = mReservationList.get(position);
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return mReservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewClient;
        private TextView mTextViewParking;
        private TextView mTextViewPayed;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewClient = itemView.findViewById(R.id.text_view_client);
            mTextViewParking = itemView.findViewById(R.id.text_view_parking);
            mTextViewPayed = itemView.findViewById(R.id.text_view_payed);
        }

        public void bind(Reservation reservation) {
            mTextViewClient.setText(reservation.getClient());
            mTextViewParking.setText(reservation.getParking());
            mTextViewPayed.setText(reservation.isPayed() ? "Paid" : "Not Paid");
        }
    }
}

package com.example.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder> {

    private Context mContext;
    private List<Parking> mParkingList;

    public ParkingAdapter(Context context, List<Parking> parkingList) {
        mContext = context;
        mParkingList = parkingList;
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.parking_item, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
        Parking parking = mParkingList.get(position);
        holder.bind(parking);
    }

    @Override
    public int getItemCount() {
        return mParkingList.size();
    }

    public static class ParkingViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewName;
        private TextView mTextViewTaken;
        private TextView mTextViewUpdate;

        public ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewName = itemView.findViewById(R.id.text_view_name);
            mTextViewTaken = itemView.findViewById(R.id.text_view_taken);
            mTextViewUpdate = itemView.findViewById(R.id.text_view_update);
        }

        public void bind(Parking parking) {
            mTextViewName.setText(parking.getName());
            mTextViewTaken.setText(parking.isTaken() ? "Taken" : "Not Taken");
            mTextViewUpdate.setText(parking.getUpdate());
        }
    }
}

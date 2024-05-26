package com.example.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mContext;
    private List<User> mUserList;

    public UserAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewNom;
        private TextView mTextViewSolde;
        private TextView mTextViewTel;
        private Button mDeleteButton; // Add reference to the delete button

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewNom = itemView.findViewById(R.id.text_view_nom);
            mTextViewSolde = itemView.findViewById(R.id.text_view_solde);
            mTextViewTel = itemView.findViewById(R.id.text_view_tel);
            mDeleteButton = itemView.findViewById(R.id.deletebutton); // Initialize the delete button
        }

        public void bind(User user) {
            mTextViewNom.setText(user.getNom());
            mTextViewSolde.setText(String.valueOf(user.getSolde()));
            mTextViewTel.setText(user.getTel());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser usero = mAuth.getCurrentUser();
            // Set OnClickListener for the delete button
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the item that was clicked
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Remove the user from the list
                        mUserList.remove(position);
                        // Notify adapter that an item is removed
                        notifyItemRemoved(position);
                    }
                }
            });
        }
    }
}

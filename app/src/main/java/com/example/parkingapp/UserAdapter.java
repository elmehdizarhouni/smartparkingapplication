package com.example.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewNom;
        private TextView mTextViewSolde;
        private TextView mTextViewTel;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewNom = itemView.findViewById(R.id.text_view_nom);
            mTextViewSolde = itemView.findViewById(R.id.text_view_solde);
            mTextViewTel = itemView.findViewById(R.id.text_view_tel);
        }

        public void bind(User user) {
            mTextViewNom.setText(user.getNom());
            mTextViewSolde.setText(String.valueOf(user.getSolde()));  // Conversion en chaîne de caractères
            mTextViewTel.setText(user.getTel());
        }
    }
}

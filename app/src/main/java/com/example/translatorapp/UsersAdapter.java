package com.example.translatorapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {

    //Array of Users
    private ArrayList<User> users;
    private Context context;
    private OnUserClickListener listener;

    public UsersAdapter(ArrayList<User> users, Context context, OnUserClickListener listener) {
        this.users = users;
        this.context = context;
        this.listener = listener;
    }

    //Interface for AllMessages ClickListener
    interface OnUserClickListener{
        void onUserClicked(int position);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflates userHolder resource
        View view = LayoutInflater.from(context).inflate(R.layout.userholder,parent,false);

        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.userEmail.setText(users.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder{

        TextView userEmail;
        ImageView userPhoto;
        public UserHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onUserClicked(getAdapterPosition());
                }
            });
            userEmail = itemView.findViewById(R.id.userEmailTxt);
            userPhoto = itemView.findViewById((R.id.friendImg));
        }
    }
}

package com.example.translatorapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
        holder.username.setText(users.get(position).getEmail());
        Glide.with(context).load(users.get(position).getProfilePhoto())
                .error(R.drawable.accounticon).placeholder(R.drawable.accounticon)
                .into(holder.userPhoto);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder{

        TextView username;
        ImageView userPhoto;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onUserClicked(getAdapterPosition());
                }
            });
            username = itemView.findViewById(R.id.usernameTxt);
            userPhoto = itemView.findViewById((R.id.friendImg));
        }
    }
}

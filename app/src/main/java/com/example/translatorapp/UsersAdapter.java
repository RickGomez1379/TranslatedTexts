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
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;

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
        holder.username.setText(users.get(position).getUsername());
        holder.usersLanguage.setText(getLanguageCode(users.get(position).getLanguageCode()));
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
        TextView usersLanguage;

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
            usersLanguage = itemView.findViewById(R.id.usersLanguage);
        }
    }
    private String getLanguageCode(int fromLanguage) {

        String languageCode;
        switch (fromLanguage) {
            case 11:
                languageCode = "English";
                break;
            case 0:
                languageCode = "Afrikaans";
                break;
            case 1:
                languageCode = "Arabic";
                break;
            case 2:
                languageCode ="Belarusian";
                break;
            case 3:
                languageCode = "Bulgarian";
                break;
            case 4:
                languageCode = "Bengali";
                break;
            case 5:
                languageCode = "Catalan";
                break;
            case 6:
                languageCode = "Czech";
                break;
            case 7:
                languageCode = "Welsh";
                break;
            case 22:
                languageCode = "Hindi";
                break;
            case 56:
                languageCode = "Urdu";
                break;
            case 13:
                languageCode = "Spanish";
                break;
            case 29:
                languageCode = "Japanese";
                break;
            default:
                languageCode = "Unable to get Language";
                break;
        }
        return languageCode;
    }
}

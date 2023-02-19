package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllMessages extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private ProgressBar bar;
    private UsersAdapter usersAdapter;
    UsersAdapter.OnUserClickListener onUserClickListener;
    private int userLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_messages);

        users = new ArrayList<>();
        bar = findViewById(R.id.messagesProgressBar);
        recyclerView = findViewById(R.id.messagesRecycler);


        //When Users is Clicked, Opens Messages between Users
        onUserClickListener = new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(int position) {

                //Carry data to Messages Activity
                startActivity(new Intent(AllMessages.this, Message.class)
                        .putExtra("username_of_messenger", users.get(position).getUsername())
                        .putExtra("email_of_messenger", users.get(position).getEmail())
                        .putExtra("language_of_messenger", users.get(position).getLanguageCode())
                        .putExtra("language_of_user", userLanguage));

            }
        };
        getUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profilemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menuItemProfile)
        {
            startActivity(new Intent(AllMessages.this, Profile.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    //Populates Users Array
    private void getUsers(){
        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    //Gets Language Code of User's
                    if(data.getValue(User.class).getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        userLanguage= data.getValue(User.class).getLanguageCode();
                    }
                    //Populate Array
                    users.add(data.getValue(User.class));
                }
                //Displays Users in this Activity
                usersAdapter = new UsersAdapter(users, AllMessages.this,onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(AllMessages.this));
                recyclerView.setAdapter(usersAdapter);
                bar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
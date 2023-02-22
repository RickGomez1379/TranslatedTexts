package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class Contacts extends AppCompatActivity {

    private BottomNavigationView nav;
    private UsersAdapter usersAdapter;
    private ProgressBar bar;
    private ArrayList<User> users;
    private RecyclerView recyclerView;
    private UsersAdapter.OnUserClickListener onUserClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        bar = findViewById(R.id.contactsBar);
        recyclerView = findViewById(R.id.contactsRecycler);
        users = new ArrayList<>();

        nav = findViewById(R.id.ContactsNavigationView);

        //Change Action Bar
        ChangeActionBar("Contacts");

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.MessagesNav) {
                    startActivity(new Intent(Contacts.this, AllMessages.class));
                    finish();
                }
                else if (item.getItemId()==R.id.Settings) {
                    startActivity(new Intent(Contacts.this, Profile.class));
                    finish();
                }
                return false;
            }
        });
        getUsers();
    }

    private void getUsers(){

        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data : snapshot.getChildren()){

                    //Prevents from Adding Current User to Arraylist
                    if(data.getValue(User.class).getEmail().toLowerCase(Locale.ROOT).equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().toLowerCase(Locale.ROOT))){

                    }
                    else
                        //Populate Array
                        users.add(data.getValue(User.class));

                }
                //Sorts Array
                users.sort(new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        return user.getEmail().toLowerCase(Locale.ROOT).compareTo(t1.getEmail().toLowerCase(Locale.ROOT));
                    }
                });
                //Displays Users in this Activity
                usersAdapter = new UsersAdapter(users, Contacts.this,onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(Contacts.this));
                recyclerView.setAdapter(usersAdapter);
                bar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void ChangeActionBar(String title){
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#2699FB"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
    }
}
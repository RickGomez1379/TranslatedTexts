package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Contacts extends AppCompatActivity {

    private BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        nav = findViewById(R.id.ContactsNavigationView);

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
    }
}
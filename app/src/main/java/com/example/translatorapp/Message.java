package com.example.translatorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Message extends AppCompatActivity {

    private RecyclerView messages;
    private EditText edtMessage;
    private TextView txtChattingWith;
    private ProgressBar progressMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messages = findViewById(R.id.recyclerMessage);
        edtMessage = findViewById(R.id.edtMessage);
        txtChattingWith = findViewById(R.id.userChattingWith);
        progressMsg = findViewById(R.id.progressBarMessage);
    }
}
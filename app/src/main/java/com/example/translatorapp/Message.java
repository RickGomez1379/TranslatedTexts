package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Message extends AppCompatActivity {

    private RecyclerView messagesRecyclerView;
    private EditText edtMessage;
    private TextView txtChattingWith;
    private ProgressBar progressMsg;
    private ArrayList<MessageClass> messages;
    private ImageView sendImg;
    private String emailofMessenger, chatroomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        emailofMessenger = getIntent().getStringExtra("useremail_of_messenger");
        messagesRecyclerView = findViewById(R.id.recyclerMessage);
        edtMessage = findViewById(R.id.edtMessage);
        txtChattingWith = findViewById(R.id.userChattingWith);
        progressMsg = findViewById(R.id.progressBarMessage);
        messages = new ArrayList<>();
        sendImg = findViewById(R.id.sendImgView);

        txtChattingWith.setText(emailofMessenger);
        setUpChatRoom();
    }


    //Makes Chat Room between Users
    private void setUpChatRoom(){
        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myEmail = snapshot.getValue(User.class).getEmail();
                if(emailofMessenger.compareTo(myEmail) > 0 ) {
                    chatroomId = myEmail + emailofMessenger;
                }
                else {
                    chatroomId = emailofMessenger + myEmail;
                }
                attachMessageListener(chatroomId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void attachMessageListener(String chatroomId) {
        FirebaseDatabase.getInstance().getReference("messages/" + chatroomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Clear Messages before Adding Data
                messages.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue((MessageClass.class)));
                }
                messagesRecyclerView.scrollToPosition(messages.size() - 1);
                messagesRecyclerView.setVisibility(View.VISIBLE);
                progressMsg.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
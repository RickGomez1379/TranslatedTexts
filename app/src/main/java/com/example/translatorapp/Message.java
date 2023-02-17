package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private String usernameofMessenger, emailofMessenger, chatroomId;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        usernameofMessenger = getIntent().getStringExtra("username_of_messenger");
        emailofMessenger = getIntent().getStringExtra("email_of_messenger");

        messagesRecyclerView = findViewById(R.id.recyclerMessage);
        edtMessage = findViewById(R.id.edtMessage);
        txtChattingWith = findViewById(R.id.userChattingWith);
        progressMsg = findViewById(R.id.progressBarMessage);

        messages = new ArrayList<>();
        sendImg = findViewById(R.id.sendImgView);
        messageAdapter = new MessageAdapter(messages, getIntent(), Message.this);
        txtChattingWith.setText(usernameofMessenger);
        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtMessage.getText().toString().isEmpty())
                    return;

                else {
                    //Send Message to Database
                    FirebaseDatabase.getInstance().getReference("messages/" + chatroomId).push()
                            .setValue(new MessageClass(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                    usernameofMessenger, edtMessage.getText().toString()));
                    edtMessage.setText("");
                }
            }
        });
        //Sets up Recycler View
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messageAdapter);
        setUpChatRoom();
    }


    //Makes Chat Room between Users
    private void setUpChatRoom(){
        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.getValue(User.class).getUsername();
                if(usernameofMessenger.compareTo(username) > 0 ) {
                    chatroomId = username + usernameofMessenger;
                }
                else if(usernameofMessenger.compareTo(username)==0){
                    chatroomId = username + usernameofMessenger;
                }
                else {
                    chatroomId = usernameofMessenger + username;
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
                //Clear Previous Messages
                messages.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue((MessageClass.class)));
                }
                messageAdapter.notifyDataSetChanged();
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
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

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
    private int messengerLanguage;
    private int userLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //Gets Receiver Information
        usernameofMessenger = getIntent().getStringExtra("username_of_messenger");
        emailofMessenger = getIntent().getStringExtra("email_of_messenger");
        messengerLanguage = getIntent().getIntExtra("language_of_messenger", 1);

        //Gets Sender's Preferred Language
        userLanguage = getIntent().getIntExtra("language_of_user",1);

        messagesRecyclerView = findViewById(R.id.recyclerMessage);
        edtMessage = findViewById(R.id.edtMessage);

        //Receiver
        txtChattingWith = findViewById(R.id.userChattingWith);

        progressMsg = findViewById(R.id.progressBarMessage);

        txtChattingWith.setText(usernameofMessenger);
        sendImg = findViewById(R.id.sendImgView);

        messages = new ArrayList<>();


        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtMessage.getText().toString().isEmpty())
                    return;

                else {

                    //Send Original and Translated Message to Database and Receiver
                    TranslateText(userLanguage, messengerLanguage, edtMessage.getText().toString());

                }
            }
        });
        //Sets up Recycler View
        messageAdapter = new MessageAdapter(messages, Message.this);
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

        //Populates ChatRoom Messages
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
    private void TranslateText(int fromLanguage, int toLanguage, String source)
    {
        //Sets Translator Options
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguage)
                .setTargetLanguage(toLanguage)
                .build();

        //Creates Translator
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        //Creates a Model Builder
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        //Downloads Language Models if Needed
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                //Translates Messages
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {


                        //Send Original Message to Database
                        FirebaseDatabase.getInstance().getReference("messages/" + chatroomId).push()
                                .setValue(new MessageClass(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                        usernameofMessenger, edtMessage.getText().toString()));

                        //Send Translated Message to Database
                        FirebaseDatabase.getInstance().getReference("messages/" + chatroomId).push()
                                .setValue(new MessageClass(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                        usernameofMessenger, s));

                        edtMessage.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Message.this,
                                "Fail to Translate: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Message.this,
                        "Failed to Download Model: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
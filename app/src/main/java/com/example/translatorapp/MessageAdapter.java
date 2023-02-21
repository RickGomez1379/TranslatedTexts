package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<MessageClass> messages;
    private Context context;

    public MessageAdapter(ArrayList<MessageClass> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_holder,parent,false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
    holder.txtMessage.setText(messages.get(position).getContent());
    ConstraintLayout cl = holder.cl;

    //Change Constraint Determined by Which User is Messaging
        //User's Message on the Right Side
    if(messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.clear(R.id.messageCarView, ConstraintSet.LEFT);
        constraintSet.clear(R.id.messageContent, ConstraintSet.LEFT);
        constraintSet.connect(R.id.messageCarView, ConstraintSet.RIGHT, R.id.cLayout, ConstraintSet.RIGHT, 0);
        constraintSet.connect(R.id.messageContent, ConstraintSet.RIGHT, R.id.messageCarView, ConstraintSet.LEFT, 0);
        constraintSet.applyTo(cl);

    }
    else{
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.clear(R.id.messageCarView, ConstraintSet.RIGHT);
        constraintSet.clear(R.id.messageContent, ConstraintSet.RIGHT);
        constraintSet.connect(R.id.messageCarView, ConstraintSet.LEFT, R.id.cLayout, ConstraintSet.LEFT, 0);
        constraintSet.connect(R.id.messageContent, ConstraintSet.LEFT, R.id.messageCarView, ConstraintSet.RIGHT, 0);
        constraintSet.applyTo(cl);
    }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        ConstraintLayout cl;
        TextView txtMessage;


        public MessageHolder(@NonNull View itemView){
            super(itemView);
            cl = itemView.findViewById(R.id.cLayout);
            txtMessage = itemView.findViewById(R.id.messageContent);
        }
    }

}

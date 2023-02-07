package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private Button register;
    private TextView loginInstead;
    private EditText email;
    private EditText password;
    private EditText reenterPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.RegisterButton);
        loginInstead = findViewById(R.id.LoginHere);
        email = findViewById(R.id.RegisterEmail);
        password = findViewById(R.id.RegisterPassword);
        reenterPassword = findViewById(R.id.ReenterPassword);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString() == "")
                    Toast.makeText(Register.this, "Please Enter an Email. ", Toast.LENGTH_SHORT).show();
                else if(password.getText().toString() == "")
                    Toast.makeText(Register.this, "Please Enter a Password. ", Toast.LENGTH_SHORT).show();
                else if(password.getText().toString() != reenterPassword.getText().toString())
                    Toast.makeText(Register.this, "Password Entries are not the same. Please Re-enter your Password. ", Toast.LENGTH_SHORT).show();
                else
                    HandleRegister();
            }
        });

    }

    private void HandleRegister()
    {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    Toast.makeText(Register.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Register.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
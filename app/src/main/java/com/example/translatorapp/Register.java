package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;

public class Register extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText reenterPassword;
    private EditText username;
    String[] Languages = { "English", "Afrikaans", "Arabic", "Belarusian", "Bulgarian",
            "Bengali", "Catalan", "Czech", "Welsh", "Hindi", "Urdu", "Spanish", "Japanese"};
    private int languageCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button register = findViewById(R.id.RegisterButton);
        TextView loginInstead = findViewById(R.id.LoginHere);
        email = findViewById(R.id.RegisterEmail);
        password = findViewById(R.id.RegisterPassword);
        reenterPassword = findViewById(R.id.ReenterPassword);
        username = findViewById(R.id.RegisterUsername);
        Spinner languageSpinner = findViewById(R.id.languageSpinner);


        //Register User
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty())
                    Toast.makeText(Register.this, "Please Enter an Email. ", Toast.LENGTH_SHORT).show();
                else if(password.getText().toString().isEmpty())
                    Toast.makeText(Register.this, "Please Enter a Password. ", Toast.LENGTH_SHORT).show();
                else if(!password.getText().toString().equals(reenterPassword.getText().toString()))
                    Toast.makeText(Register.this, "Password Entries are not the same. Please Re-enter your Password. ", Toast.LENGTH_SHORT).show();
                else
                    HandleRegister();
            }
        });

        //Navigates to Login Activity
        loginInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        //Gets Users Preferred Language
        //By Default: English
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                languageCode = getLanguageCode(Languages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Adapter to display languages
        ArrayAdapter fromAdapter = new ArrayAdapter(this,R.layout.spinner_item,Languages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(fromAdapter);

    }

    private void HandleRegister()
    {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //Creates User Object in Realtime Database
                    FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User(email.getText().toString(), "", username.getText().toString(), languageCode));
                    Toast.makeText(Register.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, AllMessages.class));
                    finish();
                }
                else
                    Toast.makeText(Register.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Gets User's Preferred Language to Store as an Int
    private int getLanguageCode(String fromLanguage) {

        switch (fromLanguage) {
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "Afrikaans":
                languageCode = FirebaseTranslateLanguage.AF;
                break;
            case "Arabic":
                languageCode = FirebaseTranslateLanguage.AR;
                break;
            case "Belarusian":
                languageCode = FirebaseTranslateLanguage.BE;
                break;
            case "Bulgarian":
                languageCode = FirebaseTranslateLanguage.BG;
                break;
            case "Bengali":
                languageCode = FirebaseTranslateLanguage.BN;
                break;
            case "Catalan":
                languageCode = FirebaseTranslateLanguage.CA;
                break;
            case "Czech":
                languageCode = FirebaseTranslateLanguage.CS;
                break;
            case "Welsh":
                languageCode = FirebaseTranslateLanguage.CY;
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "Urdu":
                languageCode = FirebaseTranslateLanguage.UR;
                break;
            case "Spanish":
                languageCode = FirebaseTranslateLanguage.ES;
                break;
            case "Japanese":
                languageCode = FirebaseTranslateLanguage.JA;
                break;
            default:
                languageCode = 1;
                break;
        }
        return languageCode;
    }
}
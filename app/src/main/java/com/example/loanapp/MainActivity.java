package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    Button lendButton;
    Button borrowButton;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        borrowButton = findViewById(R.id.borrowbtn);
        lendButton = findViewById(R.id.lendbtn);
        welcomeText = findViewById(R.id.welcome_text);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            welcomeText.setText("Welcome to LendMe, " + currentUser.getUsername() + " !");
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        lendButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LendingActivity.class));
        });
        borrowButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BorrowingActivity.class));
        });
    }
}
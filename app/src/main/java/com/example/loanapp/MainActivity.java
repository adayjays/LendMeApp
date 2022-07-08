package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    Button lendbtn;
    Button borrowbtn;
    TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        borrowbtn = findViewById(R.id.borrowbtn);
        lendbtn = findViewById(R.id.lendbtn);
        welcomeText = findViewById(R.id.welcome_text);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            welcomeText.setText(String.format("Welcome to LendMe, %s !", currentUser.getUsername()));
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        lendbtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LendingActivity.class));
        });
        borrowbtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BorrowingActivity.class));
        });
    }
}
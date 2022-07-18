package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    Button lendBtn;
    Button borrowBtn;
    TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        borrowBtn = findViewById(R.id.borrowbtn);
        lendBtn = findViewById(R.id.lendbtn);
        welcomeText = findViewById(R.id.welcome_text);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            welcomeText.setText("Welcome to LendMe, "+currentUser.getUsername()+ " !");
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        lendBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LendingActivity.class));
        });
        borrowBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BorrowingActivity.class));
        });
    }
}
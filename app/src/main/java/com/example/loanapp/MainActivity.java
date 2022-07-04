package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    Button lendbtn;
    Button borrowbtn;
    TextView welcome_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        borrowbtn = findViewById(R.id.borrowbtn);
        lendbtn = findViewById(R.id.lendbtn);
        welcome_text = findViewById(R.id.welcome_text);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            welcome_text.setText("Welcome to LendMe, "+currentUser.getUsername()+ " !");
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
package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button navigateSignup;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(LoginActivity.this);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            goToMain();
        }
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);
        navigateSignup = findViewById(R.id.navigatesignup);


        loginButton.setOnClickListener(v -> login(username.getText().toString(), password.getText().toString()));

        navigateSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }
    private void login(String username, String password) {
        progressDialog.show();
        ParseUser.logInInBackground(username, password, (parseUser, e) -> {
            progressDialog.dismiss();
            if (parseUser != null) {
                showAlert("Successful Login", "Welcome back " + username + " !");
            } else {
                ParseUser.logOut();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
    private void goToMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}
package com.example.loanapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class PaymentActivity extends AppCompatActivity {
    Button back_home_btn,to_confirm_btn;
    String value_from_activity = "-1";
    EditText expiry_date,card_number,cvv_txt;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Bundle extras = getIntent().getExtras();

        back_home_btn = findViewById(R.id.back_home_btn);
        to_confirm_btn = findViewById(R.id.to_confirm_btn);
        cvv_txt = findViewById(R.id.cvv_txt);
        card_number = findViewById(R.id.card_number);
        expiry_date = findViewById(R.id.expiry_date);
        progressDialog = new ProgressDialog(PaymentActivity.this);

        if (extras != null) {
            String value = extras.getString("key");
            value_from_activity = value;
        }
        if(value_from_activity !="-1"){
            to_confirm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveCard();

                }
            });
        }
        back_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

    }
    public void saveCard(){
        ParseObject item = new ParseObject("card");
        if (cvv_txt.getText().toString().length() != 0 && card_number.getText().toString().length() != 0 && card_number.getText().toString().length() != 0 ) {

            progressDialog.show();
            item.put("cvv", cvv_txt.getText().toString());
            item.put("card_number", card_number.getText().toString());
            item.put("expiry_date", expiry_date.getText().toString());
            item.put("for_item",value_from_activity);

            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {

                item.put("owner",currentUser.getObjectId());
            }
            item.saveInBackground(e -> {
                progressDialog.dismiss();
                if (e == null) {
                    //We saved the object and fetching data again
                    showAlert("Success","Card saved Successfully");
                    Intent i = new Intent(PaymentActivity.this,ConfirmationActivity.class);
        i.putExtra("key",value_from_activity);
        startActivity(i);
                } else {
                    //We have an error.We are showing error message here.
                    showAlert("Error", e.getMessage());
                }
            });
        }else{
            showAlert("Error", "Please cvv,card number and Expiry date");
        }
//        Intent i = new Intent(PaymentActivity.this,ConfirmationActivity.class);
//        i.putExtra("key",value_from_activity);
//        startActivity(i);
        }
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(PaymentActivity.this, PaymentActivity.class);
                    intent.putExtra("key",value_from_activity);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
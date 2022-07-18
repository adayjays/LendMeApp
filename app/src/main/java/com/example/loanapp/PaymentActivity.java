package com.example.loanapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class PaymentActivity extends AppCompatActivity {
    Button backHomeBtn, toConfirmBtn;
    String valueFromActivity = "-1";
    EditText expiryDate, cardNumber, cvvTxt;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Bundle extras = getIntent().getExtras();

        backHomeBtn = findViewById(R.id.back_home_btn);
        toConfirmBtn = findViewById(R.id.to_confirm_btn);
        cvvTxt = findViewById(R.id.cvv_txt);
        cardNumber = findViewById(R.id.card_number);
        expiryDate = findViewById(R.id.expiry_date);
        progressDialog = new ProgressDialog(PaymentActivity.this);

        if (extras != null) {
            String value = extras.getString("key");
            valueFromActivity = value;
        }
        if(valueFromActivity !="-1"){
            toConfirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveCard();

                }
            });
        }
        backHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

    }
    public void saveCard(){
        ParseObject item = new ParseObject("card");
        if (cvvTxt.getText().toString().length() != 0 && cardNumber.getText().toString().length() != 0 && cardNumber.getText().toString().length() != 0 ) {

            progressDialog.show();
            item.put("cvv", cvvTxt.getText().toString());
            item.put("card_number", cardNumber.getText().toString());
            item.put("expiry_date", expiryDate.getText().toString());
            item.put("for_item", valueFromActivity);

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
        i.putExtra("key", valueFromActivity);
        startActivity(i);
                } else {
                    //We have an error.We are showing error message here.
                    showAlert("Error", e.getMessage());
                }
            });
        }else{
            showAlert("Error", "Please cvv,card number and Expiry date");
        }

        }
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(PaymentActivity.this, PaymentActivity.class);
                    intent.putExtra("key", valueFromActivity);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConfirmationActivity extends AppCompatActivity {
    Boolean is_available = false;
    TextView thank_you_msg,you_msg,notf_msg;
    Button homeBtn,confirm;
    String value_key = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        thank_you_msg = findViewById(R.id.thank_you_msg);
        you_msg = findViewById(R.id.you_msg);
        notf_msg = findViewById(R.id.notf_msg);
        homeBtn = findViewById(R.id.button_home);
        confirm = findViewById(R.id.confirm_btn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ConfirmationActivity.this, MainActivity.class);
                startActivity(in);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem(value_key);
                Toast t = Toast.makeText(ConfirmationActivity.this, "Borrow Confirmed !", Toast.LENGTH_SHORT);
                t.show();
                Intent in = new Intent(ConfirmationActivity.this, MainActivity.class);
                startActivity(in);
            }
        });
        Bundle extras = getIntent().getExtras();
//        Log.e(extras.getString("key"));
        if (extras != null) {
            String value = extras.getString("key");
            value_key = value;
            is_available = true;
            getData(value);
            //The key argument here must match that used in the other activity
        }
    }

    public void getData(String key){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("items");
// Query parameters based on the item name
        query.whereEqualTo("objectId", key);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject player, ParseException e) {
                if (e == null) {
                    is_available = true;
                    String title = player.getString("title");
                    String username = "Sara";
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        username = currentUser.getUsername();
                    }
                    String new_thank_you_msg  = "Thank you for using LendMe,"+ username +"! ";
                    Date today = new Date();
                    SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, 1);  // number of days to add
                    String tomorrow = (String)(formattedDate.format(c.getTime()));

                    String new_you_msg = "You and the lender will meet at the agreed location on "+tomorrow;
                    c.add(Calendar.DATE,6);
                    String new_notf_msg = "You must return '"+ title +"' by "+ (String)formattedDate.format(c.getTime()) +" to avoid late fees.";
                    thank_you_msg.setText(new_thank_you_msg);
                    you_msg.setText(new_you_msg);
                    notf_msg.setText(new_notf_msg);

                } else {
                    // Something is wrong
                }
            }
        });
    }
    private void updateItem(String key){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("items");
// Retrieve the object by id
        query.getInBackground(key, new GetCallback<ParseObject>() {
            public void done(ParseObject player, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    player.put("is_borrowable", 0);
                    player.saveInBackground();
                } else {
                    // Failed
                }
            }
        });
    }

}
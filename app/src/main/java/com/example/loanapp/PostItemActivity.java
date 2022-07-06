package com.example.loanapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PostItemActivity extends AppCompatActivity {

    EditText title;
    EditText image_url;
    EditText description;
    EditText availability;
    EditText price;
    Button submit;
    Spinner category;
    private FloatingActionButton openInputPopupDialogButton;
    private RecyclerView recyclerView;
    private TextView empty_text;
    private FusedLocationProviderClient fusedLocationClient;
    private Location loc;

    private ProgressDialog progressDialog;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        availability = findViewById(R.id.availability);
        submit = findViewById(R.id.submit);
        category = findViewById(R.id.category);
        price = findViewById(R.id.price);
        image_url = findViewById(R.id.image_url);
        progressDialog = new ProgressDialog(PostItemActivity.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        loc = null;

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            loc = null;
                        }else{
                            loc = location;
                        }
                    }
                });

        String[] items = new String[]{"Books", "Outdoor supplies", "Technology","Household Items","clothing/Jewelry", "Miscellaneous"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        category.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveTodo();
            }
        });
    }
    private void saveTodo() {
        ParseObject todo = new ParseObject("items");
        if (title.getText().toString().length() != 0 && description.getText().toString().length() != 0 &&(URLUtil.isHttpUrl(image_url.getText().toString()) || URLUtil.isHttpsUrl(image_url.getText().toString()))) {

            progressDialog.show();
            todo.put("title", title.getText().toString());
            todo.put("description", description.getText().toString());
            todo.put("availability", availability.getText().toString());
            todo.put("price",price.getText().toString());
            todo.put("image_url",image_url.getText().toString());
            todo.put("seller_loc",loc.toString());
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {

                todo.put("posted_by",currentUser.getObjectId());
            }
            todo.saveInBackground(e -> {
                progressDialog.dismiss();
                if (e == null) {
                    //We saved the object and fetching data again
                    showAlert("Success","Item saved Successfully");
                } else {
                    //We have an error.We are showing error message here.
                    showAlert("Error", e.getMessage());
                }
            });
        } else {
            showAlert("Error", "Please enter a title and description");
        }
    }
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PostItemActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(PostItemActivity.this, LendingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
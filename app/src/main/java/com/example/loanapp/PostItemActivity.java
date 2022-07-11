package com.example.loanapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private FusedLocationProviderClient fusedLocationClient;
    private Location loc;
    private String cat;

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
        cat = "Books";

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

        String[] itemsCategories = new String[]{"Books", "Outdoor supplies", "Technology","Household Items","clothing/Jewelry", "Miscellaneous"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsCategories);

        category.setAdapter(adapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               @Override
                                               public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                   cat = category.getItemAtPosition(i).toString();
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> adapterView) {

                                               }
                                           });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        savePostItem();
                    }
                });
    }
    private void savePostItem() {
        ParseObject item = new ParseObject("items");
        if (title.getText().toString().length() != 0 && description.getText().toString().length() != 0 &&(URLUtil.isHttpUrl(image_url.getText().toString()) || URLUtil.isHttpsUrl(image_url.getText().toString()))) {

            progressDialog.show();
            item.put("title", title.getText().toString());
            item.put("description", description.getText().toString());
            item.put("availability", availability.getText().toString());
            item.put("price",price.getText().toString());
            item.put("image_url", image_url.getText().toString());
            if (loc == null){
                item.put("seller_loc", "no location data found");
            }else {
                item.put("seller_loc",loc.toString());
            }
            item.put("category",cat);
            item.put("is_borrowable",1);
            ParseUser currentUser = ParseUser.getCurrentUser();
            item.put("posted_by",currentUser.getObjectId());

            item.saveInBackground(e -> {
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
            showAlert("Error", "Please enter a title, image_ur should contain http or htpps and description");
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
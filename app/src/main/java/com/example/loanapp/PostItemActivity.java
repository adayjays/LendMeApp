package com.example.loanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PostItemActivity extends AppCompatActivity implements LocationListener {

    EditText title;
    EditText imageUrl;
    EditText description;
    EditText availability;
    EditText price;
    Button submitButton;
    Spinner categorySpinner;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    private Location location;
    private String category;
    private String locationLatLong ="";

    private ProgressDialog progressDialog;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        availability = findViewById(R.id.availability);
        submitButton = findViewById(R.id.submit);
        categorySpinner = findViewById(R.id.category);
        price = findViewById(R.id.price);
        imageUrl = findViewById(R.id.image_url);
        progressDialog = new ProgressDialog(PostItemActivity.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        location = null;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
        }

        category = "Books";

        String[] items = new String[]{"Books", "Outdoor supplies", "Technology","Household Items","clothing/Jewelry", "Miscellaneous"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               @Override
                                               public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                   category = categorySpinner.getItemAtPosition(i).toString();
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> adapterView) {

                                               }
                                           });

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        saveTodo();
                    }
                });
    }
    private void saveTodo() {
        ParseObject item = new ParseObject("items");
        GetLocation getLocation = new GetLocation(PostItemActivity.this);
        if (title.getText().toString().length() != 0 && description.getText().toString().length() != 0 &&(URLUtil.isHttpUrl(imageUrl.getText().toString()) || URLUtil.isHttpsUrl(imageUrl.getText().toString()))) {

            progressDialog.show();
            item.put("title", title.getText().toString());
            item.put("description", description.getText().toString());
            item.put("availability", availability.getText().toString());
            item.put("price",price.getText().toString());
            item.put("image_url", imageUrl.getText().toString());
            if (locationLatLong == ""){
                item.put("seller_loc", "no location data found");
            }else {
                String[]locationLatLongArray = locationLatLong.split(",");
                double latitude = Double.parseDouble(locationLatLongArray[0]);
                double longitude = Double.parseDouble(locationLatLongArray[1]);

                item.put("seller_loc",getLocation.getAddresPlain(latitude,longitude));
            }
            item.put("categorySpinner", category);
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
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        locationLatLong = latitude+","+longitude;

    }
}
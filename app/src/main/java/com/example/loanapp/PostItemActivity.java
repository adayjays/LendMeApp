package com.example.loanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PostItemActivity extends AppCompatActivity implements LocationListener {

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
    private LocationManager locationManager;
    private Location loc;
    private String cat;
    private String location_lat_long="";

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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(loc);
        }

        cat = "Books";


        String[] items = new String[]{"Books", "Outdoor supplies", "Technology","Household Items","clothing/Jewelry", "Miscellaneous"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

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

                        saveTodo();
                    }
                });
    }
    private void saveTodo() {
        ParseObject item = new ParseObject("items");
        GetLocation getLocation = new GetLocation(PostItemActivity.this);
        if (title.getText().toString().length() != 0 && description.getText().toString().length() != 0 &&(URLUtil.isHttpUrl(image_url.getText().toString()) || URLUtil.isHttpsUrl(image_url.getText().toString()))) {

            progressDialog.show();
            item.put("title", title.getText().toString());
            item.put("description", description.getText().toString());
            item.put("availability", availability.getText().toString());
            item.put("price",price.getText().toString());
            item.put("image_url", image_url.getText().toString());
            if (location_lat_long == ""){
                item.put("seller_loc", "no location data found");
            }else {
                String[]location_lat_long_array = location_lat_long.split(",");
                double lat = Double.parseDouble(location_lat_long_array[0]);
                double lng = Double.parseDouble(location_lat_long_array[1]);

                item.put("seller_loc",getLocation.getAddresPlain(lat,lng));
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        location_lat_long = latitude+","+longitude;

    }
}
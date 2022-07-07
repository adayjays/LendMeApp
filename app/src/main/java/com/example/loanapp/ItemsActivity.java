package com.example.loanapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ItemsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
//    private TextView empty_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        recyclerView = findViewById(R.id.recyclerView);
//        empty_text = findViewById(R.id.empty_text);

        Bundle b = getIntent().getExtras();
        String value = ""; // or other values
        if(b != null) {
            value = b.getString("cat");
        }
        getItemList(value);

    }
    private void getItemList(String val) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("items");
        //We use this code to fetch data from newest to oldest.
        if (val != "") {
            query.whereEqualTo("category", val);
        }
        query.whereEqualTo("is_borrowable",1);
        query.orderByDescending("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {

                initTodoList(objects);
            } else {
                showAlert("Error", e.getMessage());
            }
        });
    }


    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemsActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(ItemsActivity.this, ItemsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
    private void initTodoList(List<ParseObject> list) {
        if (list == null || list.isEmpty()) {
//            empty_text.setVisibility(View.VISIBLE);
            return;
        }
//        empty_text.setVisibility(View.GONE);

        ItemAdapter adapter = new ItemAdapter(list, this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}

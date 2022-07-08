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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Locale;

public class ItemsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private static RecyclerViewClickListener itemListener;

    private RecyclerView recyclerView;
    private TextView category_type;
//    private TextView empty_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        recyclerView = findViewById(R.id.recyclerView);
        category_type = findViewById(R.id.category_type);
//        empty_text = findViewById(R.id.empty_text);

        Bundle b = getIntent().getExtras();
        String value = ""; // or other values
        if(b != null) {
            value = b.getString("cat");
//            set it as title of the page
            category_type.setText(value.toUpperCase(Locale.ROOT));

        }
        getItemList(value);

        itemListener = new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                Toast.makeText(ItemsActivity.this,"Position is",Toast.LENGTH_LONG).show();
            }

        };

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
//    @Override
//    public void recyclerViewListClicked(View v, int position){
//
//    }


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

        ItemAdapter adapter = new ItemAdapter(list, ItemsActivity.this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}

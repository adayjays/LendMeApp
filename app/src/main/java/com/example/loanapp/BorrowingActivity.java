package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BorrowingActivity extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowing);
        String[] itemsCategories = new String[]{"Books", "Outdoor supplies", "Technology", "Household Items", "clothing/Jewelry", "Miscellaneous"};

        listView = (ListView) findViewById(R.id.my_list_view);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                itemsCategories);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(BorrowingActivity.this, ItemsActivity.class);
                intent.putExtra("cat",itemsCategories[position]);
                startActivity(intent);

            }

        });
    }
}
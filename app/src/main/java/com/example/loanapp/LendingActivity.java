package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LendingActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lending);
        String[] itemsCategories = new String[]{"Books", "Outdoor supplies", "Technology", "Household Items", "clothing/Jewelry", "Miscellaneous"};

        listView = (ListView) findViewById(R.id.my_list_view2);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                itemsCategories);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(LendingActivity.this, PostItemActivity.class);
//                intent.putExtra("cat",itemsCategories[position]);
                startActivity(intent);

            }

        });
    }
}
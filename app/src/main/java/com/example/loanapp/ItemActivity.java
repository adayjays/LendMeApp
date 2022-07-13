package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ItemActivity extends AppCompatActivity {
    TextView desc_txt, title_txt,other_txt;
    ImageView imageView3;
    Button borrow_btn,chat;
    boolean is_available = false;
    String object_id = "1";
    String owner_id = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        desc_txt = findViewById(R.id.desc_txt);
        imageView3 = findViewById(R.id.imageView3);
        title_txt = findViewById(R.id.title_txt);
        borrow_btn = findViewById(R.id.borrow_btn);
        other_txt = findViewById(R.id.other_txt);
        chat = findViewById(R.id.chat_btn);
        Bundle extras = getIntent().getExtras();
//        Log.e(extras.getString("key"));
        if (extras != null) {
            String value = extras.getString("key");
            object_id = value;

            is_available = true;
            getData(value);
            //The key argument here must match that used in the other activity
        }
        if (is_available){
            borrow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ItemActivity.this,PaymentActivity.class);
                    i.putExtra("key",object_id);
                    startActivity(i);
                }
            });
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ItemActivity.this,ChatActivity.class);
                    String extra = owner_id+","+object_id;
                    intent.putExtra("key",extra);
                    startActivity(intent);
                }
            });
        }
    }
    public void getData(String key){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("items");
// Query parameters based on the item name
        query.whereEqualTo("objectId", key);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject item, ParseException e) {
                if (e == null) {
                    is_available = true;
                    String desc = item.getString("description");
                    String title = item.getString("title");
                    String img =  item.getString("image_url");
                    owner_id = item.getString("posted_by");
                    String new_other_txt = "$"+item.getString("price")+"/day\n Available "+item.getString("price")+"\nLocal only";
                    desc_txt.setText(desc);
                    title_txt.setText(title);
                    imageView3.setImageBitmap(getBitmapFromURL(img));
                    other_txt.setText(new_other_txt);
                } else {
                    // Something is wrong
                }
            }
        });
    }
    public static Bitmap getBitmapFromURL(String src) {
        if(URLUtil.isHttpUrl(src) || URLUtil.isHttpsUrl(src)) {
            try {
                Log.e("src", src);
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Log.e("Bitmap", "returned");
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Exception", e.getMessage());
                return null;
            }
        }else {
            return null;
        }
    }

}
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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ItemActivity extends AppCompatActivity {
    TextView descTxt, titleTxt, otherTxt;
    ImageView imageView3;
    Button borrowBtn,chat;
    boolean isAvailable = false;
    String objectId = "1";
    String ownerId = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        descTxt = findViewById(R.id.desc_txt);
        imageView3 = findViewById(R.id.imageView3);
        titleTxt = findViewById(R.id.title_txt);
        borrowBtn = findViewById(R.id.borrow_btn);
        otherTxt = findViewById(R.id.other_txt);
        chat = findViewById(R.id.chat_btn);
        Bundle extras = getIntent().getExtras();
//        Log.e(extras.getString("key"));
        if (extras != null) {
            String value = extras.getString("key");
            objectId = value;

            isAvailable = true;
            getData(value);
            //The key argument here must match that used in the other activity
        }
        if (isAvailable){
            borrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ItemActivity.this,PaymentActivity.class);
                    i.putExtra("key", objectId);
                    startActivity(i);
                }
            });
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ItemActivity.this,ChatActivity.class);
                    String extra = ownerId +","+ objectId;
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
                    isAvailable = true;
                    String desc = item.getString("description");
                    String title = item.getString("title");
                    String img =  item.getString("image_url");
                    ownerId = item.getString("posted_by");
                    String new_other_txt = "$"+item.getString("price")+"/day\n Available "+item.getString("price")+"\nLocal only";
                    descTxt.setText(desc);
                    titleTxt.setText(title);
                    imageView3.setImageBitmap(getBitmapFromURL(img));
                    otherTxt.setText(new_other_txt);
                } else {
                    // Something is wrong
                }
            }
        });
    }
    public static Bitmap getBitmapFromURL(String src) {
        if(URLUtil.isHttpUrl(src) || URLUtil.isHttpsUrl(src)) {
            try {
//                Log.e("src", src);
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                Log.e("Bitmap", "returned");
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
//                Log.e("Exception", e.getMessage());
                return null;
            }
        }else {
            return null;
        }
    }

}
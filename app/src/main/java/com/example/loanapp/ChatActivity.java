package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    Button send;
    EditText msg;
    String itemId, chatId, userId, sellerId;
    RecyclerView recyclerView;
    RecyclerViewClickListener recyclerViewClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        msg = findViewById(R.id.msg_input);
        send = findViewById(R.id.send_btn);
        recyclerView = findViewById(R.id.recycler_chat);

        itemId ="";
        chatId = getChatId();
        userId = "";
        sellerId ="";
        ParseUser currentUser = ParseUser.getCurrentUser();
        userId =currentUser.getObjectId();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
//            owner_id+","+object_id
            String[] valueArray = value.split(",");
            itemId = valueArray[1];
            sellerId = valueArray[0];

        }else{
            showToast("Empty key");

        }
        if (itemId.equals("") && sellerId.equals("") && userId.equals("") ){

        }else {
            chatId = getChatId();
            getChats();
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("sending...");
                sendMessage();
                getChats();
            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // do something every 5 seconds

                getChats();
            }
        }, 5000);
    }

    private void getChats() {
        readChat();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("chats");

// The query will resolve only after calling this method
        query.whereEqualTo("seller", sellerId);
        query.whereEqualTo("item", itemId);
        query.whereEqualTo("possible_buyer", userId);
        query.findInBackground((objects, e) -> {
            if (e == null) {

                initChatList(objects);
            } else {
                showToast("Error !"+ e.getMessage());
            }
        });
    }

    private void initChatList(List<ParseObject> list) {
        if (list == null || list.isEmpty()) {
//            empty_text.setVisibility(View.VISIBLE);
            return;
        }
//        empty_text.setVisibility(View.GONE);

        ChatAdapter adapter = new ChatAdapter(list, ChatActivity.this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void sendMessage(){

        ParseObject msg_item =new ParseObject("messages");
        if (msg.getText().toString().trim().length() > 0){
            if(chatId.trim().length() == 0){
                msg_item.put("seller", sellerId);
                msg_item.put("item",itemId);
                msg_item.put("possible_buyer", userId);
                msg_item.saveInBackground(e -> {
                    if (e == null){
                       chatId = getChatId();
                        saveChat();
                    }else {
                        showToast("Error ! : "+ e.getMessage());
                    }
                });
            }else{
                saveChat();
            }

        }else{
            showToast("Empty Text");
        }
    }
    public void createMessageItem(){
        ParseObject msg_item =new ParseObject("messages");
        if(chatId.trim().length() == 0){
            msg_item.put("seller", sellerId);
            msg_item.put("item",itemId);
            msg_item.put("possible_buyer", userId);
            msg_item.saveInBackground(e -> {
                if (e == null){
                    chatId = getChatId();

                }else {
                    showToast("Error ! : "+ e.getMessage());
                }
            });
        }

    }
    public void saveChat(){
        ParseObject chat = new ParseObject("chats");
            chat.put("message", msg.getText().toString());
            chat.put("seller", sellerId);
            chat.put("item", itemId);
            chat.put("possible_buyer", userId);
            chat.put("chat_id", chatId);
        chat.put("is_read","sent");
        chat.put("sender", userId);

            chat.saveInBackground(e -> {
                if (e == null) {
                    //We saved the object and fetching data again
                    showToast("Sent");
                    msg.setText("");
                } else {
                    //We have an error.We are showing error message here.
                    showToast("Error !"+ e.getMessage());
                }
            });
    }
    public String getChatId(){
        final String[] ret = {""};
        ParseQuery<ParseObject> query = ParseQuery.getQuery("messages");
        query.whereEqualTo("seller", sellerId);
        query.whereEqualTo("item", itemId);
        query.whereEqualTo("possible_buyer", userId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject player, ParseException e) {
                if (e == null) {
                    ret[0] = player.getObjectId();
                } else {
                    // Something is wrong
                    createMessageItem();
                    showToast("Starting new Conversation");
                }
            }
        });
        return ret[0];
    }

    private void showToast(String msg) {
        Toast toast = new Toast(ChatActivity.this);
//        toast.setText(msg);
        toast.makeText(ChatActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
    //change chat status to read if the current user_id is not same as the sender
    public void readChat(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("chats");

// The query will resolve only after calling this method
        query.whereEqualTo("seller", sellerId);
        query.whereEqualTo("item", itemId);
        query.whereEqualTo("possible_buyer", userId);
        query.whereEqualTo("is_read", "sent");
        query.whereNotEqualTo("sender", userId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    // if the error is null then we are getting
                    // our object id in below line.
                    String objectID = object.getObjectId().toString();
                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                object.put("is_read", "read");
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {

                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
    }

}
package com.example.loanapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {


    private List<ParseObject> list;
    private Context context;

    public MutableLiveData<ParseObject> onEditListener = new MutableLiveData<>();
    public MutableLiveData<ParseObject> onDeleteListener = new MutableLiveData<>();

    public ChatAdapter(List<ParseObject> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        return new ChatHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, @SuppressLint("RecyclerView") final int position) {
        ParseObject object = list.get(position);
        ParseUser currentUser = ParseUser.getCurrentUser();
        String user_id =currentUser.getObjectId();
        String person = "Lender";
        if(user_id == object.getString("sender")){
            person = "Me";
        }
        holder.title.setText(person+"  : "+object.getString("message"));
        holder.msg_status.setText(object.getString("is_read"));
//        holder.description.setText(object.getString("description"));

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}


class ChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView title;
    TextView msg_status;


    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.msg_body);
        msg_status = itemView.findViewById(R.id.msg_status);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }
}

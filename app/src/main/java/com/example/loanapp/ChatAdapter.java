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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ItemHolder> {


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
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_item,parent,false);
        return new ItemHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, @SuppressLint("RecyclerView") final int position) {
        ParseObject object = list.get(position);
        holder.title.setText(object.getString("message"));
//        holder.description.setText(object.getString("description"));

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}


class ChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView title;


    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.msg_body);
//        description = itemView.findViewById(R.id.description);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }
}

package com.example.loanapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {


    private List<ParseObject> list;
    private Context context;

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
        String userObjectId =currentUser.getObjectId();
        String person = "Lender";
        if(userObjectId == object.getString("sender")){
            person = "Me";
        }
        holder.title.setText(person+"  : "+object.getString("message"));
        holder.messageStatus.setText(object.getString("is_read"));

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}


class ChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView title;
    TextView messageStatus;


    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.msg_body);
        messageStatus = itemView.findViewById(R.id.msg_status);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }
}

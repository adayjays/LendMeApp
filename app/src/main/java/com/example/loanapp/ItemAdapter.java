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

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

    private static RecyclerViewClickListener itemListener;

    private List<ParseObject> list;
    private Context context;

    public MutableLiveData<ParseObject> onEditListener = new MutableLiveData<>();
    public MutableLiveData<ParseObject> onDeleteListener = new MutableLiveData<>();

    public ItemAdapter(List<ParseObject> list, Context context) {
        this.list = list;
        this.context = context;
        this.itemListener = itemListener;

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
        holder.title.setText(object.getString("title") + " $"+object.getString("price")+" /day");
        holder.location.setText("located at : "+object.getString("seller_loc"));
        holder.image.setImageBitmap(getBitmapFromURL(object.getString("image_url")));
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                itemListener.recyclerViewListClicked(v,position);
                final Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("key",object.getObjectId());
                context.startActivity(intent);
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

    @Override
    public int getItemCount() {
        return list.size();
    }
}


class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView title;
    TextView location;
    ImageView image;


    public ItemHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        location = itemView.findViewById(R.id.location);
        image = itemView.findViewById(R.id.image_view);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }
}

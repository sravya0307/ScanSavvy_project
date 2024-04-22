package com.example.qrscanner;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewHolder>
{

    ArrayList<SearchRes>data;
    public myadapter(ArrayList<SearchRes> data){
        this.data=data;
    }
    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlexml,parent,false);
        return new myviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewHolder holder, int position) {
        String title=data.get(position).getTitle();
        if(title!=null) {
            holder.t1.setText(title);
        }
        String displaylink=data.get(position).getDisplayLink();
        if(displaylink!=null) {
            holder.t2.setText(displaylink);
        }
        List<CseImage> cseImages = data.get(position).getPageMap().getCseImage();
        if(cseImages!=null) {
            String imageUrl = cseImages.get(0).getSrc();
            Glide.with(holder.t1.getContext()).load(imageUrl).into(holder.img);
        }
        String link=data.get(position).getLink();
        if(link!=null){
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = link.toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    view.getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myviewHolder extends RecyclerView.ViewHolder{
         ImageView img;
        LinearLayout linearLayout;
         TextView t1,t2;
        public myviewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearlayout);
            img=itemView.findViewById(R.id.img);
            t1=itemView.findViewById(R.id.t1);
            t2=itemView.findViewById(R.id.t2);
        }
    }
}

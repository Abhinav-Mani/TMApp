package com.maxpetroleum.tmapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxpetroleum.tmapp.Model.SalesOfficer;
import com.maxpetroleum.tmapp.R;

import java.util.ArrayList;

public class SoListAdapter extends RecyclerView.Adapter<SoListAdapter.MyViewHolder> {
    ArrayList<SalesOfficer> list;
    ClickHandler clickHandler;

    public SoListAdapter(ArrayList<SalesOfficer> list,ClickHandler clickHandler)
    {
        this.list = list;
        this.clickHandler=clickHandler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_so_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        SalesOfficer salesOfficer=list.get(position);
        holder.name.setText("Name: "+salesOfficer.getName());
        holder.email.setText("Email: "+salesOfficer.getEmail());
        holder.uid.setText("UID: "+salesOfficer.getUid());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.itemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ClickHandler{
        public void itemClicked(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,email,uid;
        LinearLayout item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            email=itemView.findViewById(R.id.email);
            uid=itemView.findViewById(R.id.Uid);
            item=itemView.findViewById(R.id.item);
        }
    }
}

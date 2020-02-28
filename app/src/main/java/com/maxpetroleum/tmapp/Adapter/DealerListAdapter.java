package com.maxpetroleum.tmapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.maxpetroleum.tmapp.Model.Dealer;
import com.maxpetroleum.tmapp.R;

import java.util.ArrayList;

public class DealerListAdapter extends RecyclerView.Adapter<DealerListAdapter.MyVieHolder>{
    ArrayList<Dealer> list;

    public DealerListAdapter(ArrayList<Dealer> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_dealer,parent,false);
        MyVieHolder myVieHolder=new MyVieHolder(view);
        return myVieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, final int position) {
        Dealer dealer=list.get(position);
        holder.dealerCode.setText(dealer.getUid());
        holder.pendingRequests.setText(dealer.getPendingRequests()+"");
        if(dealer.getPendingRequests()==0)
            holder.pendingRequests.setVisibility(View.GONE);
        else
            holder.pendingRequests.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ClickHandler{
        public void itemClicked(int position);
    }

    public static class MyVieHolder extends RecyclerView.ViewHolder {
        TextView dealerCode,pendingRequests;
        LinearLayout item;
        public MyVieHolder(@NonNull View itemView) {
            super(itemView);
            dealerCode=itemView.findViewById(R.id.dealerCode);
            pendingRequests=itemView.findViewById(R.id.pendingRequests);
            item=itemView.findViewById(R.id.SingleDealer);
        }
    }
}

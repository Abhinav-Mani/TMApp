package com.maxpetroleum.tmapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.tmapp.Adapter.PoListAdapter;
import com.maxpetroleum.tmapp.Model.PO_Info;
import com.maxpetroleum.tmapp.R;

import java.util.ArrayList;

public class PoList extends AppCompatActivity implements PoListAdapter.ClickHandler {

    RecyclerView recyclerView;
    PoListAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<PO_Info> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polist);

        init();

        fetch();
    }

    private void fetch() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    PO_Info po_info = dataSnapshot1.getValue(PO_Info.class);
                    list.add(po_info);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        list=new ArrayList<>();
        recyclerView=findViewById(R.id.PoList);
        adapter=new PoListAdapter(list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("PO");
    }

    @Override
    public void onItemClicked(PO_Info po_info) {
        Intent intent=new Intent(this,PoDetail.class);
        intent.putExtra("Data",po_info);
        startActivity(intent);
    }
}

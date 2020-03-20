package com.maxpetroleum.tmapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.tmapp.Adapter.SoListAdapter;
import com.maxpetroleum.tmapp.Model.SalesOfficer;
import com.maxpetroleum.tmapp.R;

import java.util.ArrayList;

public class SoList extends AppCompatActivity implements SoListAdapter.ClickHandler {

    RecyclerView recyclerView;
    private static SoListAdapter adapter;
    private static ArrayList<SalesOfficer> list;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    public static void removefromList(SalesOfficer salesOfficer){
        String uid=salesOfficer.getUid();
        for (int i=0;i<list.size();i++){
            SalesOfficer so=list.get(i);
            if(so.getUid().equalsIgnoreCase(uid)){
                list.remove(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so_list);

        init();

        fetch();
    }

    private void fetch() {
        reference.child("UserData").child("Sales officer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    SalesOfficer officer = dataSnapshot1.getValue(SalesOfficer.class);
                    officer.setUid(dataSnapshot1.getKey());
                    list.add(officer);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void init() {
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        recyclerView=findViewById(R.id.SoList);
        list=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SoListAdapter(list,SoList.this);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void itemClicked(int position) {
        Intent intent=new Intent(this,SODetails.class);
        intent.putExtra("Data",list.get(position));
        startActivity(intent);
    }
}

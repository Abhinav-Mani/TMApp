package com.maxpetroleum.tmapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.tmapp.Adapter.DealerListAdapter;
import com.maxpetroleum.tmapp.Model.Dealer;
import com.maxpetroleum.tmapp.Model.SalesOfficer;
import com.maxpetroleum.tmapp.R;

import java.util.ArrayList;
import java.util.Collections;

public class SODetails extends AppCompatActivity implements DealerListAdapter.ClickHandler,View.OnClickListener {
    TextView name,email,password,uid;
    SalesOfficer officer;
    Button addDealer;
    RecyclerView recyclerView;
    DealerListAdapter adapter;
    ArrayList<Dealer> list;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sodetails);

        check();

        init();

        setListeners();

        setValues();

        fetch();
    }

    private void fetch() {
         myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Dealer dealer=new Dealer(dataSnapshot1.getKey(),(Long) dataSnapshot1.getValue());
                    list.add(dealer);
                }
                Collections.sort(list,Collections.<Dealer>reverseOrder());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter.notifyDataSetChanged();

    }

    private void setListeners() {
        addDealer.setOnClickListener(this);
    }

    private void setValues() {
        name.setText    ("Name: "+officer.getName());
        password.setText("Password: "+officer.getPassword());
        uid.setText     ("Uid: "+officer.getUid());
        email.setText   ("Email: "+officer.getEmail());
    }

    private void check() {
        Intent intent=getIntent();
        if(intent.hasExtra("Data")){
            officer=(SalesOfficer) intent.getSerializableExtra("Data");
        }
    }

    private void init() {
        list=new ArrayList<>();
        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        uid=findViewById(R.id.Uid);

        recyclerView=findViewById(R.id.poList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new DealerListAdapter(list,this);
        recyclerView.setAdapter(adapter);

        addDealer=findViewById(R.id.addDealer);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("SO").child(officer.getUid());

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view==addDealer){
            Intent intent=new Intent(this,AddDealer.class);
            intent.putExtra("Data",officer);
            startActivity(intent);
        }
    }

    @Override
    public void itemClicked(int position) {
        Dealer dealer=list.get(position);
        Intent intent=new Intent(SODetails.this,PoList.class);
        intent.putExtra("Data",dealer);
        startActivity(intent);
    }
}

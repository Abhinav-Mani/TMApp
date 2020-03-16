package com.maxpetroleum.tmapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.tmapp.Adapter.PoListAdapter;
import com.maxpetroleum.tmapp.Adapter.ViewPagerAddapter;
import com.maxpetroleum.tmapp.Model.Dealer;
import com.maxpetroleum.tmapp.Model.PO_Info;
import com.maxpetroleum.tmapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PoList extends AppCompatActivity implements PoListAdapter.ClickHandler,View.OnClickListener {

    TextView name,email,password,uid;
    ViewPager viewPager;
    ViewPagerAddapter addapter;
    TabLayout tabLayout;
    public static Dealer dealer;
    public static HashMap<String,String> hashMap;
    ImageView back;
    ProgressDialog progressDialog;
    Button updateBalance;
    private TextView curBal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polist);

        check();

        init();

        setValues();
    }

    private void check() {
        Intent intent=getIntent();
        if(intent.hasExtra("Data")){
            dealer= (Dealer) intent.getSerializableExtra("Data");
        }else {
            finish();
        }

    }

    private void init() {
        viewPager=findViewById(R.id.pager);
        addapter=new ViewPagerAddapter(getSupportFragmentManager());
        viewPager.setAdapter(addapter);
        tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        back = findViewById(R.id.back);

        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        uid=findViewById(R.id.Uid);
        curBal = findViewById(R.id.currentBal);
        updateBalance = findViewById(R.id.updateBalance);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        hashMap=new HashMap<>();
        back.setOnClickListener(this);
        updateBalance.setOnClickListener(this);

    }

    private void setValues() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("UserData")
                .child("Dealer")
                .child(dealer.getUid());

        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    name.setText("Dealer Name: "+dataSnapshot.child("name").getValue().toString());
                    email.setText("Email: "+dataSnapshot.child("email").getValue().toString());
                    password.setText("Password: "+dataSnapshot.child("password").getValue().toString());
                    uid.setText("UID: "+dealer.getUid());
                    curBal.setText("Current Balance: ₹"+dataSnapshot.child("balance").getValue().toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClicked(PO_Info po_info) {
        Intent intent=new Intent(this,PoDetail.class);
        intent.putExtra("Key",hashMap.get(po_info.getPo_no()));
        intent.putExtra("Data",po_info);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v == back) finish();
        else if(v == updateBalance){
            startActivity(new Intent(this,UpdateBalance.class));
        }
    }
}

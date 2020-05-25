package com.maxpetroleum.tmapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.maxpetroleum.tmapp.Util.DeleteUser;
import com.maxpetroleum.tmapp.Util.EmailModifier;

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
    Button updateBalance,deleteDealar;
    private TextView curBal;
    String Email,Pass;
    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polist);

        check();

        init();

        addListeners();

        setValues();
    }

    private void addListeners() {
        deleteDealar.setOnClickListener(this);
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
        deleteDealar=findViewById(R.id.removeDealer);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        hashMap=new HashMap<>();
        back.setOnClickListener(this);
        updateBalance.setOnClickListener(this);

        root=FirebaseDatabase.getInstance().getReference();

    }

    private void setValues() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("UserData")
                .child("Dealer")
                .child(dealer.getUid());

        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    name.setText("Dealer Name: "+dataSnapshot.child("name").getValue().toString());
                    email.setText("Email: "+dataSnapshot.child("email").getValue().toString());
                    Email=dataSnapshot.child("email").getValue().toString();
                    password.setText("Password: "+dataSnapshot.child("password").getValue().toString());
                    Pass=dataSnapshot.child("password").getValue().toString();
                    uid.setText("UID: "+dealer.getUid());
                    if(dataSnapshot.child("balance").getValue()!=null)
                        curBal.setText("Current Balance: ₹"+dataSnapshot.child("balance").getValue().toString());
                    else
                        curBal.setText("Current Balance: ₹"+0);
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
        }else if(v==deleteDealar){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Alert");
            dialog.setMessage("Are you sure to remove this user?");
            dialog.setPositiveButton(this.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeDealer();
                }
            });
            dialog.setNegativeButton(this.getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    private void removeDealer() {
        DeleteUser.DELETE_USER(Email,Pass);
        Toast.makeText(this,"removing",Toast.LENGTH_LONG).show();
        EmailModifier modifier=new EmailModifier(Email);
        String newEmail=modifier.getEmail();
        root.child("User").child("Dealer").child(newEmail).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                root.child("UserData").child("Dealer").child(dealer.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        root.child("SO").child(SODetails.officer.getUid()).child(dealer.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                        //SoList.removefromList(officer);

                    }
                });
            }
        });
    }
}

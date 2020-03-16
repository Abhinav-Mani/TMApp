package com.maxpetroleum.tmapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.tmapp.Model.SalesOfficer;
import com.maxpetroleum.tmapp.R;

public class AddDealer extends AppCompatActivity implements View.OnClickListener {
    EditText DealerId,DealerName,Email,Password,ConfirmPassword;
    Button Submit;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    SalesOfficer officer;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dealer);

        check();

        init();

        setListeners();
    }

    private void check() {
        Intent intent=getIntent();
        if(intent.hasExtra("Data")){
            officer=(SalesOfficer)intent.getSerializableExtra("Data");
        }else {
            finish();
        }
    }

    private void setListeners() {
        Submit.setOnClickListener(this);
    }

    private void init() {
        DealerId=findViewById(R.id.DealerId);
        mAuth = FirebaseAuth.getInstance();
        DealerName=findViewById(R.id.DealerName);
        Email=findViewById(R.id.email);
        Password=findViewById(R.id.password);
        ConfirmPassword=findViewById(R.id.confirm_Password);
        Submit=findViewById(R.id.submit);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
    }

    @Override
    public void onClick(View view) {
        if(valdate()){
            progressDialog.show();
            myRef.child("UserData").child("Dealer").child(DealerId.getText().toString().trim()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.show();
                    if(dataSnapshot.getValue()==null){
                        crateUser();
                    } else {
                        Toast.makeText(AddDealer.this,"UID already Exists",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
    }

    private void crateUser() {
        String email=Email.getText().toString().trim();
        String password=Password.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.signOut();
                            progressDialog.dismiss();
                            Toast.makeText(AddDealer.this, "Dealer Added Succssfully", Toast.LENGTH_SHORT).show();
                            push();
                        } else {
                            Toast.makeText(AddDealer.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void push() {
        myRef.child("UserData").child("Dealer").child(DealerId.getText().toString().trim()).child("name").setValue(DealerName.getText().toString());
        myRef.child("UserData").child("Dealer").child(DealerId.getText().toString().trim()).child("email").setValue(Email.getText().toString());
        myRef.child("UserData").child("Dealer").child(DealerId.getText().toString().trim()).child("password").setValue(Password.getText().toString());
        myRef.child("UserData").child("Dealer").child(DealerId.getText().toString().trim()).child("so id").setValue(officer.getUid());
        myRef.child("SO").child(officer.getUid()).child(DealerId.getText().toString().trim()).setValue(0);
        String email= Email.getText().toString();
        email=email.replace('@','a');
        email=email.replace('.','d');
        email=email.toLowerCase().trim();
        myRef.child("User").child("Dealer").child(email).setValue(DealerId.getText().toString().trim());

        finish();
    }

    private boolean valdate() {
        boolean valid=true;
        if(TextUtils.isEmpty(DealerId.getText().toString().trim())){
            Toast.makeText(this,"DealerId Cannot be Empty",Toast.LENGTH_SHORT).show();
            valid=false;
        } else if(TextUtils.isEmpty(DealerName.getText().toString().trim())){
            Toast.makeText(this,"Name Cannot be Empty",Toast.LENGTH_SHORT).show();
            valid=false;
        }  else if(TextUtils.isEmpty(Email.getText().toString().trim())){
            Toast.makeText(this,"Email Cannot be Empty",Toast.LENGTH_SHORT).show();
            valid=false;
        }  else if(TextUtils.isEmpty(Password.getText().toString().trim())){
            Toast.makeText(this,"Fields Cannot be Empty",Toast.LENGTH_SHORT).show();
            valid=false;
        }  else if(TextUtils.isEmpty(ConfirmPassword.getText().toString().trim())){
            Toast.makeText(this,"Field Cannot be Empty",Toast.LENGTH_SHORT).show();
            valid=false;
        }  else if(!Password.getText().toString().equals(ConfirmPassword.getText().toString())){
            Toast.makeText(this,"Password and Confirm Password",Toast.LENGTH_SHORT).show();
            valid=false;
        }

        return valid;
    }
}

package com.maxpetroleum.tmapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.tmapp.R;



public class AddSO extends AppCompatActivity implements View.OnClickListener {

    EditText SoId,SoName,Email,Password,ConfirmPassword;
    Button Submit;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_so);

        init();

        setListeners();
    }

    private void setListeners() {
        Submit.setOnClickListener(this);
    }

    private void init() {
        SoId=findViewById(R.id.soId);
        mAuth = FirebaseAuth.getInstance();
        SoName=findViewById(R.id.soName);
        Email=findViewById(R.id.email);
        Password=findViewById(R.id.password);
        ConfirmPassword=findViewById(R.id.confirm_Password);
        Submit=findViewById(R.id.submit);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(valdate()){
            progressDialog.show();
            myRef.child("UserData").child("Sales officer").child(SoId.getText().toString().trim()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()==null){
                        crateUser();
                    } else {
                        Toast.makeText(AddSO.this,"UID already Exists",Toast.LENGTH_LONG).show();
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
                            push();
                            progressDialog.dismiss();

                            Toast.makeText(AddSO.this, "SO Added Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddSO.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void push() {
        myRef.child("UserData").child("Sales officer").child(SoId.getText().toString().trim()).child("name").setValue(SoName.getText().toString());
        myRef.child("UserData").child("Sales officer").child(SoId.getText().toString().trim()).child("email").setValue(Email.getText().toString());
        myRef.child("UserData").child("Sales officer").child(SoId.getText().toString().trim()).child("password").setValue(Password.getText().toString());
        String email= Email.getText().toString();
        email=email.replace('@','a');
        email=email.replace('.','d');
        email=email.toLowerCase().trim();
        myRef.child("User").child("Sales officer").child(email).setValue(SoId.getText().toString().trim());

        finish();
    }

    private boolean valdate() {
        boolean valid=true;
        if(TextUtils.isEmpty(SoId.getText().toString().trim())){
            Toast.makeText(this,"So Id Cannot be Empty",Toast.LENGTH_SHORT).show();
            valid=false;
        } else if(TextUtils.isEmpty(SoName.getText().toString().trim())){
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

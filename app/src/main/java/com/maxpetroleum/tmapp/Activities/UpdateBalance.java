package com.maxpetroleum.tmapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.tmapp.R;

public class UpdateBalance extends AppCompatActivity implements View.OnClickListener {

    TextView currBal;
    EditText et_bal;
    Button update;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_balance);

        init();
    }

    private void init(){
        currBal = findViewById(R.id.currentBal);
        et_bal = findViewById(R.id.et_bal);
        update = findViewById(R.id.updateBal);
        back = findViewById(R.id.back);

        update.setOnClickListener(this);
        back.setOnClickListener(this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserData").child("Dealer").child(PoList.dealer.getUid());
        ref.keepSynced(true);
        ref.child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    currBal.setText("Current Balance: ₹"+dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v == update){
            if(et_bal.getText().toString().isEmpty()){
                Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
            }else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Alert");
                dialog.setMessage("Are you sure");
                dialog.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref = ref.child("UserData").child("Dealer").child(PoList.dealer.getUid());
                        ref.child("balance").setValue(et_bal.getText().toString());
                        et_bal.setText("");
                    }
                });
                dialog.show();
            }
        }

        else if(v == back) finish();
    }
}

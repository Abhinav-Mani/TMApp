package com.maxpetroleum.tmapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maxpetroleum.tmapp.Model.GradeRate;
import com.maxpetroleum.tmapp.Model.Rate;
import com.maxpetroleum.tmapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Rates extends AppCompatActivity implements View.OnClickListener {
    EditText price1,price2,price3,price4,price5,price6;
    Button submit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    GradeRate rate;
    ArrayList<Rate> rates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        init();

        setListeners();

    }

    private void setListeners() {

        submit.setOnClickListener(this);

    }

    private void init() {

        price1=findViewById(R.id.Grade1Price);
        price2=findViewById(R.id.Grade2Price);
        price3=findViewById(R.id.Grade3Price);
        price4=findViewById(R.id.Grade4Price);
        price5=findViewById(R.id.Grade5Price);
        price6=findViewById(R.id.Grade6Price);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("GradeRate");

        rates=new ArrayList<>();

        submit=findViewById(R.id.submit);

    }

    @Override
    public void onClick(View view) {

        if(valdate()){
            databaseReference.setValue(rate);
        }

    }

    private boolean valdate() {
        boolean valid=true;
        if(TextUtils.isEmpty(price1.getText().toString().trim())){
            valid=false;
        }
        else if(TextUtils.isEmpty(price2.getText().toString().trim())){
            valid=false;
        }
        else if(TextUtils.isEmpty(price3.getText().toString().trim())){
            valid=false;
        }
        else if(TextUtils.isEmpty(price4.getText().toString().trim())){
            valid=false;
        }
        else if(TextUtils.isEmpty(price5.getText().toString().trim())){
            valid=false;
        }
        else if(TextUtils.isEmpty(price6.getText().toString().trim())){
            valid=false;
        } else{
            rates.clear();
            rates.add(new Rate("ENGINEOIL BPCL 800ML 5W30MA",Long.valueOf(price1.getText().toString().trim())));
            rates.add(new Rate("ENGINEOIL BPCL 1000ML5W30MA",Long.valueOf(price2.getText().toString().trim())));
            rates.add(new Rate("ENGINEOIL BPCL 900ML10W30MA",Long.valueOf(price3.getText().toString().trim())));
            rates.add(new Rate("ENGINEOIL BPCL 1000ML10W30MA",Long.valueOf(price4.getText().toString().trim())));
            rates.add(new Rate("ENGINEOIL BPCL 800ML10W30MA",Long.valueOf(price5.getText().toString().trim())));
            rates.add(new Rate("ENGINEOIL BPCL 800ML10W30MB",Long.valueOf(price6.getText().toString().trim())));

            Calendar calendar = Calendar.getInstance();

            String month=String.valueOf(calendar.get(calendar.MONTH));

            rate=new GradeRate(month,rates);

        }
        if(!valid){
            Toast.makeText(this,"GradeRate cannot be empty",Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
}

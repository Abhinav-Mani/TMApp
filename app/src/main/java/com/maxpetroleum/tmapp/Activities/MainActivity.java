package com.maxpetroleum.tmapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.maxpetroleum.tmapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button setRate,addSo,SOList,POList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setListers();
    }

    private void setListers() {
        setRate.setOnClickListener(this);
        addSo.setOnClickListener(this);
        SOList.setOnClickListener(this);
        POList.setOnClickListener(this);
    }

    private void init() {
        setRate=findViewById(R.id.setRate);
        addSo=findViewById(R.id.addSo);
        SOList=findViewById(R.id.SOLIST);
        POList=findViewById(R.id.POLIST);
    }

    @Override
    public void onClick(View view) {
        if(view==setRate){
            Intent intent=new Intent(this, Rates.class);
            startActivity(intent);
        } else if(view==addSo){
            Intent intent=new Intent(this,AddSO.class);
            startActivity(intent);
        } else if(view==SOList){
            Intent intent=new Intent(this,SoList.class);
            startActivity(intent);
        } else if(view==POList){
            Intent intent=new Intent(this, PoList.class);
            startActivity(intent);
        }

    }
}

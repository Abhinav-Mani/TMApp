package com.maxpetroleum.tmapp.Activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.maxpetroleum.tmapp.Model.GradeInfo;
import com.maxpetroleum.tmapp.Model.PO_Info;
import com.maxpetroleum.tmapp.R;

import java.util.ArrayList;
import java.util.Collections;


public class PoDetail extends AppCompatActivity implements View.OnClickListener {
    PO_Info po_info;
    TextView status,po_no,amount,poDate,deliveryDate,billDate,paymentDate;
    Button grades;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po_detail);

        check();

        init();

        setValues();

        setListners();
    }

    private void setListners() {
        grades.setOnClickListener(this);
    }


    private void setValues() {
        po_no.setText(po_info.getPo_no());
        amount.setText("₹"+po_info.getAmount());
        poDate.setText(po_info.getPo_Date());
        deliveryDate.setText(po_info.getDelivery_date());
        billDate.setText(po_info.getBill_date());
        paymentDate.setText(po_info.getPayment_date());

        if(po_info.getPayment_date() == null){
            status.setText("Pending\nPayment Date");
            status.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        else if(po_info.getBill_date() == null){
            status.setText("Pending\nBill Date");
            status.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else if(po_info.getDelivery_date() == null){
            status.setText("Pending\nDelivery Date");
            status.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else if(po_info.getDelivery_date() != null){
            status.setText("Deal Closed");
            status.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
    }

    private void init() {

        status=findViewById(R.id.status_txt);
        po_no=findViewById(R.id.po_no);
        amount=findViewById(R.id.amt);
        poDate=findViewById(R.id.po_date);
        deliveryDate=findViewById(R.id.delivery_date);
        billDate=findViewById(R.id.bill_date);
        grades=findViewById(R.id.view_grades_but);
        paymentDate=findViewById(R.id.payment_date);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void check() {
        Intent intent=getIntent();
        if(intent.hasExtra("Data")){
            po_info=(PO_Info) intent.getSerializableExtra("Data");
        }
        else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if(view==grades){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Grades");
            ListView listView = new ListView(this);
            QntAdapter adapter = new QntAdapter(this, po_info.getGrade());
            listView.setAdapter(adapter);
            dialog.setView(listView);
            dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    static class QntAdapter extends ArrayAdapter<ArrayList<GradeInfo>> {
        Context context; ArrayList<GradeInfo> grade;int flag;

        QntAdapter(Context context, ArrayList<GradeInfo> grade){
            super(context,R.layout.item_grades, Collections.singletonList(grade));
            this.context = context;
            this.grade = grade;
        }

        @Override
        public int getCount() {
            return grade.size();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_grades,parent,false);
            TextView gname = view.findViewById(R.id.gname);
            TextView qnt = view.findViewById(R.id.qnt);
            TextView amt = view.findViewById(R.id.amt);

            gname.setText(grade.get(position).getGrade_name());
            qnt.setText(grade.get(position).getQnty());
            amt.setText("₹"+(Integer.parseInt(grade.get(position).getQnty())*Integer.parseInt(grade.get(position).getRate())));
            return view;
        }
    }


}

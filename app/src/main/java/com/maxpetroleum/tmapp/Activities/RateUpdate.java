package com.maxpetroleum.tmapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.tmapp.Model.GradeRate;
import com.maxpetroleum.tmapp.Model.Rate;
import com.maxpetroleum.tmapp.R;

import java.util.ArrayList;
import java.util.Calendar;

public class RateUpdate extends AppCompatActivity {
    ListView rv;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Rate> rates;
    ProgressDialog progressDialog;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        init();
    }

    private void getGradeData(){
        databaseReference.child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    rates.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        rates.add(new Rate(ds.child("name").getValue().toString(),Double.parseDouble(ds.child("rate").getValue().toString())));
                    }
                    progressDialog.dismiss();
                    GradeAdapter adapter = new GradeAdapter(RateUpdate.this,rates);
                    adapter.notifyDataSetChanged();
                    rv.setAdapter(adapter);
                }
                catch (Exception e){
                    Toast.makeText(RateUpdate.this, "Error loading rate", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {

        rv = findViewById(R.id.recycler_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Grades");
        progressDialog.show();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("GradeRate");
        add = findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog2 = new AlertDialog.Builder(RateUpdate.this);
                LayoutInflater inflater = LayoutInflater.from(RateUpdate.this);
                View view = inflater.inflate(R.layout.dialog_update_grade,null);
                dialog2.setView(view);
                final EditText name_et = view.findViewById(R.id.name);
                final EditText rate_et = view.findViewById(R.id.rate);
                dialog2.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!TextUtils.isEmpty(rate_et.getText().toString()) && !TextUtils.isEmpty(name_et.getText().toString().trim())){
                            rates.add(new Rate(name_et.getText().toString(),Double.parseDouble(rate_et.getText().toString())));
                            RateUpdate.updataRate(rates);
                        }
                    }
                });
                dialog2.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog2.show();
            }
        });

        rates = new ArrayList<>();
        getGradeData();

    }

    public static void updataRate(ArrayList<Rate> rates){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GradeRate");
        Calendar calendar = Calendar.getInstance();
        String month=String.valueOf(calendar.get(calendar.MONTH));
        GradeRate rate=new GradeRate(month,rates);
        ref.setValue(rate);

    }
    class GradeAdapter extends ArrayAdapter<ArrayList<GradeAdapter>> {
        Context context;
        ArrayList<Rate> grade;

        GradeAdapter(Context context, ArrayList<Rate> grade) {
            super(context, R.layout.item_grade_rate);
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
            final LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_grade_rate, parent, false);
            TextView gname = view.findViewById(R.id.gname);
            TextView amt = view.findViewById(R.id.amt);
            ImageView iv = view.findViewById(R.id.options);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setItems(new String[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(which==0){
                                final AlertDialog.Builder dialog2 = new AlertDialog.Builder(context);
                                LayoutInflater inflater = LayoutInflater.from(context);
                                View view = inflater.inflate(R.layout.dialog_update_grade,null);
                                dialog2.setView(view);
                                EditText name_et = view.findViewById(R.id.name);
                                final EditText rate_et = view.findViewById(R.id.rate);
                                name_et.setText(grade.get(position).getName());
                                name_et.setEnabled(false);
                                dialog2.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(TextUtils.isEmpty(rate_et.getText().toString())){
                                            rate_et.setError("Empty");
                                        }
                                        else{
                                            grade.get(position).setRate(Double.parseDouble(rate_et.getText().toString()));
                                            RateUpdate.updataRate(grade);
                                        }
                                    }
                                });
                                dialog2.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog2.show();
                            }
                            else if(which==1){
                                grade.remove(position);
                                RateUpdate.updataRate(grade);
                            }
                        }
                    });
                    dialog.show();
                }
            });
            gname.setText(grade.get(position).getName());
            amt.setText("₹" + grade.get(position).getRate());
            return view;
        }
    }
}

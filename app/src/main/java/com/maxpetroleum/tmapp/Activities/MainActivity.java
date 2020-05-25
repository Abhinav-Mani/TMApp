package com.maxpetroleum.tmapp.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.tmapp.R;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Permission";
    Button setRate, addSo, SOList,exportExcel;
    CSVWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setListers();

        isReadStoragePermissionGranted();

        isWriteStoragePermissionGranted();

    }

    private void setListers() {
        setRate.setOnClickListener(this);
        addSo.setOnClickListener(this);
        SOList.setOnClickListener(this);
        exportExcel.setOnClickListener(this);
    }

    private void init() {
        setRate = findViewById(R.id.setRate);
        addSo = findViewById(R.id.addSo);
        SOList = findViewById(R.id.SOLIST);
        exportExcel = findViewById(R.id.exportExcel);

        if (!isOnline()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setTitle("Alert");
            dialog.setMessage("You are not connected to internet!");
            dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == setRate) {
            Intent intent = new Intent(this, RateUpdate.class);
            startActivity(intent);
        } else if (view == addSo) {
            Intent intent = new Intent(this, AddSO.class);
            startActivity(intent);
        } else if (view == SOList) {
            Intent intent = new Intent(this, SoList.class);
            startActivity(intent);
        } else if(view == exportExcel){
            exportToExcel();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void exportToExcel() {
        final int
                DealerName = 0,
                SO_Name = 1,
                PO_Number = 2,
                PO_Date = 3,
                grade_name = 4,
                qty = 5,
                Amount = 4,
                Payment_Date = 5,
                Bill_Date = 6,
                Delivery_Date = 7;

        String[] column_names = {
                "Dealer",
                "Sales Officer",
                "PO Number",
                "PO Date",
//                "Grade Name",
//                "Quantity",
                "Amount",
                "Payment Date",
                "Bill Date",
                "Delivery Date"
        };
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait");
        pd.show();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "MHC Data.csv";
        String filePath = baseDir + File.separator + fileName;
        final File f = new File(filePath);
        FileWriter mFileWriter;
        try {
            if (f.exists() && !f.isDirectory()) {
                mFileWriter = new FileWriter(filePath);
                writer = new CSVWriter(mFileWriter);
                writer.writeNext(column_names);
            } else {
                writer = new CSVWriter(new FileWriter(filePath));
                writer.writeNext(column_names);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] row = new String[10];
                if(dataSnapshot.child("PO").exists()){
                    Toast.makeText(MainActivity.this, "It is saved as MHC Data.csv in your storage", Toast.LENGTH_LONG).show();
                    for (DataSnapshot ds : dataSnapshot.child("PO").getChildren()) {
                        row[DealerName] = dataSnapshot.child("UserData").child("Dealer").child(ds.getKey()).child("name").getValue().toString();
                        String soid = dataSnapshot.child("UserData").child("Dealer").child(ds.getKey()).child("so id").getValue().toString();
                        row[SO_Name] = dataSnapshot.child("UserData").child("Sales officer").child(soid).child("name").getValue().toString();

                        for(DataSnapshot poItem: ds.getChildren()){
                            if(poItem.child("po_no").exists()) row[PO_Number] = poItem.child("po_no").getValue().toString();
                            else row[PO_Number] = "";
                            if(poItem.child("po_Date").exists()) row[PO_Date] = poItem.child("po_Date").getValue().toString();
                            else row[PO_Date] = "";
                            if(poItem.child("amount").exists()) row[Amount] = poItem.child("amount").getValue().toString();
                            else row[Amount] = "";
                            if(poItem.child("payment_date").exists()) row[Payment_Date] = poItem.child("payment_date").getValue().toString();
                            else row[Payment_Date] = "";
                            if(poItem.child("bill_date").exists()) row[Bill_Date] = poItem.child("bill_date").getValue().toString();
                            else row[Bill_Date] = "";
                            if(poItem.child("delivery_date").exists()) row[Delivery_Date] = poItem.child("delivery_date").getValue().toString();
                            else row[Delivery_Date] = "";

                            writer.writeNext(row);

                        }
                    }

                }
                pd.dismiss();
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }
}

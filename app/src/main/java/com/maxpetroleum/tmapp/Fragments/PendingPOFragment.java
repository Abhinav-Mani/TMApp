package com.maxpetroleum.tmapp.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.tmapp.Activities.PoList;
import com.maxpetroleum.tmapp.Adapter.PoListAdapter;
import com.maxpetroleum.tmapp.Model.PO_Info;
import com.maxpetroleum.tmapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingPOFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    ArrayList<PO_Info> list;
    PoListAdapter adapter;

    public PendingPOFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_pending_po, container, false);
        init(view, (PoList) getActivity());
        fetch();
        return view;
    }

    private void fetch() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                Log.d("ak47", dataSnapshot.getKey() + " " + dataSnapshot.getValue() + " " + "onDataChange: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (String.valueOf(dataSnapshot1.getValue()).equalsIgnoreCase("Pending")) {
                        getMoreInfo(dataSnapshot1.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMoreInfo(final String key) {
        Log.d("hello", "getMoreInfo: " + key);
        database.getReference("PO").child(PoList.dealer.getUid()).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("hello", "data: " + dataSnapshot.getValue().toString());
                Log.d("hello", "getMoreInfo: " + list.size());
                PO_Info po_info = dataSnapshot.getValue(PO_Info.class);
                boolean found = false;
                for (int i = 0; i < list.size(); i++) {
                    Log.d("hello", "getMoreInfo: after change" + i);

                    if (list.get(i).getPo_no().equalsIgnoreCase(po_info.getPo_no())) {
                        list.get(i).setBill_date(po_info.getBill_date());
                        found = true;
                    }
                    if (list.get(i).getBill_date() != null) {
                        list.remove(i);
                        found = true;
                        Log.d("hello", "found");
                    }
                }
                if (!found)
                    list.add(po_info);
                Log.d("hello", "getMoreInfo: after change" + list.size());
                adapter.notifyDataSetChanged();
                PoList.hashMap.put(po_info.getPo_no(), key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void init(View view, PoList poList) {
        database = FirebaseDatabase.getInstance();
        Log.d("ak47", "init: " + PoList.dealer.getUid());
        myRef = database.getReference().child("Dealer").child(PoList.dealer.getUid());
        recyclerView = view.findViewById(R.id.pendingRequestsPO);
        list = new ArrayList<>();
        adapter = new PoListAdapter(list, poList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

}

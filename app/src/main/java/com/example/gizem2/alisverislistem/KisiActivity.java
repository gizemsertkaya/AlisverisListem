package com.example.gizem2.alisverislistem;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.gizem2.alisverislistem.Adapter.MyAdapterKisi;
import com.example.gizem2.alisverislistem.Base.BaseActivity;
import com.example.gizem2.alisverislistem.Base.Kisi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KisiActivity extends BaseActivity {

    SwipeRefreshLayout swipeRefresh;
    ArrayList<Kisi> kisilerlist;
    static ArrayList<String> kisiler;
    public ListView listgelecekkisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisi);
        listgelecekkisi = (ListView) findViewById(R.id.listkisiler);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.list_swipperrefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                kisileridoldur();
            }
        });

        listgelecekkisi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                kisiler.add(kisilerlist.get(position).getId());
            }
        });
    }

    private void kisileridoldur() {
        showProgressDialog("Lütfen bekleyin", "Veritabanına bağlantı kuruluyor");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        final Query kisilerquery = myRef.child("uyeler");
        kisilerquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kisilerlist = new ArrayList<>();
                for (DataSnapshot gelen : dataSnapshot.getChildren()) {
                    Kisi yeni = gelen.getValue(Kisi.class);
                    kisilerlist.add(yeni);
                }
                MyAdapterKisi adapter = new MyAdapterKisi(KisiActivity.this, kisilerlist);
                listgelecekkisi.setAdapter(adapter);
                hideProgressDialog();
                kisilerquery.removeEventListener(this);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        kisileridoldur();
    }
}

package com.example.gizem2.alisverislistem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gizem2.alisverislistem.Adapter.MyAdapterUrunler;
import com.example.gizem2.alisverislistem.Base.BaseActivity;
import com.example.gizem2.alisverislistem.Base.Urunler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UrunlerActivity extends BaseActivity {
    SwipeRefreshLayout swipeRefresh;
    ArrayList<Urunler> urunlerList;
    String listeId;
    ListView listyapilacakurun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urunler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        listeId = intent.getStringExtra("listeId");

        listyapilacakurun = (ListView) findViewById(R.id.list1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(UrunlerActivity.this, UrunEkleActivity.class);
                intent.putExtra("listeId", listeId);
                startActivity(intent);
            }
        });
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.list_swipperrefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UrunleriDoldur();
            }
        });
    }

    private void UrunleriDoldur() {
        showProgressDialog("Lütfen bekleyin", "Veritabanına bağlantı kuruluyor");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        final Query listelerQuery = myRef.child("urunler").orderByChild("eklenmeZamani");
        listelerQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                urunlerList = new ArrayList<>();
                for (DataSnapshot gelen : dataSnapshot.getChildren()) {
                    Urunler yeni = gelen.getValue(Urunler.class);
                    if (yeni.getListeId().equals(listeId))
                        urunlerList.add(yeni);
                }
                MyAdapterUrunler adapter = new MyAdapterUrunler(UrunlerActivity.this, urunlerList);
                listyapilacakurun.setAdapter(adapter);
                hideProgressDialog();
                listelerQuery.removeEventListener(this);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kisi, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.arkadas_ekle) {
//
           Intent intent=new Intent(this,KisiActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UrunleriDoldur();
    }
}

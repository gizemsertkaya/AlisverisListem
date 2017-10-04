package com.example.gizem2.alisverislistem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gizem2.alisverislistem.Adapter.MyAdapterListe;
import com.example.gizem2.alisverislistem.Base.BaseActivity;
import com.example.gizem2.alisverislistem.Base.Kisi;
import com.example.gizem2.alisverislistem.Base.Liste;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainListeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SwipeRefreshLayout swipeRefresh;
    TextView nav_txtAdSoyad, nav_txtEmail;
    Kisi kullanici;
    ArrayList<Liste> listelerList;

    ListView listyapilacak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        listyapilacak = (ListView) findViewById(R.id.list1);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.list_swipperrefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listeleriDoldur();


            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/


                startActivity(new Intent(MainListeActivity.this, ListeEkleActivity.class));


            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) finish();

        nav_txtAdSoyad = (TextView) header.findViewById(R.id.nav_txtAdSoyad);
        nav_txtEmail = (TextView) header.findViewById(R.id.nav_txtEmail);

        nav_txtEmail.setText(user.getEmail());

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("uyeler");
        Query query = myRef.child(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kullanici = dataSnapshot.getValue(Kisi.class);
                nav_txtAdSoyad.setText(String.format("%s", kullanici.getAd() == null ? "Ad" : kullanici.getAd()));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        listeleriDoldur();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
////            mAuth = FirebaseAuth.getInstance();
////            FirebaseAuth.getInstance().signOut();
////            LoginManager.getInstance().logOut();
////            Toast.makeText(this, "GÜLE GÜLE", Toast.LENGTH_SHORT).show();
////            mAuth.signOut();
////            goLoginScreen();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_kisiler) {
            startActivity(new Intent(MainListeActivity.this, KisiActivity.class));
        } else if (id == R.id.nav_cikis) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Toast.makeText(this, "GÜLE GÜLE", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            goLoginScreen();

       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void goLoginScreen() {
        Intent intent = new Intent(MainListeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void listeleriDoldur() {
        showProgressDialog("Lütfen bekleyin", "Veritabanına bağlantı kuruluyor");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        final Query listelerQuery = myRef.child("listeler").orderByChild("eklenmeZamani");
        listelerQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listelerList = new ArrayList<>();
                for (DataSnapshot gelen : dataSnapshot.getChildren()) {
                    Liste yeni = gelen.getValue(Liste.class);
                    if (yeni.getEkleyen().equals(user.getUid().toString()))
                    listelerList.add(yeni);
                }
                MyAdapterListe adapter = new MyAdapterListe(MainListeActivity.this, listelerList);
                listyapilacak.setAdapter(adapter);
                hideProgressDialog();
                listelerQuery.removeEventListener(this);
                swipeRefresh.setRefreshing(false);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

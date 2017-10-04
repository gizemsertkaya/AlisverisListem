package com.example.gizem2.alisverislistem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gizem2.alisverislistem.Base.BaseActivity;
import com.example.gizem2.alisverislistem.Base.Liste;
import com.example.gizem2.alisverislistem.Base.Urunler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class UrunEkleActivity extends BaseActivity {
    Button btnEkle;
    EditText txtYapilacak;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String listeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        listeId = intent.getStringExtra("listeId");

        setContentView(R.layout.activity_urunekle);
        btnEkle = (Button) findViewById(R.id.add_btnEkle);
        txtYapilacak = (EditText) findViewById(R.id.add_txtUrun);
        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtYapilacak.getText())) {
                    Toast.makeText(UrunEkleActivity.this, "Lütfen birşeyler yazın", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressDialog("Veritabanına bağlanıyor", "Lütfen bekleyiniz");
                Urunler urunler = new Urunler();
                urunler.setAd(txtYapilacak.getText().toString());
                urunler.setEklenmeZamani(new Date().toString());
                urunler.setListeId(listeId);
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                urunler.setEkleyen(user.getUid().toString());
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference().child("urunler");
                myRef.child(urunler.getId().toString()).setValue(urunler);
                Toast.makeText(UrunEkleActivity.this, "Eklendi", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
                finish();
            }
        });
    }
}

package com.example.gizem2.alisverislistem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gizem2.alisverislistem.Base.BaseActivity;
import com.example.gizem2.alisverislistem.Base.Liste;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ListeEkleActivity extends BaseActivity {
    Button btnEkle;
    EditText txtYapilacak;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressDialog dialog;
    Liste liste;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listeekle);
        btnEkle = (Button) findViewById(R.id.add_btnEkle);
        txtYapilacak = (EditText) findViewById(R.id.add_txtYapilacak);
        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(txtYapilacak.getText())) {
                    Toast.makeText(ListeEkleActivity.this, "Lütfen birşeyler yazın", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressDialog("Veritabanına bağlanıyor", "Lütfen bekleyiniz");
                Liste liste = new Liste();
                liste.setIcerik(txtYapilacak.getText().toString());
                liste.setEklenmeZamani(new Date().toString());
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                liste.setEkleyen(user.getUid().toString());

//                ArrayList<String> kisiler = MyAdapterKisi.kisiler;
//                liste.setArkadasId(kisiler);

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference().child("listeler");
                myRef.child(liste.getId().toString()).setValue(liste);

                Toast.makeText(ListeEkleActivity.this, "Eklendi", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ListeEkleActivity.this, MainListeActivity.class));
                hideProgressDialog();
                finish();


            }
        });

      /*
              if (checkarkadas.isChecked()){
                  liste.setArkadasId();
              }*/



    }
}

package com.example.gizem2.alisverislistem.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gizem2.alisverislistem.Base.Kisi;
import com.example.gizem2.alisverislistem.Base.Liste;
import com.example.gizem2.alisverislistem.R;
import com.example.gizem2.alisverislistem.UrunlerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gizem2 on 28.09.2017.
 */

public class MyAdapterListe extends ArrayAdapter<Liste> {
    private final Context context;
    private final ArrayList<Liste> values;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    Button btnDetay;
    SwipeRefreshLayout swipeRefresh;

    public MyAdapterListe(Context context, ArrayList<Liste> values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
        //this.onClickListener=onClickListener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        CircleImageView circleImage = (CircleImageView) rowView.findViewById(R.id.lst_imgprofil);
        TextView textView = (TextView) rowView.findViewById(R.id.lst_txtKullaniciAdi);
        final TextView txtIcerik = (TextView) rowView.findViewById(R.id.lst_txtIcerik);
        txtIcerik.setText(values.get(position).getIcerik());
        btnDetay = (Button) rowView.findViewById(R.id.btnurunler);
        btnDetay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UrunlerActivity.class);
                intent.putExtra("listeId", values.get(position).getId());
                context.startActivity(intent);




            }
        });


        txtIcerik.setOnLongClickListener(new View.OnLongClickListener() {


            @Override
            public boolean onLongClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Liste silinecek = values.get(position);
                                database = FirebaseDatabase.getInstance();
                                myRef = database.getReference().child("listeler");
                                myRef.child(silinecek.getId()).removeValue();
                                Toast.makeText(context, "Silindi", Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(context, "İptal edildi", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                        .setNegativeButton("Bilemiyorum", dialogClickListener).show();
                return false;

            }
        });

        kullaniciBilgisiniGetir(values.get(position).getEkleyen(), circleImage, textView);
        return rowView;
    }

    private void kullaniciBilgisiniGetir(String ekleyen, final ImageView imageView, final TextView textView) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("uyeler");
        final Query query = myRef.child(ekleyen);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kisi kisi = dataSnapshot.getValue(Kisi.class);
                if (kisi.getAd() != null)
                    textView.setText(String.format("%s %s", kisi.getAd() == null ? "Ad" : kisi.getAd(), kisi.getSoyad() == null ? "" : kisi.getSoyad()));
                if (kisi.getFotograf() != null)
                    Picasso.with(context).load(kisi.getFotograf()).into(imageView);
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
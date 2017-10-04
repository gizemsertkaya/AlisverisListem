package com.example.gizem2.alisverislistem.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gizem2.alisverislistem.Base.Kisi;
import com.example.gizem2.alisverislistem.Base.Liste;
import com.example.gizem2.alisverislistem.Base.Urunler;
import com.example.gizem2.alisverislistem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gizem2 on 29.09.2017.
 */

public class MyAdapterUrunler extends ArrayAdapter<Urunler> {
    private final Context context;
    private final ArrayList<Urunler> values;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    TextView textView;

    public MyAdapterUrunler(Context context, ArrayList<Urunler> values) {
        super(context, R.layout.urunler_liste_item, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.urunler_liste_item, parent, false);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.lst_checkBox);
      // CircleImageView circleImage = (CircleImageView) rowView.findViewById(R.id.lst_imgprofil);
        TextView textView = (TextView) rowView.findViewById(R.id.lst_txtKullaniciAdi);
        final TextView txtIcerik = (TextView) rowView.findViewById(R.id.lst_txtIcerik);
        txtIcerik.setText(values.get(position).getAd());


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    txtIcerik.setPaintFlags(txtIcerik.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    if (values.get(position).getYapilmaZamani() != null)
                        return;
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference().child("urunler");
                    Urunler urunler = values.get(position);
                    urunler.setYapilmaZamani(new Date().toString());
                    myRef.child(urunler.getId()).setValue(urunler);

                } else {
                    txtIcerik.setPaintFlags(txtIcerik.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference().child("urunler");
                    Urunler urunler = values.get(position);
                    urunler.setYapilmaZamani(null);
                    myRef.child(urunler.getId()).setValue(urunler);
                }
            }
        });
        if (values.get(position).getYapilmaZamani() == null)
            checkBox.setChecked(false);
        else
            checkBox.setChecked(true);

        txtIcerik.setOnLongClickListener(new View.OnLongClickListener() {


            @Override
            public boolean onLongClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Urunler silinecek = values.get(position);
                                database = FirebaseDatabase.getInstance();
                                myRef = database.getReference().child("urunler");
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
                builder.setMessage("Silmek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                        .setNegativeButton("Hayır", dialogClickListener).show();
                return false;

            }
        });
      kullaniciBilgisiniGetir(values.get(position).getEkleyen(), textView);
        return rowView;
    }

    private void kullaniciBilgisiniGetir(String ekleyen,final TextView textView) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("uyeler");
        final Query query = myRef.child(ekleyen);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kisi kisi = dataSnapshot.getValue(Kisi.class);
                if (kisi.getAd() != null)
                    textView.setText(String.format("%s %s", kisi.getAd() == null ? "Ad" : kisi.getAd(), kisi.getSoyad() == null ? "" : kisi.getSoyad()));
//                if (kisi.getFotograf() != null)
//                    Picasso.with(context).load(kisi.getFotograf()).into(imageView);
//                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

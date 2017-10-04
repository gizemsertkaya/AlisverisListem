package com.example.gizem2.alisverislistem.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gizem2.alisverislistem.Base.Kisi;
import com.example.gizem2.alisverislistem.R;
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
 * Created by Gizem2 on 1.10.2017.
 */

public class MyAdapterKisi extends ArrayAdapter<Kisi> implements View.OnClickListener {

    private final Context context;
    private final ArrayList<Kisi> values;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private CheckBox checkarkadas;
    public static ArrayList<Kisi> secilenKisiler;
    private String listId;
    SwipeRefreshLayout swipeRefresh;

    public MyAdapterKisi(Context context, ArrayList<Kisi> values) {
        super(context, R.layout.list_item_kisi, values);
        this.context = context;
        this.values = values;
        //this.onClickListener=onClickListener;
    }
    public MyAdapterKisi(Context context, ArrayList<Kisi> values,String listId) {
        super(context, R.layout.list_item_kisi, values);
        this.context = context;
        this.values = values;
        this.listId=listId;
        //this.onClickListener=onClickListener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_kisi, parent, false);
        final int pos = position;
        checkarkadas = (CheckBox) rowView.findViewById(R.id.checkarkadasekle);

        //paylasilanlariDoldur();

        checkarkadas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(secilenKisiler==null)
                    secilenKisiler= new ArrayList<Kisi>();
                Kisi falan= values.get(pos);
                if(isChecked){
                    secilenKisiler.add(falan);
                }else{
                    secilenKisiler.remove(falan);
                }
                //paylasimikaydet();
            }
        });
        CircleImageView circleImage = (CircleImageView) rowView.findViewById(R.id.lst_imgprofil);
        TextView textView = (TextView) rowView.findViewById(R.id.lst_txtKullaniciAdi);
        kullaniciBilgisiniGetir(values.get(position).getId(), textView, circleImage);
        return rowView;
    }

    private void kullaniciBilgisiniGetir(String ekleyen, final TextView textView, final ImageView imageView) {
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


    @Override
    public void onClick(View v) {

    }
}

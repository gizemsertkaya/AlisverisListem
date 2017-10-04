package com.example.gizem2.alisverislistem.Base;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Gizem2 on 23.09.2017.
 */

public class Liste {
    private String icerik, eklenmeZamani, yapilmaZamani, id, ekleyen;
    private ArrayList<String> arkadasId;

    public ArrayList<String> getArkadasId() {
        return arkadasId;
    }

    public void setArkadasId(ArrayList<String> arkadasId) {
        this.arkadasId = arkadasId;
    }


    public String getIcerik() {
        return icerik;
    }


    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public String getEklenmeZamani() {
        return eklenmeZamani;
    }

    public void setEklenmeZamani(String eklenmeZamani) {
        this.eklenmeZamani = eklenmeZamani;
    }

    public String getYapilmaZamani() {
        return yapilmaZamani;
    }

    public void setYapilmaZamani(String yapilmaZamani) {
        this.yapilmaZamani = yapilmaZamani;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEkleyen() {
        return ekleyen;
    }

    public void setEkleyen(String ekleyen) {
        this.ekleyen = ekleyen;
    }

    public Liste() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return icerik;
    }
}

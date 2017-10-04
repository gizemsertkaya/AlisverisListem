package com.example.gizem2.alisverislistem.Base;

import java.util.UUID;

/**
 * Created by Gizem2 on 28.09.2017.
 */

public class Urunler {
    private String ad, eklenmeZamani, yapilmaZamani, id, ekleyen;
    private String listeId;

    public String getListeId() {
        return listeId;
    }

    public void setListeId(String listeId) {
        this.listeId = listeId;
    }

    public Urunler() {
        this.id = UUID.randomUUID().toString();
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
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
}

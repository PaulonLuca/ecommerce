package com.unife.ecommerce.model.mo;

import java.util.ArrayList;

public class Categoria {
    private Long idCat;
    private String nomeCat;
    //(0,n) array di cataloghi in cui è presente una certa cetagoria
    private ArrayList<Catalogo> cataloghi;
    //(0,n) array di prodotti di con questa categoria
    private ArrayList<Prodotto> prodotti;
    private boolean deleted;

    public Long getIdCat() { return idCat; }

    public void setIdCat(Long idCat) { this.idCat = idCat; }

    public String getNomeCat() { return nomeCat;}

    public void setNomeCat(String nomeCat) { this.nomeCat = nomeCat;}

    public ArrayList<Catalogo> getCataloghi() { return cataloghi; }

    public void setCataloghi(ArrayList<Catalogo> cataloghi) { this.cataloghi = cataloghi; }

    public ArrayList<Prodotto> getProdotti() { return prodotti; }

    public void setProdotti(ArrayList<Prodotto> prodotti) { this.prodotti = prodotti; }

    public boolean isDeleted() { return deleted; }

    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}

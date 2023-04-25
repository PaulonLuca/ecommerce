package com.unife.ecommerce.model.mo;

import java.util.ArrayList;

public class Prodotto {
    private Long idProd;
    private String nomeProd;
    private String descr;
    private int qtyDisp;
    private double prezzo;
    private String fotoPath;//da sostituire con array di foto
    private boolean isLocked;
    //(1,1) un prodotto ha una cetegoria
    private Categoria cat;
    //(1,1) un prodotto ha una marca
    private Marca marca;
    //(1,1) un prodotto ha un fornitore
    private Fornitore forn;
    //(0,n) un prodotto puù stare in n vetrine promozionali
    //ArrayList di coppie vetrina-sconto<--------DA FARE
    //(0,n) un prodotto ha una lista di ordini in cui è inserito con la rispettiva quantità
    //ArrayList<Ordine,qty>

    public Long getIdProd() { return idProd;}

    public void setIdProd(Long idProd) { this.idProd = idProd;}

    public String getNomeProd() { return nomeProd;}

    public void setNomeProd(String nomeProd) {this.nomeProd = nomeProd;}

    public String getDescr() {return descr;}

    public void setDescr(String descr) {this.descr = descr;}

    public int getQtyDisp() {return qtyDisp;}

    public void setQtyDisp(int qtyDisp) {this.qtyDisp = qtyDisp;}

    public double getPrezzo() {return prezzo;}

    public void setPrezzo(double prezzo) {this.prezzo = prezzo;}

    public String getFotoPath() {return fotoPath;}

    public void setFotoPath(String fotoPath) {this.fotoPath = fotoPath;}

    public boolean isLocked() {return isLocked;}

    public void setLocked(boolean locked) {isLocked = locked;}

    public Categoria getCat() {return cat;}

    public void setCat(Categoria cat) {this.cat = cat;}

    public Marca getMarca() {return marca;}

    public void setMarca(Marca marca) {this.marca = marca;}

    public Fornitore getForn() {return forn;}

    public void setForn(Fornitore forn) {this.forn = forn;}

    //(0,n) un prodotto puù stare in n vetrine promozionali
    //ArrayList di coppie vetrina-sconto
}



package com.unife.ecommerce.model.mo;

import java.io.File;
import java.util.ArrayList;

//Oggetto che mappa il prodotto
public class Prodotto {
    private Long idProd;
    private String nomeProd;
    private String descr;
    private int qtyDisp;
    private double prezzo;
    private String fotoPath;//da sostituire con array di foto
    private File[] fotoProdotto;
    private boolean isLocked;
    //(1,1) un prodotto ha una cetegoria
    private Categoria cat;
    //(1,1) un prodotto ha una marca
    private Marca marca;
    //(1,n) un prodotto ha n fornitori
    private ArrayList<Fornitore> fornitori;
    //(0,n) un prodotto ha una lista di ordini in cui è inserito con la rispettiva quantità
    private ArrayList<OrdineQty> ordQty;
    //(0,n) ogni prodotto ha una lista di carrelli in cui è presente in una certa quantità
    private ArrayList<CarrelloQty> carrelli;
    private boolean deleted;

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

    public ArrayList<OrdineQty> getOrdQty() {   return ordQty;  }

    public void setOrdQty(ArrayList<OrdineQty> ordQty) {    this.ordQty = ordQty;}

    public boolean isDeleted() {    return deleted;}

    public void setDeleted(boolean deleted) {   this.deleted = deleted; }

    public File[] getFotoProdotto() {   return fotoProdotto;}

    public void setFotoProdotto(File[] fotoProdotto) {  this.fotoProdotto = fotoProdotto;}

    public ArrayList<Fornitore> getFornitori() {return fornitori;}

    public void setFornitori(ArrayList<Fornitore> fornitori) {this.fornitori = fornitori;}

    public ArrayList<CarrelloQty> getCarrelli() {return carrelli;}

    public void setCarrelli(ArrayList<CarrelloQty> carrelli) {this.carrelli = carrelli;}
}



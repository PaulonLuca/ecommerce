package com.unife.ecommerce.model.mo;

import java.util.ArrayList;

public class Marca {
    private Long idMarca;
    private String nomeMarca;
    //(0,n) array di prodotti con questa marca
    private ArrayList<Prodotto> prodotti;
    private boolean deleted;

    public Long getIdMarca() { return idMarca; }

    public void setIdMarca(Long idMarca) { this.idMarca = idMarca; }

    public String getNomeMarca() { return nomeMarca; }

    public void setNomeMarca(String nomeMarca) { this.nomeMarca = nomeMarca; }

    public ArrayList<Prodotto> getProdotti() {  return prodotti;    }

    public void setProdotti(ArrayList<Prodotto> prodotti) { this.prodotti = prodotti;   }

    public boolean isDeleted() {    return deleted; }

    public void setDeleted(boolean deleted) {   this.deleted = deleted; }
}

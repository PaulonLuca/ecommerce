package com.unife.ecommerce.model.mo;

import java.util.ArrayList;

//Oggetto che mappa lo stato
public class Stato {
    private Long idStato;
    private String nomeStato;
    //(0,n) lista degli ordini che si trovano in questo stato
    private ArrayList<Ordine> ordini;
    private boolean deleted;

    public Long getIdStato() { return idStato; }

    public void setIdStato(Long idStato) { this.idStato = idStato; }

    public String getNomeStato() { return nomeStato; }

    public void setNomeStato(String nomeStato) { this.nomeStato = nomeStato; }

    public ArrayList<Ordine> getOrdini() {  return ordini;  }

    public void setOrdini(ArrayList<Ordine> ordini) {   this.ordini = ordini;   }

    public boolean isDeleted() {    return deleted; }

    public void setDeleted(boolean deleted) {   this.deleted = deleted; }
}

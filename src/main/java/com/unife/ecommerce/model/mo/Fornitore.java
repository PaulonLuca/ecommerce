package com.unife.ecommerce.model.mo;

import java.util.ArrayList;

public class Fornitore {
    private Long idFornitore;
    private String nomeFornitore;
    //(0,n) Array di proddoti forniti da questo fornitore
    private ArrayList<Prodotto> prodotti;
    private boolean deleted;

    public Long getIdFornitore() { return idFornitore; }

    public void setIdFornitore(Long idFornitore) { this.idFornitore = idFornitore; }

    public String getNomeFornitore() { return nomeFornitore; }

    public void setNomeFornitore(String nomeFornitore) { this.nomeFornitore = nomeFornitore; }

    public ArrayList<Prodotto> getProdotti() { return prodotti; }

    public void setProdotti(ArrayList<Prodotto> prodotti) { this.prodotti = prodotti; }

    public boolean isDeleted() { return deleted; }

    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}

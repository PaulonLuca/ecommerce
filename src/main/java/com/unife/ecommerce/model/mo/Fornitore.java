package com.unife.ecommerce.model.mo;

public class Fornitore {
    private Long idFornitore;
    private String nomeFornitore;
    //(0,n) Array di proddoti forniti da questo fornitore
    private Prodotto[] prodotti;

    public Long getIdFornitore() { return idFornitore; }

    public void setIdFornitore(Long idFornitore) { this.idFornitore = idFornitore; }

    public String getNomeFornitore() { return nomeFornitore; }

    public void setNomeFornitore(String nomeFornitore) { this.nomeFornitore = nomeFornitore; }

    public Prodotto[] getProdotti() {return prodotti;}

    public void setProdotti(Prodotto[] prodotti) { this.prodotti = prodotti;}
}

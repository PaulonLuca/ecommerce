package com.unife.ecommerce.model.mo;

public class Marca {
    private Long idMarca;
    private String nomeMarca;
    //(0,n) array di prodotti con questa marca
    private Prodotto[] prodotti;

    public Long getIdMarca() { return idMarca; }

    public void setIdMarca(Long idMarca) { this.idMarca = idMarca; }

    public String getNomeMarca() { return nomeMarca; }

    public void setNomeMarca(String nomeMarca) { this.nomeMarca = nomeMarca; }

    public Prodotto[] getProdotti() { return prodotti;}

    public void setProdotti(Prodotto[] prodotti) {this.prodotti = prodotti;}
}

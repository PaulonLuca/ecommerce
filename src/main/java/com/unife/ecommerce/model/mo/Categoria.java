package com.unife.ecommerce.model.mo;

public class Categoria {
    private Long idCat;
    private String nomeCat;
    //(0,n) array di cataloghi in cui è presente una certa cetagoria
    private Catalogo[] cataloghi;
    //(0,n) array di prodotti di con questa categoria
    private Prodotto[] prodotti;

    public Long getIdCat() { return idCat; }

    public void setIdCat(Long idCat) { this.idCat = idCat; }

    public String getNomeCat() { return nomeCat;}

    public void setNomeCat(String nomeCat) { this.nomeCat = nomeCat;}

    public Prodotto[] getProdotti() {return prodotti;}

    public void setProdotti(Prodotto[] prodotti) { this.prodotti = prodotti;}
}

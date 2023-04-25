package com.unife.ecommerce.model.mo;

public class Vetrina {
    private Long idVetrina;
    private String nomeVetrina;
    //(0,n) array di prodotti nella vetrina con lo sconto
    //ArrayList<Prod, int>  <----DA FARE

    public Long getIdVetrina() { return idVetrina;}

    public void setIdVetrina(Long idVetrina) { this.idVetrina = idVetrina;}

    public String getNomeVetrina() { return nomeVetrina; }

    public void setNomeVetrina(String nomeVetrina) { this.nomeVetrina = nomeVetrina; }
}

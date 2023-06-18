package com.unife.ecommerce.model.mo;

//Oggetto che mappa la vetrina
public class Vetrina {
    private Long idVetrina;
    private String nomeVetrina;
    private int sconto;

    public Long getIdVetrina() { return idVetrina;}

    public void setIdVetrina(Long idVetrina) { this.idVetrina = idVetrina;}

    public String getNomeVetrina() { return nomeVetrina; }

    public void setNomeVetrina(String nomeVetrina) { this.nomeVetrina = nomeVetrina; }

    public int getSconto() {    return sconto;}

    public void setSconto(int sconto) { this.sconto = sconto;   }
}

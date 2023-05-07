package com.unife.ecommerce.model.mo;

public class Spedizione {
    private Long idSped;
    private String nomeSped;
    private int numGiorni;
    private double costo;
    //(1,1) la spedizione ha un solo ordine
    private Ordine ordine;
    private boolean deleted;

    public Long getIdSped() {return idSped;}

    public void setIdSped(Long idSped) {this.idSped = idSped;}

    public String getNomeSped() {return nomeSped;}

    public void setNomeSped(String nomeSped) {this.nomeSped = nomeSped;}

    public int getNumGiorni() {return numGiorni;}

    public void setNumGiorni(int numGiorni) {this.numGiorni = numGiorni;}

    public double getCosto() {return costo;}

    public void setCosto(double costo) {this.costo = costo;}

    public Ordine getOrdine() {return ordine;}

    public void setOrdine(Ordine ordine) {this.ordine = ordine;}

    public boolean isDeleted() {return deleted;}

    public void setDeleted(boolean deleted) {this.deleted = deleted;}
}

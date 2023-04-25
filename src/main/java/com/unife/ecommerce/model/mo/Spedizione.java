package com.unife.ecommerce.model.mo;


import java.sql.Date;

public class Spedizione {
    private Long idSped;
    private Date dataSped; //data dell'sql
    private boolean isOrdinaria;
    private double speseOrd;
    private double speseEpr;
    //(1,1) la spedizione ha un solo ordine
    private Ordine ordine;

    public Long getIdSped() { return idSped; }

    public void setIdSped(Long idSped) { this.idSped = idSped;}

    public Date getDataSped() { return dataSped;}

    public void setDataSped(Date dataSped) { this.dataSped = dataSped;}

    public boolean isOrdinaria() { return isOrdinaria;}

    public void setOrdinaria(boolean ordinaria) { isOrdinaria = ordinaria;}

    public double getSpeseOrd() { return speseOrd;}

    public void setSpeseOrd(double speseOrd) { this.speseOrd = speseOrd;}

    public double getSpeseEpr() { return speseEpr;}

    public void setSpeseEpr(double speseEpr) { this.speseEpr = speseEpr;}

    public Ordine getOrdine() { return ordine;}

    public void setOrdine(Ordine ordine) { this.ordine = ordine;}
}

package com.unife.ecommerce.model.mo;

public class IndirizzoSpedizione {
    private Long idIndSped;
    private String citta;
    private String via;
    private String civico;
    private String cap;
    //(1,1) utente a cui è associato l'indirizzo di spedizione
    private Utente utente;
    //(0,n) ordini con quest'indirizzo di spedizione
    private Ordine[] ordini;

    public Long getIdIndSped() { return idIndSped;}

    public void setIdIndSped(Long idIndSped) { this.idIndSped = idIndSped;}

    public String getCitta() {return citta;}

    public void setCitta(String citta) {this.citta = citta;}

    public String getVia() {return via;}

    public void setVia(String via) {this.via = via;}

    public String getCivico() {return civico;}

    public void setCivico(String civico) {this.civico = civico;}

    public String getCap() {return cap;}

    public void setCap(String cap) { this.cap = cap;}

    public Utente getUtente() {return utente;}

    public void setUtente(Utente utente) { this.utente = utente;}

    public Ordine[] getOrdini() { return ordini;}

    public void setOrdini(Ordine[] ordini) { this.ordini = ordini;}
}

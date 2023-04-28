package com.unife.ecommerce.model.mo;

import java.sql.Date;
import java.util.ArrayList;

public class Ordine {
    private Long idOrd;
    private Date dataOrd;
    //(1,1) pagamento dell'ordine
    private Pagamento pag;
    //(1,1) spedizone dell'ordine
    private Spedizione sped;
    //(1,1) stato dell'ordine
    private Stato stato;
    //(1,1) utente che ha fatto l'ordine
    private Utente utente;
    //(1,1) indirizzo di spedizione dell'ordine
    private IndirizzoSpedizione indSped;

    //(1,n) array di prodotti con la loro quantità
    private ArrayList<ProdottoQty> prodQty; //pensa a soluzione migliore
    private boolean deleted;

    public Long getIdOrd() { return idOrd;}

    public void setIdOrd(Long idOrd) { this.idOrd = idOrd;}

    public Date getDataOrd() { return dataOrd;}

    public void setDataOrd(Date dataOrd) { this.dataOrd = dataOrd;}

    public Pagamento getPag() { return pag;}

    public void setPag(Pagamento pag) { this.pag = pag;}

    public Spedizione getSped() { return sped; }

    public void setSped(Spedizione sped) { this.sped = sped;}

    public Stato getStato() { return stato;}

    public void setStato(Stato stato) { this.stato = stato;}

    public Utente getUtente() { return utente;}

    public void setUtente(Utente utente) { this.utente = utente;}

    public IndirizzoSpedizione getIndSped() { return indSped;}

    public void setIndSped(IndirizzoSpedizione indSped) { this.indSped = indSped;}

    public ArrayList<ProdottoQty> getProdQty() {    return prodQty; }

    public void setProdQty(ArrayList<ProdottoQty> prodQty) {    this.prodQty = prodQty; }

    public boolean isDeleted() {    return deleted; }

    public void setDeleted(boolean deleted) {   this.deleted = deleted; }
}

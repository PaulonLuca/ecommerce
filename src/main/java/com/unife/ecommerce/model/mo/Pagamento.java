package com.unife.ecommerce.model.mo;

import java.sql.Date;

public class Pagamento {
    private Long idPag;
    private Date dataPag;
    private double totale;
    private String numeroCarta;
    private int mese;
    private int anno;
    private int cvv;
    //(1,1) tipologia di pagamento
    private TipoPagamento tipoPag;
    //(1,1) un pagamento ha un solo ordine associato
    private Ordine ordine;
    private boolean deleted;

    public Long getIdPaga() { return idPag;}

    public void setIdPaga(Long idPaga) {this.idPag = idPaga;}

    public Date getDataPag() {return dataPag;}

    public void setDataPag(Date dataPag) {this.dataPag = dataPag;}

    public double getTotale() {return totale;}

    public void setTotale(double totale) {this.totale = totale;}

    public TipoPagamento getTipoPag() {return tipoPag;}

    public void setTipoPag(TipoPagamento tipoPag) {this.tipoPag = tipoPag;}

    public String getNumeroCarta() {return numeroCarta;}

    public void setNumeroCarta(String numeroCarta) {this.numeroCarta = numeroCarta;}

    public Ordine getOrdine() { return ordine;}

    public void setOrdine(Ordine ordine) {this.ordine = ordine;}

    public boolean isDeleted() {    return deleted; }

    public void setDeleted(boolean deleted) {   this.deleted = deleted; }

    public int getMese() { return mese;}

    public void setMese(int mese) { this.mese = mese;}

    public int getAnno() { return anno;}

    public void setAnno(int anno) { this.anno = anno;}

    public int getCvv() { return cvv;}

    public void setCvv(int cvv) { this.cvv = cvv;}
}

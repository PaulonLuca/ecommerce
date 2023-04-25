package com.unife.ecommerce.model.mo;

public class Stato {
    private Long idStato;
    private String nomeStato;
    //(0,n) lista degli ordini che si trovano in questo stato
    Ordine[] ordini;

    public Long getIdStato() { return idStato; }

    public void setIdStato(Long idStato) { this.idStato = idStato; }

    public String getNomeStato() { return nomeStato; }

    public void setNomeStato(String nomeStato) { this.nomeStato = nomeStato; }
}
